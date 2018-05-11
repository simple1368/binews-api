package ksd.binews.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;

import ksd.binews.entity.ChangeRank;
import ksd.binews.entity.Coin;
import ksd.binews.entity.CoinExpand;
import ksd.binews.entity.Example;
import ksd.binews.entity.Platform;
import ksd.binews.entity.QuoteCurrency;
import ksd.binews.mapper.ChangeRankMapper;
import ksd.binews.mapper.CoinMapper;
import ksd.binews.mapper.PlatformMapper;
import ksd.binews.mapper.QuoteCurrencyMapper;

@Service
public class MarketService {

	@Autowired
	private PlatformMapper platformMapper;
	@Autowired
	private CoinMapper coinMapper;
	@Autowired
	private QuoteCurrencyMapper quoteCurrencyMapper;
	@Autowired
	private ChangeRankMapper changeRankMapper;
	@Value("${server.baseUrl}")
	private String baseUrl;

	/**
	 * 查询行情页banner
	 * 
	 * @return
	 */
	public Map<String, Object> queryBanner() {
		//币种数量
		int coinCount = coinMapper.selectCoinCount();
		//交易所数量
		int platformCount = platformMapper.selectPlatformCount();
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("list", coinMapper.selectCoinBanner());
		returnMap.put("coinCount", coinCount);
		returnMap.put("platformCount", platformCount);
		return returnMap;
	}

	/**
	 * 币种列表
	 * @return
	 */
	public Map<String, Object> queryCoinList(Example example) {
		
		if (StringUtils.isBlank(example.getLimit())) {
			example.setLimit(" 15 ");
		}
		if (StringUtils.isBlank(example.getOrder())) {
			example.setOrder(" desc ");
		}
		if (StringUtils.isBlank(example.getOrderBy())) {
			example.setOrderBy(" market_value ");
		}
		switch (example.getLimit()) {
			case "15":
				example.setLimit(" 15");break;
			case "30":
				example.setLimit(" 30");break;
			case "50":
				example.setLimit(" 50");break;
			default:
				example.setLimit(null);break;
		}
		
		switch (example.getOrder()) {
			case "up":
				example.setOrder(" asc ");break;
			case "down":
				example.setOrder(" desc ");break;
			default:
				example.setOrder(" desc ");break;
		}
		
		switch (example.getOrderBy()) {
			case "price":
				example.setOrderBy(" price ");break;
			case "dayRise":
				example.setOrderBy(" day_rise ");break;
			case "dayTradeAmount":
				example.setOrderBy(" day_trade_amount ");break;
			case "marketValue":
				example.setOrderBy(" market_value ");break;
			default:
				example.setOrderBy(" market_value ");break;
		}
		
		List<CoinExpand> coinList = coinMapper.selectCoinListCondition(example);
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("list", coinList);
		return returnMap;
	}

	/**
	 * 交易所列表
	 * 
	 * @return
	 */
	public List<Platform> queryPlatformList(Map<String, Object> condition) {
		List<Platform> list = platformMapper.selectPlatformListCondition(condition);
		
		for (Platform platform : list) {
			platform.setLogoUrl(StringUtils.isBlank(platform.getLogoUrl()) ? "" : baseUrl + platform.getLogoUrl());
		}
		
		return list;
	}
	
	public Map<String, Object> searchList(String keyword) {
		
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("keyword", keyword);
		paraMap.put("deletedFlag", "n");
		paraMap.put("order", " market_value desc");
		if (StringUtils.isBlank(keyword)) {
			paraMap.put("limit", 5);
		}
		List<Coin> coinList = coinMapper.selectCoinSearchList(paraMap);
		
		paraMap.clear();
		paraMap.put("keyword", keyword);
		paraMap.put("deletedFlag", "n");
		paraMap.put("order", " day_trade_amount desc");
		if (StringUtils.isBlank(keyword)) {
			paraMap.put("limit", 5);
		}
		List<Platform> platformList = platformMapper.selectPlatformSearchList(paraMap);
		
		paraMap.clear();
		paraMap.put("deletedFlag", "n");
		List<QuoteCurrency> quoteCurrencyList = quoteCurrencyMapper.selectListByCondition(paraMap);
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("coinList", coinList);
		returnMap.put("platformList", platformList);
		returnMap.put("quoteCurrencyList", quoteCurrencyList);
		
		return returnMap;
	}
	
	/**
	 * 币种排行榜
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public List<Coin> coinRankingPage(int size) {
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("limit", size);
		List<Coin> list = coinMapper.selectCoinRanking(paraMap);
		return list;
	}
	
	public List<ChangeRank> changeRanks(String time, int type) {
		Map<String, Object> condition = Maps.newHashMap();
		switch (time) {
			case "1h":
				condition.put("type", 1);
				break;
			case "24h":
				condition.put("type", 2);
				break;
			case "7d":
				condition.put("type", 3);
				break;
			default:
				condition.put("type", 2);
				break;
		}
		if (type == 1) {
			condition.put("order", " change_value desc ");
			condition.put("condition", " change_value >= 0 ");
		} else {
			condition.put("order", " change_value asc");
			condition.put("condition", " change_value <= 0 ");
		}
		condition.put("limit", " 20 ");
		return changeRankMapper.selectList(condition);
	}
	
	/**
	 * 交易所排行榜
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public List<Platform> platformRankingPage(int size) {
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("limit", size);
		List<Platform> list = platformMapper.selectPlatformRanking(paraMap);
		return list;
	}
}
