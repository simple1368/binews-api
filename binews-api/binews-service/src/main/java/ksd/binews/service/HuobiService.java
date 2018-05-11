package ksd.binews.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ksd.binews.entity.Coin;
import ksd.binews.entity.CoinMarket;
import ksd.binews.entity.Platform;
import ksd.binews.entity.PlatformCoin;
import ksd.binews.entity.PlatformCoinExtends;
import ksd.binews.entity.QuoteCurrency;
import ksd.binews.mapper.CoinMapper;
import ksd.binews.mapper.CoinMarketMapper;
import ksd.binews.mapper.PlatformCoinMapper;
import ksd.binews.mapper.PlatformMapper;
import ksd.binews.mapper.QuoteCurrencyMapper;
import ksd.binews.utils.PublicUtil;

@Service
public class HuobiService {

	@Autowired
	private CoinMapper coinMapper;
	@Autowired
	private QuoteCurrencyMapper quoteCurrencyMapper;
	@Autowired
	private PlatformCoinMapper platformCoinMapper;
	@Autowired
	private PlatformMapper platformMapper;
	@Autowired
	private CoinMarketMapper coinMarketMapper;
	
	
	/**
	 * 币种
	 * 
	 * @param currencies
	 * @return
	 */
	@Transactional
	public int listCoins(List<Map<String, Object>> symbolList, String platformId) {
		if (symbolList.isEmpty()) {
			return -1;
		}
		//添加币种
		List<Map<String, Object>> coinList = new ArrayList<Map<String,Object>>();
		coinList.addAll(symbolList);
		List<Map<String, Object>> qcList = new ArrayList<Map<String,Object>>();
		qcList.addAll(symbolList);
		List<Map<String, Object>> pcList = new ArrayList<Map<String,Object>>();
		pcList.addAll(symbolList);
		
		int result = insertCoin(coinList);
		if (result < 0) {
			throw new RuntimeException("没有要新增的币种或新增失败");
		}
		//添加交易基准
		result = insertQuoteCurrency(qcList);
		if (result < 0) {
			throw new RuntimeException("没有要新增的交易基准或新增交易基准失败");
		}
		//添加交易所-币种-交易基准关系
		result = insertPlatformCoin(pcList, platformId);
		if (result < 0) {
			throw new RuntimeException("没有要新增的交易所-币种-交易基准关系或新增交易所-币种-交易基准关系失败");
		}
		
		//修改交易所数据
		int tradePairCount = symbolList.size();
		List<String> quoteList = new ArrayList<String>();
		List<String> baseList = new ArrayList<String>();
		for (Map<String, Object> map : symbolList) {
			if (!quoteList.contains(map.get("quote-currency").toString())) {
				quoteList.add(map.get("quote-currency").toString());
			}
			if (!baseList.contains(map.get("base-currency").toString())) {
				baseList.add(map.get("base-currency").toString());
			}
		}
		
		StringBuilder sb = new StringBuilder();
		for (String string : quoteList) {
			if (sb != null && StringUtils.isNotBlank(sb.toString())) {
				sb.append(",");
			}
			sb.append(string);
		}
		Platform platform = new Platform();
		platform.setId(platformId);
		platform.setTradeBase(sb == null ? "" : sb.toString());
		platform.setBitCount(baseList.size());
		platform.setTradePairCount(tradePairCount);
		
		result = platformMapper.updateByPrimaryKeySelective(platform);
		if (result <= 0) {
			throw new RuntimeException("更新交易所数据失败");
		}
		
		return result;
	}
	
