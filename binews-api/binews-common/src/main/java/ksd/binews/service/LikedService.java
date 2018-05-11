package ksd.binews.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ksd.binews.entity.Liked;
import ksd.binews.entity.User;
import ksd.binews.mapper.LikedMapper;
import ksd.binews.utils.PublicUtil;

@Service
public class LikedService {

	@Autowired
	private LikedMapper likedMapper;
	@Autowired
	private NewsService newsService;
	
	/**
	 * 点赞或者取消点赞
	 * @param user
	 * @param newsId
	 * @return
	 */
	@Transactional
	public Map<String, Object> liked(User user, String newsId) {

		//查询当前用户是否已经点赞过
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("newsId", newsId);
		paramMap.put("likedBy", user.getId());
		
		Liked liked = likedMapper.selectUserRecordExist(paramMap);

		boolean flag = (liked == null) ? true : false;//true:点赞 false:取消点赞
		String likedId = "";
		if (!flag) {
			likedId = liked.getId();
		}
		
		//增加点赞记录
		int result = -1;
		liked = new Liked();
		if (flag) {
			liked.setId(PublicUtil.getUUID());
			liked.setNewsId(newsId);
			liked.setLikedBy(user.getId());
			liked.setLikedTime(new Timestamp(new Date().getTime()));
			liked.setCreateTme(new Timestamp(new Date().getTime()));
			liked.setDeletedFlag("n");
			
			result = likedMapper.insertSelective(liked);
		} else {
			liked.setId(likedId);
			liked.setUpdateTime(new Timestamp(new Date().getTime()));
			liked.setDeletedFlag("y");
			
			result = likedMapper.updateByPrimaryKeySelective(liked);
		}
		if (result <= 0) {
			throw new RuntimeException("transaction rollback");
		}
		
		//增加资讯的点赞数量
		result = newsService.attrOperation(1, flag ? +1 : -1, newsId);
		if (result <= 0) {
			throw new RuntimeException("transaction rollback");
		}

		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("code", result);
		returnMap.put("flag", flag);
		return returnMap;
	}

}
