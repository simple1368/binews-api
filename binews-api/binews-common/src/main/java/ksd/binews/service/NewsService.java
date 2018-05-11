package ksd.binews.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import ksd.binews.entity.News;
import ksd.binews.entity.NewsExpand;
import ksd.binews.mapper.NewsMapper;
import ksd.binews.utils.PublicUtil;

/**
 * 资讯
 * @author Administrator
 *
 */
@Service
public class NewsService {

	@Autowired
	private NewsMapper newsMapper;
	
	public News queryById(String id) {
		return newsMapper.selectNewsByPrimaryKey(id);
	}
	
	/**
	 * 查询资讯分页数据(前台)
	 * @param pageNum 当前页
	 * @param pageSize 每页大小
	 * @param paramMap 参数
	 * @return
	 */
	public PageInfo queryNewsPageInfoFront(int pageNum, int pageSize, Map<String, Object> paramMap) {
		PageHelper.startPage(pageNum,pageSize);
		
		List<NewsExpand> newsList = newsMapper.selectNewsListFront(paramMap);
		
		PageInfo pageInfo = new PageInfo<>(newsList);
		
		return pageInfo;
	}
	
	/**
	 * 定时查询新资讯
	 * @param paramMap
	 * @return
	 */
	public int queryNewsPageInfoTiming(String time) {
		int count = newsMapper.CountNewsByCondition(time);
		
		return count;
	}
	
	/**
	 * 查询资讯分页数据（后台）
	 * @param pageNum 当前页
	 * @param pageSize 每页大小
	 * @param paramMap 参数
	 * @return
	 */
	public PageInfo queryNewsPageInfo(int pageNum, int pageSize, Map<String, Object> paramMap) {
		PageHelper.startPage(pageNum,pageSize);
		
		List<NewsExpand> newsList = newsMapper.selectNewsList(paramMap);
		
		PageInfo pageInfo = new PageInfo<>(newsList);
		
		return pageInfo;
	}
	
	/**
	 * 新增资讯
	 * @param news
	 * @return
	 */
	public Map<String, Object> insertNews(News news,String adminId) {
		
		news.setId(PublicUtil.getUUID());
		news.setPublishTime(new Timestamp(new Date().getTime()));
		news.setLikedCount(0);
		news.setCommentCount(0);
		news.setShareCount(0);
		news.setCreateBy(adminId);
		news.setCreateTime(new Timestamp(new Date().getTime()));
		news.setDeletedFlag("n");
		
		int result = newsMapper.insertNews(news);
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("code", result);
		returnMap.put("id", news.getId());
		return returnMap;
	}
	
	/**
	 * 更新资讯
	 * @param news
	 * @return
	 */
	public Map<String, Object> updateNews(News news,String adminId) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		if (StringUtils.isBlank(news.getId())) {
			returnMap.put("code", -1);
			return returnMap;
		}
		
		News record = newsMapper.selectNewsByPrimaryKey(news.getId());
		if (record == null || record.getDeletedFlag().toString().toLowerCase().equals("y")) {
			returnMap.put("code", -2);
			return returnMap;
		}
		
		//不允许修改的字段
		news.setPublishTime(null);
		news.setCreateBy(null);
		news.setCreateTime(null);
		news.setDeletedFlag(null);
		
		news.setUpdateBy(adminId);
		news.setUpdateTime(PublicUtil.getCurrentTimestamp());
		
		int result = newsMapper.updateByPrimaryKeySelective(news);
		
		returnMap.put("code", result);
		returnMap.put("id", news.getId());
		return returnMap;
	}
	
	/**
	 * 修改新闻属性数量
	 * @param attr（ 1:likedCount、2:commentCount、3:shareCount ）
	 * @param flag（ +1 、 -1 ）
	 * @param newsId 资讯id
	 * @return
	 */
	public int attrOperation(int attr, int flag, String newsId) {
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("attr", attr);
		paramMap.put("flag", flag);
		paramMap.put("id", newsId);
		
		int result = newsMapper.updateAttrCount(paramMap);
		
		return result;
	}
	
	/**
	 * 删除资讯
	 * @param id
	 * @return
	 */
	public int deleteNewsById(String id,String adminId) {
		
		News news = newsMapper.selectNewsByPrimaryKey(id);
		//判断是否已经删除
		if (news == null || news.getDeletedFlag().trim().toLowerCase().equalsIgnoreCase("y")) {
			return -2;
		}
		int result = -1;
		news = new News();
		news.setId(id);
		news.setUpdateBy(adminId);
		news.setUpdateTime(new Timestamp(new Date().getTime()));
		news.setDeletedFlag("y");
		
		result = newsMapper.updateByPrimaryKeySelective(news);
		
		return result;
	}
}
