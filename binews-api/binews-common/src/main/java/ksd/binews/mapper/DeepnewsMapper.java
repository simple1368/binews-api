package ksd.binews.mapper;

import java.util.List;
import java.util.Map;

import ksd.binews.entity.Deepnews;

public interface DeepnewsMapper {
	
	Deepnews selectByPrimaryKey(String id);
    
	List<Deepnews> selectDeepnewsListFront();
	
	List<Deepnews> selectDeepnewsList();
	
	Deepnews selectDeepnewsDetail(String id);
	
	Deepnews selectDeepnewsNext(Map<String, Object> paramMap);
	
	int insertSelective(Deepnews deepnews);
	
	int updateBrowserCount(String id);
	
	int updateByPrimaryKeySelective(Deepnews deepnews);
	
	int deleteByPrimaryKey(String id);
}