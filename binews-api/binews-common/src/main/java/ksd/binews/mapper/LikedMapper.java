package ksd.binews.mapper;

import java.util.Map;

import ksd.binews.entity.Liked;

public interface LikedMapper {
	
	Liked selectByPrimaryKey(String id);
	
	Liked selectUserRecordExist(Map<String, Object> paraMap);
	
	int insertSelective(Liked liked);
	
	int updateByPrimaryKeySelective(Liked liked);
	
	int deleteByPrimaryKey(String id);
}