package ksd.binews.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;

import ksd.binews.annotation.Authorization;
import ksd.binews.constants.MsgConstants;
import ksd.binews.dto.BaseResponse;
import ksd.binews.entity.TGuideArticle;
import ksd.binews.service.GuideArticleService;
import ksd.binews.utils.PublicUtil;

@RequestMapping("/admin")
@RestController
public class GuideArticleController {

	private static final Logger log = LoggerFactory.getLogger(GuideArticleController.class);
	
	@Autowired
	GuideArticleService guideArticleService;
	
	@Authorization
	@RequestMapping(path="/guide/articles",method=RequestMethod.GET)
	public BaseResponse list(@RequestParam(value="pageNum",defaultValue="1")int pageNum) {
		
		List<TGuideArticle> list = guideArticleService.list(pageNum, -1);
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
	
	@Authorization
	@RequestMapping(value="/guide/article",method=RequestMethod.GET)
	public BaseResponse getArticleForAdmin(String id) {
		if (StringUtils.isBlank(id)) {
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_MISSING);
		}
		
		TGuideArticle model = new TGuideArticle();
		model.setId(id);
		TGuideArticle item = guideArticleService.find(model);
		return BaseResponse.success(PublicUtil.ConvertObjToMap(item));
	}
	
	@Authorization
	@RequestMapping(value="/guide/article/add",method=RequestMethod.POST)
	public BaseResponse addArticle(@RequestBody @Validated TGuideArticle model, BindingResult bindingResult) {
		if(model == null || StringUtils.isBlank(model.getTitle()) || StringUtils.isBlank(model.getSubTitle()) 
				|| StringUtils.isBlank(model.getArticleType()+"") || StringUtils.isBlank(model.getContent()) || StringUtils.isBlank(model.getCreateBy())) {
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_MISSING);
		}
		if (bindingResult.hasErrors()) {
			for (FieldError err : bindingResult.getFieldErrors()) {
				log.info("error code = [" + err.getCode() + "]");
				return BaseResponse.error(
					err.getDefaultMessage() == null ? MsgConstants.MSG_PARAMETER_PATTERN_BAD : err.getDefaultMessage());
			}
		}
		
		int resultCount = guideArticleService.addArticle(model);
		if(resultCount > 0) {
			return BaseResponse.success();
		}else {
			return BaseResponse.error();
		}
	}
	
	@Authorization
	@RequestMapping(value="/guide/article/update",method=RequestMethod.POST)
	public BaseResponse updateArticle(@RequestBody @Validated TGuideArticle model, BindingResult bindingResult) {
		if(model == null || StringUtils.isBlank(model.getId())) {
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_MISSING);
		}
		if (bindingResult.hasErrors()) {
			for (FieldError err : bindingResult.getFieldErrors()) {
				log.info("error code = [" + err.getCode() + "]");
				return BaseResponse.error(
					err.getDefaultMessage() == null ? MsgConstants.MSG_PARAMETER_PATTERN_BAD : err.getDefaultMessage());
			}
		}
		
		int resultCount = guideArticleService.updateArticle(model);
		if(resultCount > 0) {
			return BaseResponse.success();
		}else {
			return BaseResponse.error();
		}
	}
	
	@Authorization
	@RequestMapping(path="/guide/article/del",method=RequestMethod.POST)
	public BaseResponse delArticle(@RequestBody TGuideArticle model) {
		if(model == null || StringUtils.isBlank(model.getId())) {
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_MISSING);
		}
		
		int resultCount = guideArticleService.delArticle(model.getId());
		if(resultCount > 0) {
			return BaseResponse.success();
		}else {
			return BaseResponse.error();
		}
	}
}
