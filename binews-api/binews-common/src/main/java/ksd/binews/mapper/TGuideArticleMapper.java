package ksd.binews.mapper;

import java.util.List;
import java.util.Map;

import ksd.binews.entity.TGuideArticle;

public interface TGuideArticleMapper {

	List<TGuideArticle> list(Map<String, Object> map);
	
	int countList(Map<String, Object> map);

	TGuideArticle getById(String id);

	int addArticle(TGuideArticle model);
	
	int delArticle(String id);

	int updateArticle(TGuideArticle model);
}
