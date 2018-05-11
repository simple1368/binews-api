package ksd.binews.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;

import ksd.binews.constants.MsgConstants;
import ksd.binews.dto.BaseResponse;
import ksd.binews.entity.TGuideArticle;
import ksd.binews.service.GuideArticleService;
import ksd.binews.utils.PublicUtil;

@RestController
public class GuideArticleController {

	private static final Logger log = LoggerFactory.getLogger(GuideArticleController.class);
	
	@Autowired
	GuideArticleService guideArticleService;
	
	@RequestMapping(path="/guide/articles",method=RequestMethod.GET)
	public BaseResponse listFront(@RequestParam(value="pageNum",defaultValue="1")int pageNum,
			@RequestParam("articleType") int type) {
		List<TGuideArticle> list = guideArticleService.list(pageNum,type);
		if(!CollectionUtils.isEmpty(list)) {
			Map<String, Object> map = Maps.newHashMap();
			map.put("list", list);
			map.put("total", guideArticleService.countList(pageNum));
			return BaseResponse.success(map);
		}else {
			Map<String, Object> map = Maps.newHashMap();
			map.put("list", null);
			map.put("total", 0);
			return BaseResponse.success(map);
		}
	}
	
	@RequestMapping(path="/guide/articles/count",method=RequestMethod.GET)
	public BaseResponse countList(@RequestParam(value="pageNum",defaultValue="1")int pageNum) {
		Map<String, Object> map = Maps.newHashMap();
		map.put("count", guideArticleService.countList(pageNum));
		return BaseResponse.success(map);		
	}
	
	@RequestMapping(value="/guide/article",method=RequestMethod.GET)
	public BaseResponse getArticle(String id) {
		if (StringUtils.isBlank(id)) {
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_MISSING);
		}
		
		TGuideArticle model = new TGuideArticle();
		model.setId(id);
		TGuideArticle item = guideArticleService.find(model);
		return BaseResponse.success(PublicUtil.ConvertObjToMap(item));
	}
	
}
