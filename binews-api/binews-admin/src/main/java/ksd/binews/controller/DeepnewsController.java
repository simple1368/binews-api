package ksd.binews.controller;

import java.util.HashMap;
import java.util.Map;

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

import com.github.pagehelper.PageInfo;

import ksd.binews.annotation.Authorization;
import ksd.binews.annotation.CurrentAdmin;
import ksd.binews.constants.MsgConstants;
import ksd.binews.dto.BaseResponse;
import ksd.binews.entity.Admin;
import ksd.binews.entity.Deepnews;
import ksd.binews.service.DeepnewsService;
import ksd.binews.utils.PublicUtil;

/**
 * 深度资讯
 * @author Administrator
 *
 */
@RequestMapping("/admin")
@RestController
public class DeepnewsController {
	
	private static final Logger logger = LoggerFactory.getLogger(DeepnewsController.class);

	@Autowired
	private DeepnewsService deepnewsService;
	
	/**
	 * 查询深度资讯列表（后台）
	 * @param pageNum
	 * @return
	 */
	@Authorization
	@RequestMapping(path="/deepnews/list",method=RequestMethod.GET)
	public BaseResponse queryDeepnewsPage(@RequestParam(value="pageNum",defaultValue="1")int pageNum) {
		
		pageNum = pageNum < 1 ? 1 : pageNum;
		pageNum = pageNum > 1000 ? 1000 : pageNum;
		
		PageInfo pageInfo = deepnewsService.queryDeepnewsPage(pageNum, 10);
		
		Map<String, Object> returnData = new HashMap<String, Object>();
		returnData.put("pageInfo", pageInfo);
		
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS,returnData);
	}
	
	/**
	 * 查询深度资讯详情（后台）
	 * @param id
	 * @return
	 */
	@Authorization
	@RequestMapping(path="/deepnews",method=RequestMethod.GET)
	public BaseResponse queryDeepnewsDetail(String id) {
		if (StringUtils.isBlank(id)) {
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_MISSING);
		}
		
		Map<String, Object> returnData = new HashMap<String, Object>();
		returnData = PublicUtil.ConvertObjToMap(deepnewsService.queryDeepnewsDetail(id));
		
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS,returnData);
	}
	
	/**
	 * 新增深度资讯
	 * @param deepnews
	 * @param bindingResult
	 * @return
	 */
	@Authorization
	@RequestMapping(path="/deepnews/add",method=RequestMethod.POST)
	public BaseResponse addDeepnews(@RequestBody @Validated Deepnews deepnews,
			BindingResult bindingResult,@CurrentAdmin Admin admin) {
		
		if (deepnews == null) {
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_MISSING);
		}
		if (bindingResult.hasErrors()) {
			for (FieldError err : bindingResult.getFieldErrors()) {
				logger.info("error code = [" + err.getCode() + "]");
				return BaseResponse.error(StringUtils.isBlank(err.getDefaultMessage()) ? 
						MsgConstants.MSG_PARAMETER_PATTERN_BAD : err.getDefaultMessage());
			}
		}
		
		int result = deepnewsService.insertDeepnews(deepnews, admin);
		
		if (result <= 0) {
			return BaseResponse.error(MsgConstants.MSG_OPERATION_FAIL);
		}
		
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS);
	}
	
	/**
	 * 更新深度资讯
	 * @param deepnews
	 * @param bindingResult
	 * @return
	 */
	@Authorization
	@RequestMapping(path="/deepnews/update",method=RequestMethod.POST)
	public BaseResponse updateDeepnews(@RequestBody @Validated Deepnews deepnews,
			BindingResult bindingResult,@CurrentAdmin Admin admin) {
		
		if (deepnews == null || StringUtils.isBlank(deepnews.getId())) {
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_MISSING);
		}
		if (bindingResult.hasErrors()) {
			for (FieldError err : bindingResult.getFieldErrors()) {
				logger.info("error code = [" + err.getCode() + "]");
				return BaseResponse.error(StringUtils.isBlank(err.getDefaultMessage()) ? 
						MsgConstants.MSG_PARAMETER_PATTERN_BAD : err.getDefaultMessage());
			}
		}
		
		int result = deepnewsService.updateDeepnews(deepnews, admin);
		
		if (result <= 0) {
			return BaseResponse.error(MsgConstants.MSG_OPERATION_FAIL);
		}
		
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS);
	}
	
	/**
	 * 删除深度资讯
	 * @param id
	 * @return
	 */
	@Authorization
	@RequestMapping(path="/deepnews/del",method=RequestMethod.POST)
	public BaseResponse deleteDeepnews(@RequestBody Deepnews deepnews) {
		
		if (StringUtils.isBlank(deepnews.getId())) {
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_MISSING);
		}
		
		int result = deepnewsService.deleteDeepnews(deepnews.getId());
		
		if (result == -2) {
			return BaseResponse.error("该资讯不存在，或者已经被删除");
		}
		if (result <= 0) {
			return BaseResponse.error(MsgConstants.MSG_OPERATION_FAIL);
		}
		
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS);
	}
}