	/**
	 * 行情
	 * @param platformId
	 * @return
	 */
	@Transactional
	public int listMarket(List<Coin> coinList, List<CoinMarket> marketList) {
		if (coinList == null || coinList.isEmpty() || marketList == null || marketList.isEmpty()) {
			return -1;
		}
		
		int result = coinMapper.updateCoinBatch(coinList);
		if (result <= 0) {
			throw new RuntimeException("修改币种信息失败");
		}
		
		List<CoinMarket> coinMarketList = coinMarketMapper.selectListCondition(null);
		
		List<CoinMarket> marketInsert = new ArrayList<CoinMarket>();
		List<CoinMarket> marketUpdate = new ArrayList<CoinMarket>();
		for (CoinMarket coinMarket : marketList) {
			boolean flag = false;
			String id = "";
			for (CoinMarket c : coinMarketList) {
				if (coinMarket.getPlatformCoinId().equals(c.getPlatformCoinId())) {
					flag = true;
					id = c.getId();
					break;
				}
			}
			if (flag) {
				coinMarket.setId(id);
				coinMarket.setUpdateTime(PublicUtil.getCurrentTimestamp());
				marketUpdate.add(coinMarket);
			} else {
				coinMarket.setId(PublicUtil.getUUID());
				coinMarket.setCreateTime(PublicUtil.getCurrentTimestamp());
				marketInsert.add(coinMarket);
			}
		}
		if (marketInsert != null && !marketInsert.isEmpty()) {
			result = coinMarketMapper.insertBatch(marketInsert);
			if (result <= 0) {
				throw new RuntimeException("添加币种行情失败");
			}
		}
		if (marketUpdate != null && !marketUpdate.isEmpty()) {
			result = coinMarketMapper.updateBatch(marketUpdate);
			if (result <= 0) {
				throw new RuntimeException("修改市场行情失败");
			}
		}
		
		return result;
	}
	
	/**
	 * 更新交易所的24小时交易量
	 * @param platformId
	 * @return
	 */
	public int updateTotalDay(String platformId){
		
		int result = platformMapper.updateTotalDay(platformId);
		
		return result;
	}
	
	/**
	 * 查询当前交易所的所有交易对
	 * @param platformId
	 * @return
	 */
	public List<PlatformCoinExtends> queryAllSymbol(String platformId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("platformId", platformId);
		paramMap.put("deletedFlag", "n");
		List<PlatformCoinExtends> pfcList = platformCoinMapper.selectListByCondition(paramMap);
		return pfcList;
	}

	/**
	 * 添加币种
	 * @param symbolList
	 * @return
	 */
	private int insertCoin(List<Map<String, Object>> symbolList) {
		// 查询已经存在的所有币种
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("deletedFlag", "n");
		List<Coin> coinList = coinMapper.selectListByCondition(paramMap);
		
		//去重
		List<String> newList = new ArrayList<String>();
		for (Map<String, Object> map : symbolList) {
			if (!newList.contains(map.get("base-currency").toString())) {
				newList.add(map.get("base-currency").toString());
			}
		}
		
		// 过滤已经存在的
		Iterator<String> it = newList.iterator();
		while (it.hasNext()) {
			String str = it.next();
			boolean flag = false;
			for (Coin coin : coinList) {
				if (str.equals(coin.getCode())) {
					flag = true;
					break;
				}
			}
			if (flag) {
				it.remove();
			}
		}
		
		if (newList == null || newList.isEmpty()) {
			return 0;
		}
		//添加币种
		List<Coin> list = new ArrayList<Coin>();
		Coin coin = new Coin();
		for (String str : newList) {
			coin = new Coin();
			coin.setId(PublicUtil.getUUID());
			coin.setCnName(str);
			coin.setEnName(str);
			coin.setCode(str);
			coin.setCreateTime(PublicUtil.getCurrentTimestamp());
			coin.setDeletedFlag("n");
			
			list.add(coin);
		}
		if (list == null || list.isEmpty()) {
			return 0;
		}
		int result = coinMapper.insertCoinBatch(list);
		
		return result;
	}
	
