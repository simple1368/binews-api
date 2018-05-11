package ksd.binews.mapper;

import java.util.List;
import java.util.Map;

import ksd.binews.entity.Platform;

public interface PlatformMapper {
	
	Platform selectByPrimaryKey(String id);
	
	Platform selectPlatformDetail(String id);
	
	Platform selectPlatformRemark(String id);
	
	List<Platform> selectPlatformRanking(Map<String, Object> condition);
	
	List<Platform> selectListCondition(Map<String, Object> condition);
	
	List<Platform> selectPlatformSearchList(Map<String, Object> paramMap);
	
	int selectPlatformCount();
	
    int insertSelective(Platform platform);
    
    List<Platform> selectPlatformListCondition(Map<String, Object> condition);
    
    int updateByPrimaryKeySelective(Platform platform);
    
    int updateTotalDay(String id);
    
    int deleteByPrimaryKey(Platform platform);
}