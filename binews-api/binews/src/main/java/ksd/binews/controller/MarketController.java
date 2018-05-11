package ksd.binews.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ksd.binews.constants.MsgConstants;
import ksd.binews.dto.BaseResponse;
import ksd.binews.entity.Example;
import ksd.binews.entity.Rates;
import ksd.binews.service.MarketService;
import ksd.binews.service.RatesService;

/**
 * 行情
 * @author Administrator
 *
 */
@RestController
public class MarketController {
	
	private static final Logger log = LoggerFactory.getLogger(MarketController.class);
	
	@Autowired
	private MarketService marketService;
	@Autowired
	private RatesService ratesService;

	/**
	 * 行情页banner
	 * @return
	 */
	@RequestMapping(path="/banner",method=RequestMethod.GET)
	public BaseResponse banner() {
		
		Map<String, Object> returnData = new HashMap<String, Object>();
		returnData = marketService.queryBanner();
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS,returnData);
	}
	
	/**
	 * 行情页数据
	 * @return
	 */
	@RequestMapping(path="/market/{type}",method=RequestMethod.GET)
	public BaseResponse market(@RequestParam(value="order",required=false) String order,
			@RequestParam(value="orderBy",required=false) String orderBy,
			@RequestParam(value="limit",required=false) String limit,
			@RequestParam(value="supportCurrency",required=false) String supportCurrency,
			@RequestParam(value="tradeBase",required=false) String tradeBase,
			@RequestParam(value="tradeType",required=false) String tradeType,
			@PathVariable(value="type") int type) {
		
		if (type == 1) {
			//查询币种列表
			Example example = new Example();
			example.setLimit(limit);
			example.setOrder(order);
			example.setOrderBy(orderBy);
			
			return BaseResponse.success(marketService.queryCoinList(example));
		} else {
			//查询交易所列表
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("supportCurrency", supportCurrency);
			condition.put("tradeBase", tradeBase);
			condition.put("tradeType", tradeType);
			condition.put("limit", 100);
			
			Map<String, Object> returnData = new HashMap<String, Object>();
			returnData.put("list", marketService.queryPlatformList(condition));
			return BaseResponse.success(returnData);
		}
	}
	
	@RequestMapping(path="/search",method=RequestMethod.GET)
	public BaseResponse supportList(@RequestParam(value="keyword",required=false) String keyword) {
		
		Map<String, Object> returnData = new HashMap<String, Object>();
		returnData = marketService.searchList(keyword);
		
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS, returnData);
	}
	
	/**
	 * 排行榜
	 * @param type
	 * @param time
	 * @return
	 */
	@RequestMapping(path="/ranking/{type}",method=RequestMethod.GET)
	public BaseResponse rankingMarket(@PathVariable(value="type") String type,
			@RequestParam(value="time",required=false) String time) {
		
		Map<String, Object> returnData = new HashMap<String, Object>();
		
		if (StringUtils.isBlank(time) || (!time.equals("1h") 
				&& !time.equals("24h") && !time.equals("7d"))) {
			time = "24h";
		}
		
		if (type.equals("rise")) {
			//涨幅榜
			returnData.put("list", marketService.changeRanks(time, 1));
		}
		if (type.equals("fall")) {
			//跌幅榜
			returnData.put("list", marketService.changeRanks(time, 2));
		}
		if (type.equals("coin")) {
			//币种排行
			returnData.put("list", marketService.coinRankingPage(10));
		}
		if (type.equals("exchange")) {
			//交易所
			returnData.put("list", marketService.platformRankingPage(10));
		}
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS, returnData);
	}
	
	@RequestMapping(path="/rates",method=RequestMethod.GET)
	public BaseResponse rates(){
		
		List<Rates> list = ratesService.findAll();
		
		Map<String, Object> returnData = new HashMap<String, Object>();
		returnData.put("list", list);
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS, returnData);
	}
}
