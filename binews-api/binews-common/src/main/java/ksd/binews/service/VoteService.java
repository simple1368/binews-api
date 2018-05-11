package ksd.binews.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;

import ksd.binews.entity.News;
import ksd.binews.entity.Vote;
import ksd.binews.mapper.VoteMapper;
import ksd.binews.utils.PublicUtil;

@Service
public class VoteService {

	@Autowired
	private VoteMapper voteMapper;
	@Autowired
	private NewsService newsService;

	/**
	 * 投票
	 * 
	 * @param vote
	 * @return
	 */
	@Transactional
	public Map<String, Object> vote(Vote vote) {
		Vote condition = new Vote();
		condition.setNewsId(vote.getNewsId());
		condition.setUuid(vote.getUuid());
		condition.setDeletedFlag("n");
		Vote record = voteMapper.selectByCondition(condition);
		
		int voteType = vote.getType() == 1 ? 4 : 5;
		
		int resultCount = 0;
		if(record == null){
			// 之前没有投票记录
			vote.setId(PublicUtil.getUUID());
			vote.setVoteTime(PublicUtil.getCurrentTimestamp());
			vote.setCreateTime(PublicUtil.getCurrentTimestamp());
			vote.setDeletedFlag("n");
			resultCount = voteMapper.insertSelective(vote);		
			if(resultCount < 1){
				throw new RuntimeException("[error]添加记录失败");
			}
			resultCount = newsService.attrOperation(voteType , 1, vote.getNewsId());
		}else{
			// 之前有投票记录
			// 取消之前投票记录
			vote.setId(record.getId());
			vote.setUpdateTime(PublicUtil.getCurrentTimestamp());
			vote.setDeletedFlag("y");
			resultCount = voteMapper.updateByPrimaryKeySelective(vote);
			if(resultCount < 1){
				throw new RuntimeException("[error]取消之前投票记录失败");
			}
			
			if(vote.getType() == record.getType()){
				// 取消
				// 数量减1
				resultCount = newsService.attrOperation(voteType , -1, vote.getNewsId());
			}else{
				// 反向投票
				// 把之前的投票数量减1
				resultCount = newsService.attrOperation(voteType == 4 ? 5:4, -1, vote.getNewsId());
				if(resultCount < 1){
					throw new RuntimeException("[error]把之前的投票数量减1失败");
				}
				// 当前的投票数量加1
				resultCount = newsService.attrOperation(voteType , 1, vote.getNewsId());
				if(resultCount < 1){
					throw new RuntimeException("[error] 当前的投票数量加1失败");
				}
				// 添加当前投票记录
				vote.setId(PublicUtil.getUUID());
				vote.setVoteTime(PublicUtil.getCurrentTimestamp());
				vote.setCreateTime(PublicUtil.getCurrentTimestamp());
				vote.setDeletedFlag("n");
				resultCount = voteMapper.insertSelective(vote);
			}
		}
		
		Map<String, Object> returnMap = Maps.newHashMap();
		if(resultCount > 0){
			News news = newsService.queryById(vote.getNewsId());
			
			if (news != null) {
				returnMap.put("code", resultCount);
				returnMap.put("upCount", news.getVoteUpCount());
				returnMap.put("downCount", news.getVoteDownCount());
				return returnMap;
			}
		}
		throw new RuntimeException("[error]执行失败");
		
//		int result = -1;
//		if (record == null) {
//			// 添加
//			vote.setId(PublicUtil.getUUID());
//			vote.setVoteTime(PublicUtil.getCurrentTimestamp());
//			vote.setCreateTime(PublicUtil.getCurrentTimestamp());
//			result = voteMapper.insertSelective(vote);
//		} else {
//			// 取消
//			vote.setId(record.getId());
//			vote.setUpdateTime(PublicUtil.getCurrentTimestamp());
//			vote.setDeletedFlag("y");
//			result = voteMapper.updateByPrimaryKeySelective(vote);
//		}
//		if (result <= 0) {
//			throw new RuntimeException("投票失败");
//		}
	
//		Vote condtion = new Vote();
//		condtion.setNewsId(vote.getNewsId());
//		condtion.setUuid(vote.getUuid());
//		condtion.setType(vote.getType() == 1 ? 2 : (vote.getType() == 2) ? 1 : 0);
//		condtion.setDeletedFlag("n");
//		Vote record2 = voteMapper.selectByCondition(condtion);
		
		
//		if (record2 != null) {
//			condtion = new Vote();
//			condtion.setId(record2.getId());
//			condtion.setUpdateTime(PublicUtil.getCurrentTimestamp());
//			condtion.setDeletedFlag("y");
//			result = voteMapper.updateByPrimaryKeySelective(condtion);
//			if (result <= 0) {
//				throw new RuntimeException("恢复之前有效操作失败");
//			}
//			//恢复之前操作数量
//			int op = vote.getType() == 1 ? 5 : (vote.getType() == 2) ? 4 : 0;
//			result = newsService.attrOperation(op, -1, vote.getNewsId());
//			if (result <= 0) {
//				throw new RuntimeException("恢复之前有效操作失败");
//			}
//		}
//		
//		// 增加资讯的点赞数量
//		int op = vote.getType() == 1 ? 4 : (vote.getType() == 2) ? 5 : 0;
//		result = newsService.attrOperation(op, record == null ? +1 : -1, vote.getNewsId());
//		if (result <= 0) {
//			throw new RuntimeException("更新数量失败");
//		}
//		News news = newsService.queryById(vote.getNewsId());
//		Map<String, Object> returnMap = new HashMap<String, Object>();
//		if (news != null) {
//			returnMap.put("code", result);
//			returnMap.put("upCount", news.getVoteUpCount());
//			returnMap.put("downCount", news.getVoteDownCount());
//			return returnMap;
//		}
//		returnMap.put("code", -1);
//		return returnMap;
	}
}
