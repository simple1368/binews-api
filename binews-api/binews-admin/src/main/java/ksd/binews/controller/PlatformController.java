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
import ksd.binews.entity.Platform;
import ksd.binews.service.PlatformService;
import ksd.binews.utils.PublicUtil;

/**
 * 交易所
 * 
 * @author Administrator
 *
 */
@RequestMapping("/admin")
@RestController
public class PlatformController {
	
	private static final Logger log = LoggerFactory.getLogger(PlatformController.class);

	@Autowired
	private PlatformService platformService;

	
	/**
	 * 添加交易所
	 * @param platform
	 * @param bindingResult
	 * @return
	 */
	@Authorization
	//@RequestMapping(path="/platform/add",method=RequestMethod.POST)
	public BaseResponse insertPlatform(@RequestBody @Validated Platform platform,
			BindingResult bindingResult) {

		log.info(platform.toString());
		
		if (platform == null || StringUtils.isBlank(platform.getLogo()) || StringUtils.isBlank(platform.getName()) 
				|| platform.getDayTradeAmount() == null
				|| StringUtils.isBlank(platform.getTradeType()) || StringUtils.isBlank(platform.getRemark())
				|| StringUtils.isBlank(platform.getArea())) {
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_MISSING);
		}
		
		if (bindingResult.hasErrors()) {
			for (FieldError err : bindingResult.getFieldErrors()) {
				log.info("error code = [" + err.getCode() + "]");
			}
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_PATTERN_BAD);
		}

		Map<String, Object> resultMap = platformService.insertFlatform(platform);
		if ((Integer) resultMap.get("code") <= 0) {
			return BaseResponse.error(MsgConstants.MSG_OPERATION_FAIL);
		}

		Map<String, Object> returnData = new HashMap<String, Object>();
		returnData.put("id", resultMap.get("id"));
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS, returnData);
	}
	
	/**
	 * 列表
	 * @param pageNum
	 * @return
	 */
	@Authorization
	@RequestMapping(path="/platform/list",method=RequestMethod.GET)
	public BaseResponse listPlatform(@RequestParam(value="pageNum",defaultValue="1") int pageNum) {

		Map<String, Object> condition = new HashMap<String, Object>();
		//condition.put("deletedFlag", "n");
		PageInfo pageInfo = platformService.listPlatformPage(pageNum, 10, condition);

		Map<String, Object> returnData = new HashMap<String, Object>();
		returnData.put("pageInfo", pageInfo);
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS, returnData);
	}
	
	/**
	 * 详情
	 * @param id
	 * @return
	 */
	@Authorization
	@RequestMapping(path="/platform",method=RequestMethod.GET)
	public BaseResponse Platform(@RequestParam(value="id") String id) {

		Platform platform = platformService.queryByPrimaryKey(id);

		Map<String, Object> returnData = new HashMap<String, Object>();
		returnData = PublicUtil.ConvertObjToMap(platform);
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS, returnData);
	}
	
	/**
	 * 修改交易所信息
	 * @param platform
	 * @param bindingResult
	 * @return
	 */
	@Authorization
	@RequestMapping(path="/platform/update",method=RequestMethod.POST)
	public BaseResponse updatePlatform(@RequestBody @Validated Platform platform,
			BindingResult bindingResult, @CurrentAdmin Admin admin) {
		
		if (platform == null || StringUtils.isBlank(platform.getId())) {
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_MISSING);
		}
		
		if (bindingResult.hasErrors()) {
			for (FieldError err : bindingResult.getFieldErrors()) {
				log.info("error code = [" + err.getCode() + "]");
			}
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_PATTERN_BAD);
		}
		
		int result = platformService.updatePlatform(platform, admin);
		if (result == 2) {
			return BaseResponse.error("未找到该交易所，或者已经被删除");
		}
		if (result <= 0) {
			return BaseResponse.error(MsgConstants.MSG_OPERATION_FAIL);
		}
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS);
	}
	
	/**
	 * 是否显示
	 * @param platform
	 * @param bindingResult
	 * @param admin
	 * @return
	 */
	@Authorization
	@RequestMapping(path="/platform/show",method=RequestMethod.POST)
	public BaseResponse showPlatform(@RequestBody @Validated Platform platform,
			BindingResult bindingResult, @CurrentAdmin Admin admin) {
		
		if (platform == null || StringUtils.isBlank(platform.getId())) {
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_MISSING);
		}
		
		if (bindingResult.hasErrors()) {
			for (FieldError err : bindingResult.getFieldErrors()) {
				log.info("error code = [" + err.getCode() + "]");
			}
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_PATTERN_BAD);
		}
		
		int result = platformService.updatePlatform(platform, admin);
		if (result == 2) {
			return BaseResponse.error("未找到该交易所，或者已经被删除");
		}
		if (result <= 0) {
			return BaseResponse.error(MsgConstants.MSG_OPERATION_FAIL);
		}
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS);
	}
	
	/**
	 * 删除交易所
	 * @param id
	 * @return
	 */
	@Authorization
	//@RequestMapping(path="/platform/del",method=RequestMethod.POST)
	public BaseResponse deletePlatform(@RequestBody Platform platform) {
		
		if (StringUtils.isBlank(platform.getId())) {
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_MISSING);
		}
		
		int result = platformService.deletePlatform(platform.getId());
		if (result == -2) {
			return BaseResponse.error("未找到该交易所，或者已经被删除");
		}
		if (result <= 0) {
			return BaseResponse.error(MsgConstants.MSG_OPERATION_FAIL);
		}
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS);
	}
}
