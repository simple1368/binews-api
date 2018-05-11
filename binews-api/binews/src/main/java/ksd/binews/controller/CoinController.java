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
import ksd.binews.service.CoinService;
import ksd.binews.utils.PublicUtil;

/**
 * 币种
 * 
 * @author Administrator
 *
 */
@RestController
public class CoinController {

	private static final Logger logger = LoggerFactory.getLogger(CoinController.class);

	@Autowired
	private CoinService coinService;

	/**
	 * 查询币种详情
	 * @param id
	 * @param type 1：当天  2：近7天  3：一个月  4:三个月  5：一年
	 * @return
	 */
	@RequestMapping(path="/coin",method=RequestMethod.GET)
	public BaseResponse queryCoinDetail(String id,
			@RequestParam(value="type",defaultValue="1") int type) {
		
		if (StringUtils.isBlank(id)) {
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_MISSING);
		}
		
		Map<String, Object> returnData = new HashMap<String, Object>();
		returnData = PublicUtil.ConvertObjToMap(coinService.queryCoinDetail(id, type));
		
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS, returnData);
	}
	
	/**
	 * 查询币种的市场行情
	 * @param enName
	 * @param 
	 * @return
	 */
	@RequestMapping(path="/coin/mkt",method=RequestMethod.GET)
	public BaseResponse queryMarketQuotation(
			@RequestParam(value="id",required=false) String id,
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
		try {
			returnList = coinService.queryCoinMarketQuotation(id, example);
			
			if (returnList == null || returnList.isEmpty()) {
				return BaseResponse.success();
			}
			if (group == 2) {
				returnData = groupListByQuoteCurrency(returnList);
			} else if (group == 3) {
				returnData = groupListByPlatform(returnList);
			} else {
				returnData.put("list", returnList);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return BaseResponse.error(MsgConstants.MSG_OPERATION_FAIL);
		}
		
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS,returnData);
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
	
	private Map<String, List<CoinExpand>> groupListByPlatform(List<CoinExpand> list){
		
		Map<String, List<CoinExpand>> result = new HashMap<String, List<CoinExpand>>();
		
		for (CoinExpand coin : list) {
			if (result.containsKey(coin.getpName())) {
				result.get(coin.getpName()).add(coin);
			} else {
				List<CoinExpand> newList = new ArrayList<CoinExpand>();
				newList.add(coin);
				result.put(coin.getpName(), newList);
			}
		}
		return result;
	}
	
	/**
	 * 查询币种基本信息
	 * @param id
	 * @return
	 */
	@RequestMapping(path="/coin/info",method=RequestMethod.GET)
	public BaseResponse queryCoinInfo(String id) {
		
		if (StringUtils.isBlank(id)) {
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_MISSING);
		}
		
		Map<String, Object> returnData = new HashMap<String, Object>();
		returnData = PublicUtil.ConvertObjToMap(coinService.queryCoinEssentialInfo(id));
		
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS, returnData);
	}
	
	@RequestMapping(path="/coin/chart",method=RequestMethod.GET)
	public BaseResponse queryChartData(String id){
		if (StringUtils.isBlank(id)) {
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_MISSING);
		}
		
		Map<String, Object> returnData = coinService.queryChartData(id);
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS, returnData);
	}
}
