package ksd.binews.mapper;


import java.util.List;

import ksd.binews.entity.Comments;
import ksd.binews.entity.CommentsExpand;

public interface CommentsMapper {
	
	Comments selectByPrimaryKey(String id);
	
	List<Comments> selectByNews(String newsId);
	
	List<CommentsExpand> selectListCondition(CommentsExpand comments);
	
	int insertComments(Comments comments);
	
	int updateComments(Comments comments);
	
	int deleteByPrimaryKey(Comments comments);
}
