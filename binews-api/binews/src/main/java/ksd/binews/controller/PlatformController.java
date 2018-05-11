package ksd.binews.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ksd.binews.constants.MsgConstants;
import ksd.binews.dto.BaseResponse;
import ksd.binews.entity.CoinExpand;
import ksd.binews.entity.Example;
import ksd.binews.service.PlatformService;
import ksd.binews.utils.PublicUtil;

/**
 * 交易所
 * 
 * @author Administrator
 *
 */
@RestController
public class PlatformController {
	
	private static final Logger log = LoggerFactory.getLogger(PlatformController.class);

	@Autowired
	private PlatformService platformService;

	/**
	 * 查询交易所详情
	 * @param id
	 * @return
	 */
	@RequestMapping(path="/platform",method=RequestMethod.GET)
	public BaseResponse queryPlatformDetail(String id) {
		
		if (StringUtils.isBlank(id)) {
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_MISSING);
		}
		
		Map<String, Object> returnData = new HashMap<String, Object>();
		returnData = PublicUtil.ConvertObjToMap(platformService.queryPlatformDetail(id));
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS, returnData);
	}
	
	/**
	 * 查询交易所市场行情
	 * @param platformId 交易所id
	 * @param 
	 * @return
	 */
	@RequestMapping(path="/platform/mkt",method=RequestMethod.GET)
	public BaseResponse queryMarketQuotation(@RequestParam("id") String id,
			@RequestParam(value="order",required=false) String order,
			@RequestParam(value="orderBy",required=false) String orderBy,
			@RequestParam(value="group",defaultValue="1") int group) {
		
		if (StringUtils.isBlank(id)) {
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_MISSING);
		}
		
		List<CoinExpand> returnList = new ArrayList<CoinExpand>();
		Map returnData = new HashMap();
		Example example = new Example();
		example.setOrder(order);
		example.setOrderBy(orderBy);
		
		returnList = platformService.queryPlatformCoins(id, example);
		
		if (returnList == null || returnList.isEmpty()) {
			return BaseResponse.success();
		}
		if (group == 2) {
			returnData = groupListByQuoteCurrency(returnList);
		} else if (group == 3) {
			returnData = groupListByCode(returnList);
		} else {
			returnData.put("list", returnList);
		}
		
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS, returnData);
	}
	
private Map<String, List<CoinExpand>> groupListByQuoteCurrency(List<CoinExpand> list){
		
		Map<String, List<CoinExpand>> result = new HashMap<String, List<CoinExpand>>();
		
		for (CoinExpand coin : list) {
			if (result.containsKey(coin.getQuoteCurrency())) {
				result.get(coin.getQuoteCurrency()).add(coin);
			} else {
				List<CoinExpand> newList = new ArrayList<CoinExpand>();
				newList.add(coin);
				result.put(coin.getQuoteCurrency(), newList);
			}
		}
		return result;
	}
	
	private Map<String, List<CoinExpand>> groupListByCode(List<CoinExpand> list){
		
		Map<String, List<CoinExpand>> result = new HashMap<String, List<CoinExpand>>();
		
		for (CoinExpand coin : list) {
			if (result.containsKey(coin.getCode())) {
				result.get(coin.getCode()).add(coin);
			} else {
				List<CoinExpand> newList = new ArrayList<CoinExpand>();
				newList.add(coin);
				result.put(coin.getCode(), newList);
			}
		}
		return result;
	}
	
	/**
	 * 查询交易所平台简介
	 * @param id
	 * @return
	 */
	@RequestMapping(path="/platform/info",method=RequestMethod.GET)
	public BaseResponse queryPlatRemark(String id) {
		
		if (StringUtils.isBlank(id)) {
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_MISSING);
		}
		
		Map<String, Object> returnData = new HashMap<String, Object>();
		returnData = PublicUtil.ConvertObjToMap(platformService.queryPlatformRemark(id));
		
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS,returnData);
	}
}
