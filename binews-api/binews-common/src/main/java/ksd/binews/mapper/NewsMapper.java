package ksd.binews.mapper;

import java.util.List;
import java.util.Map;

import ksd.binews.entity.News;
import ksd.binews.entity.NewsExpand;

public interface NewsMapper {
	
	List<NewsExpand> selectNewsListFront(Map<String, Object> paramMap);
	
	List<NewsExpand> selectNewsList(Map<String, Object> paramMap);
	
	int CountNewsByCondition(String time);
	
	News selectNewsByPrimaryKey(String id);
	
	int insertNews(News news);
	
	int deleteNewsByPrimaryKey(String id);
	
	int updateByPrimaryKeySelective(News news);
	
	int updateAttrCount(Map<String, Object> paramMap);
}
