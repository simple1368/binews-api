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
import org.springframework.web.bind.annotation.ModelAttribute;
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
import ksd.binews.entity.Nav;
import ksd.binews.entity.NavExpand;
import ksd.binews.service.NavService;

@RequestMapping("/admin")
@RestController
public class NavController {
	
	private static final Logger log = LoggerFactory.getLogger(NewsController.class);

	@Autowired
	private NavService navService;
	
	/**
	 * 查询列表
	 * @param pageNum
	 * @param model
	 * @return
	 */
	@Authorization
	@RequestMapping(path="/nav/list",method=RequestMethod.GET)
	public BaseResponse list(@RequestParam(value="pageNum",defaultValue="1") int pageNum,
			@ModelAttribute("model") NavExpand model) {
		
		NavExpand condition = new NavExpand();
		condition.setDeletedFlag("n");
		PageInfo pageInfo = navService.getListPage(pageNum, 10, condition);
		
		Map<String, Object> returnData = new HashMap<String, Object>();
		returnData.put("pageInfo", pageInfo);
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS, returnData);
	}
	
	/**
	 * 查询详情
	 * @param id
	 * @return
	 */
	@Authorization
	@RequestMapping(path="/nav",method=RequestMethod.GET)
	public BaseResponse nav(@RequestParam("id") String id) {
		
		NavExpand nav = navService.selectDetailById(id);
		
		Map<String, Object> returnData = new HashMap<String, Object>();
		returnData.put("nav", nav);
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS, returnData);
	}

	/**
	 * 添加
	 * @param model
	 * @param bindingResult
	 * @param admin
	 * @return
	 */
	@Authorization
	@RequestMapping(path="/nav/add",method=RequestMethod.POST)
	public BaseResponse add(@RequestBody @Validated NavExpand model, 
			BindingResult bindingResult,@CurrentAdmin Admin admin) {
		
		if (model == null) {
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_MISSING);
		}
		
		if (bindingResult.hasErrors()) {
			for (FieldError err : bindingResult.getFieldErrors()) {
				log.info("error code = [" + err.getCode() + "]");
			}
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_PATTERN_BAD);
		}
		int result = -1;
		try {
			result = navService.addNav(model, admin);
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			return BaseResponse.error(MsgConstants.MSG_OPERATION_FAIL);
		}
		
		if (result <= 0) {
			return BaseResponse.error(MsgConstants.MSG_OPERATION_FAIL);
		}
		
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS);
	}
	
	/**
	 * 修改
	 * @param model
	 * @param bindingResult
	 * @param admin
	 * @return
	 */
	@Authorization
	@RequestMapping(path = "/nav/update", method = RequestMethod.POST)
	public BaseResponse updateNav(@RequestBody @Validated NavExpand model, 
			BindingResult bindingResult,@CurrentAdmin Admin admin) {

		if (model == null || StringUtils.isBlank(model.getId())) {
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_MISSING);
		}
		if (bindingResult.hasErrors()) {
			for (FieldError err : bindingResult.getFieldErrors()) {
				log.info("error code = [" + err.getCode() + "]");
			}
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_PATTERN_BAD);
		}

		int result = -1;
		try {
			result = navService.updateNav(model, admin);
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			return BaseResponse.error(MsgConstants.MSG_OPERATION_FAIL);
		}
		
		if (result <= 0) {
			return BaseResponse.error(MsgConstants.MSG_OPERATION_FAIL);
		}
		
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS);
	}
	
	/**
	 * 删除
	 * @param model
	 * @param admin
	 * @return
	 */
	@Authorization
	@RequestMapping(path = "/nav/del", method = RequestMethod.POST)
	public BaseResponse deleteNav(@RequestBody @Validated Nav model,
			@CurrentAdmin Admin admin) {
		
		if (StringUtils.isBlank(model.getId())) {
			return BaseResponse.error("请选择要删除的导航");
		}
		int result = -1;
		try {
			result = navService.deleteNav(model.getId(), admin);
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			return BaseResponse.error(MsgConstants.MSG_OPERATION_FAIL);
		}
		
		if (result == -2) {
			return BaseResponse.error("未找到该条记录，或者已经被删除");
		}
		if (result <= 0) {
			return BaseResponse.error(MsgConstants.MSG_OPERATION_FAIL);
		}
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS);
	}
	
	@Authorization
	@RequestMapping(path = "/digit/list", method = RequestMethod.GET)
	public BaseResponse digitList() {
		
		Map<String, Object> returnData = new HashMap<String, Object>();
		returnData.put("list", navService.digitList());
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS,returnData);
	}
	
	@Authorization
	@RequestMapping(path = "/digit/in", method = RequestMethod.GET)
	public BaseResponse digitAdd() {
		navService.insertDigit("BCC,BTC,DASH,ETC,ETH,LTC,XMR,ZEC,EOS,QYUM,ZCASH");
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS);
	}
}
