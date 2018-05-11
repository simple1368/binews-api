package ksd.binews.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ksd.binews.constants.MsgConstants;
import ksd.binews.dto.BaseResponse;
import ksd.binews.entity.DigitCurrency;
import ksd.binews.entity.Nav;
import ksd.binews.entity.NavExpand;
import ksd.binews.service.NavService;

/**
 * 导航
 * @author simple
 * @date 2018年4月13日下午4:58:52
 */
@RestController
public class NavController {
	
	private static final Logger log = LoggerFactory.getLogger(NewsController.class);

	@Autowired
	private NavService navService;
	
	/**
	 * 列表
	 * @param model
	 * @return
	 */
	@RequestMapping(path="/nav/list",method=RequestMethod.GET)
	public BaseResponse list(@ModelAttribute NavExpand model) {
		
		if (model.getType() == null) {
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_MISSING);
		}
		
		NavExpand condition = new NavExpand();
		condition.setType(model.getType());
		condition.setDeletedFlag("n");
		condition.setCountry(model.getCountry());
		condition.setTradeType(model.getTradeType());
		condition.setSupportLang(model.getSupportLang());
		condition.setTradeMade(model.getTradeMade());
		condition.setIdentityType(model.getIdentityType());
		condition.setTechType(model.getTechType());
		condition.setCurrency(model.getCurrency());
		List<Nav> list = navService.getList(condition);
		
		Map<String, Object> returnData = new HashMap<String, Object>();
		returnData.put("list", list);
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS, returnData);
	}
	
	/**
	 * 详情
	 * @param id
	 * @return
	 */
	@RequestMapping(path="/nav",method=RequestMethod.GET)
	public BaseResponse info(@RequestParam("id") String id) {
		
		if (StringUtils.isBlank(id)) {
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_MISSING);
		}
		
		Nav nav = navService.selectDetailByIdFront(id);
		
		Map<String, Object> returnData = new HashMap<String, Object>();
		returnData.put("nav", nav);
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS, returnData);
	}
	
	/**
	 * 导航支持币种列表
	 * @param id
	 * @return
	 */
	@RequestMapping(path="/nav/currency",method=RequestMethod.GET)
	public BaseResponse currency(@RequestParam("id") String id) {
		
		if (StringUtils.isBlank(id)) {
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_MISSING);
		}
		
		List<DigitCurrency> list = navService.currency(id);
		
		Map<String, Object> returnData = new HashMap<String, Object>();
		returnData.put("list", list);
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS, returnData);
	}
	
	@RequestMapping(path = "/digit/list", method = RequestMethod.GET)
	public BaseResponse digitList() {
		
		Map<String, Object> returnData = new HashMap<String, Object>();
		returnData.put("list", navService.digitListFront());
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS,returnData);
	}
	
	@RequestMapping(path = "/digit/top", method = RequestMethod.GET)
	public BaseResponse digitTopList() {
		
		Map<String, Object> returnData = new HashMap<String, Object>();
		returnData.put("list", navService.digitTopList());
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS,returnData);
	}
	
	/**
	 * 数量
	 * @return
	 */
	@RequestMapping(path="/nav/count",method=RequestMethod.GET)
	public BaseResponse count() {
		
		Map<String, Object> count = navService.count();
		
		Map<String, Object> returnData = new HashMap<String, Object>();
		returnData = count;
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS, returnData);
	}
}