	/**
	 * 添加交易基准
	 * @param symbolList
	 * @return
	 */
	private int insertQuoteCurrency(List<Map<String, Object>> symbolList) {
		// 查询已经存在的所有交易基准
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("deletedFlag", "n");
		List<QuoteCurrency> quoteCurrencieList = quoteCurrencyMapper.selectListByCondition(paramMap);

		//去重
		List<String> newList = new ArrayList<String>();
		for (Map<String, Object> map : symbolList) {
			if (!newList.contains(map.get("quote-currency").toString())) {
				newList.add(map.get("quote-currency").toString());
			}
		}
		
		// 过滤已经存在的
		Iterator<String> it = newList.iterator();
		while (it.hasNext()) {
			String str = it.next();
			boolean flag = false;
			for (QuoteCurrency quoteCurrency : quoteCurrencieList) {
				if (str.equals(quoteCurrency.getCode())) {
					flag = true;
					break;
				}
			}
			if (flag) {
				it.remove();
			}
		}
		
		if (newList == null || newList.isEmpty()) {
			return 0;
		}
		//添加交易基准
		List<QuoteCurrency> list = new ArrayList<QuoteCurrency>();
		QuoteCurrency quoteCurrency = new QuoteCurrency();
		for (String str : newList) {
			quoteCurrency = new QuoteCurrency();
			quoteCurrency.setId(PublicUtil.getUUID());
			quoteCurrency.setCode(str);
			quoteCurrency.setCreateTime(PublicUtil.getCurrentTimestamp());
			quoteCurrency.setDeletedFlag("n");
			
			list.add(quoteCurrency);
		}
		if (list == null || list.isEmpty()) {
			return 0;
		}
		int result = quoteCurrencyMapper.insertQuoteCurrencyBatch(list);
		
		return result;
	}

	/**
	 * 添加币种-交易基准-交易所关系
	 * @param symbolList
	 * @param platformId
	 * @return
	 */
	private int insertPlatformCoin(List<Map<String, Object>> symbolList, String platformId){
		//查询当前交易所所有的交易对
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("platformId", platformId);
		paramMap.put("deletedFlag", "n");
		List<PlatformCoinExtends> pfcList = platformCoinMapper.selectListByCondition(paramMap);
		
		//过滤已经存在的
		Iterator<Map<String, Object>> it = symbolList.iterator();
		while (it.hasNext()) {
			Map<String, Object> map = (Map<String, Object>) it.next();
			boolean flag = false;
			for (PlatformCoinExtends pfc : pfcList) {
				if (pfc.getBaseCurrency().equals(map.get("base-currency"))
						&& pfc.getQuoteCurrency().equals(map.get("quote-currency"))) {
					flag = true;
					break;
				}
			}
			if (flag) {
				it.remove();
			}
		}
		if (symbolList == null || symbolList.isEmpty()) {
			return 0;
		}
		
		//查询所有币种
		paramMap.clear();
		paramMap.put("deletedFlag", "n");
		List<Coin> coinList = coinMapper.selectListByCondition(paramMap);
		Map<String, String> coinMap = coinList.stream().collect(
				Collectors.toMap(Coin::getId, Coin::getCode));
		
		//查询所有交易基准
		paramMap.clear();
		paramMap.put("deletedFlag", "n");
		List<QuoteCurrency> quoteCurrencieList = quoteCurrencyMapper.selectListByCondition(paramMap);
		Map<String, String> quoteCurrencieMap = quoteCurrencieList.stream().collect(
				Collectors.toMap(QuoteCurrency::getId, QuoteCurrency::getCode));
		
		//添加交易对关系
		List<PlatformCoin> list = new ArrayList<PlatformCoin>();
		PlatformCoin platformCoin = new PlatformCoin();
		for (Map<String, Object> map : symbolList) {
			platformCoin = new PlatformCoin();
			platformCoin.setId(PublicUtil.getUUID());
			platformCoin.setPlatformId(platformId);
			platformCoin.setCoinId(PublicUtil.getKey(coinMap, map.get("base-currency").toString()));
			platformCoin.setQuoteCurrencyId(PublicUtil.getKey(quoteCurrencieMap, map.get("quote-currency").toString()));
			platformCoin.setCreateTime(PublicUtil.getCurrentTimestamp());
			platformCoin.setDeletedFlag("n");
			
			list.add(platformCoin);
		}
		if (list == null || list.isEmpty()) {
			return 0;
		}
		int result = platformCoinMapper.insertPlatformCoinBatch(list);
		if (result <= 0) {
			throw new RuntimeException("添加币种-交易基准-交易所关系失败");
		}
		return result;
	}
}
