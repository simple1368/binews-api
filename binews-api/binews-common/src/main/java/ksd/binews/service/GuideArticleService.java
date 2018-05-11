package ksd.binews.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;

import ksd.binews.entity.TGuideArticle;
import ksd.binews.mapper.TGuideArticleMapper;
import ksd.binews.utils.PublicUtil;

@Service
public class GuideArticleService {
	
	@Autowired
	TGuideArticleMapper guideArticleMapper;

	public List<TGuideArticle> list(int pageNum,int type){
		Map<String, Object> condition = Maps.newHashMap();
		condition.put("deletedFlag", "N");
		if (type != -1) {
			condition.put("type", type);
		}
		condition.put("start", (pageNum-1)*10);	
		condition.put("end", 10);
		return guideArticleMapper.list(condition);
	}
	
	public int countList(int pageNum){
		Map<String, Object> condition = Maps.newHashMap();
		condition.put("deletedFlag", "N");
		condition.put("start", 0);	
		condition.put("end", 10);
		return guideArticleMapper.countList(condition);
	}

	public TGuideArticle find(TGuideArticle guideArticle) {
		return guideArticleMapper.getById(guideArticle.getId());
	}

	public int addArticle(TGuideArticle model) {
		model.setPublishTime(new Date());
		model.setCreateTime(new Date());
		model.setUpdateBy(model.getCreateBy());
		model.setUpdateTime(new Date());
		model.setId(PublicUtil.getUUID());
		model.setDeletedFlag("N");
		return guideArticleMapper.addArticle(model);
	}

	public int delArticle(String id) {
		return guideArticleMapper.delArticle(id);
	}

	public int updateArticle(TGuideArticle model) {
		model.setUpdateBy(model.getUpdateBy());
		model.setUpdateTime(new Date());
		return guideArticleMapper.updateArticle(model);
	}
}
