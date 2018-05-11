package ksd.binews.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import ksd.binews.entity.Comments;
import ksd.binews.entity.CommentsExpand;
import ksd.binews.mapper.CommentsMapper;
import ksd.binews.utils.PublicUtil;

@Service
public class CommentsService {

	@Autowired
	private CommentsMapper commentsMapper;
	@Autowired
	private NewsService newsService;
	

	
	/**
	 * 查询资讯评论分页
	 * @param pageNum 当前页
	 * @param pageSize 分页大小
	 * @param newsId 资讯id
	 * @return
	 */
	public PageInfo<Comments> queryNewsComments(int pageNum, int pageSize, String newsId) {
		PageHelper.startPage(pageNum, pageSize);
		
		List<Comments> commentslist = commentsMapper.selectByNews(newsId);
		for (Comments comments : commentslist) {
			comments.setCommentBy(comments.getCommentBy().
					replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
		}
		PageInfo<Comments> pageInfo = new PageInfo<Comments>(commentslist);
		return pageInfo;
	}
	
	public List<CommentsExpand> queryListCondition(CommentsExpand condition) {
		return commentsMapper.selectListCondition(condition);
	}
	
	public PageInfo queryListPageCondition(int pageNum, int pageSize, CommentsExpand condition) {
		
		PageHelper.startPage(pageNum,pageSize);
		List<CommentsExpand> list = commentsMapper.selectListCondition(condition);
		PageInfo pageInfo = new PageInfo(list);
		return pageInfo;
	}
	
	/**
	 * 审核
	 * @param comments
	 * @return
	 */
	public int audit(Comments comments) {
		if (comments == null) {
			return -1;
		}
		Comments record = commentsMapper.selectByPrimaryKey(comments.getId());
		if (record == null || record.getDeletedFlag().toString().toLowerCase().equals("y")) {
			return -2;
		}
		
		comments.setUpdateTime(PublicUtil.getCurrentTimestamp());
		int result = commentsMapper.updateComments(comments);
		
		return result;
	}
	
	/**
	 * 发表评论
	 * 
	 * @param comments
	 * @return
	 */
	@Transactional
	public Map<String, Object> publishComments(Comments comments) {
		
		comments.setId(PublicUtil.getUUID());
		comments.setCommentTime(PublicUtil.getCurrentTimestamp());
		comments.setCreateTime(PublicUtil.getCurrentTimestamp());
		comments.setUpdateTime(PublicUtil.getCurrentTimestamp());
		comments.setShowFlag("n");
		comments.setDeletedFlag("n");

		int result = commentsMapper.insertComments(comments);
		if (result <= 0) {
			throw new RuntimeException("transaction rollback");
		}

		//添加资讯的评论数量
		result = newsService.attrOperation(2, +1, comments.getNewsId());
		if (result <= 0) {
			throw new RuntimeException("transaction rollback");
		}

		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("code", result);
		returnMap.put("id", comments.getId());
		return returnMap;
	}

	/**
	 * 删除评论
	 * @param id
	 * @return
	 */
	@Transactional
	public int deleteCommentsById(String id) {

		Comments comments = commentsMapper.selectByPrimaryKey(id);
		// 判断是否已经删除
		if (comments.getDeletedFlag().trim().toLowerCase().equalsIgnoreCase("y")) {
			return -2;
		}
		int result = -1;
		Comments param = new Comments();
		param.setId(id);
		param.setUpdateTime(PublicUtil.getCurrentTimestamp());
		result = commentsMapper.deleteByPrimaryKey(param);
		if (result <= 0) {
			throw new RuntimeException("transaction rollback");
		}
		
		//减少资讯的评论数量
		result = newsService.attrOperation(2, -1, comments.getNewsId());
		if (result <= 0) {
			throw new RuntimeException("transaction rollback");
		}
		
		return result;
	}

}
