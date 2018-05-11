package ksd.binews.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.hazelcast.util.CollectionUtil;

import ksd.binews.dto.PriceDto;
import ksd.binews.entity.Admin;
import ksd.binews.entity.Coin;
import ksd.binews.entity.CoinExpand;
import ksd.binews.entity.Example;
import ksd.binews.entity.PlatCoin;
import ksd.binews.entity.price.PriceEntity;
import ksd.binews.mapper.CoinMapper;
import ksd.binews.mapper.PlatCoinMapper;
import ksd.binews.mapper.price.PriceMapper;
import ksd.binews.utils.PublicUtil;

@Service
public class CoinService {

	@Autowired
	private CoinMapper coinMapper;
	@Autowired
	private PlatCoinMapper platCoinMapper;
	@Autowired
	private PriceMapper priceMapper;	
	
	/**
	 * 查询币种信息
	 * @param id
	 * @return
	 */
	public Coin queryCoinDetail(String id,int type) {
		
		Coin coinInfo = coinMapper.selectCoinInfo(id);
		
		//查询行情价格
		/*Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("name", enName);
		condition.put("type", type);
		condition.put("today", new Date());
		List<CoinPrice> coinPrice = coinPriceMapper.selectByName(condition);
		
		coinInfo.setMarketTrend(coinPrice);*/
		return coinInfo;
	}
	
	public Coin queryByPrimaryKey(String id) {
		return coinMapper.selectByPrimaryKey(id);
	}
	
	public PageInfo listCoinPage(int pageNum,int pageSize,Map<String, Object> condition) {
		PageHelper.startPage(pageNum, pageSize);
		
		List<Coin> list = coinMapper.selectListByCondition(condition);
		PageInfo pageInfo = new PageInfo(list);
		return pageInfo;
	}
	
	/**
	 * 查询币种基本信息
	 * @param id
	 * @return
	 */
	public Coin queryCoinEssentialInfo(String id) {
		
		return coinMapper.selectCoinEssentialInfo(id);
	}
	
	/**
	 * 查询币种市场行情
	 * @param enName 币种名称
	 * @return
	 */
	public List<CoinExpand> queryCoinMarketQuotation(String id,Example example) throws Exception{
		
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("id", id);
		
		if (StringUtils.isBlank(example.getOrder())) {
			example.setOrder(" desc ");
		}
		if (StringUtils.isBlank(example.getOrderBy())) {
			example.setOrderBy(" day_trade_amount ");
		}
		switch (example.getOrderBy()) {
			case "dayTradeAmount":
				condition.put("orderBy", " day_trade_amount ");
				break;
			case "price":
				condition.put("orderBy", " price ");
				break;
			case "dayRise":
				condition.put("orderBy", " day_rise ");
				break;
			default:
				condition.put("orderBy", " day_trade_amount ");
				break;
		}
		switch (example.getOrder()) {
			case "up":
				condition.put("order", " asc ");
				break;
			case "down":
				condition.put("order", " desc ");
				break;
			default:
				condition.put("order", " desc ");
				break;
		}
		
		List<CoinExpand> list = coinMapper.selectMarketQuotation(condition); 
		long currentTimeLong = new Date().getTime();
		long mss = currentTimeLong;
		String time = "";
		for (CoinExpand coinExpand : list) {
			if (coinExpand == null) {
				continue;
			}
			if (coinExpand.getUpdateTime() != null) {
				mss = currentTimeLong - coinExpand.getUpdateTime().getTime();
			} else {
				mss = currentTimeLong - coinExpand.getCreateTime().getTime();
			}
			time = (mss / (1000 * 60)) + "分钟前";//分钟
			if ((mss / (1000 * 60)) >= 60) {
				time = (mss / (1000 * 60 * 60)) + "小时前";//小时
			}
			if ((mss / (1000 * 60 * 60)) >= 24) {
				time = (mss / (1000 * 60 * 60 * 24)) + "天前";//天
			}
			coinExpand.setRefreshTime(time);
			//不返回到前台
			coinExpand.setCreateTime(null);
			coinExpand.setUpdateTime(null);
		}
		
		return list;
	}

	/**
	 * 新增币种
	 * 
	 * @param coin
	 * @return
	 */
	@Transactional
	public Map<String, Object> insertCoin(Coin coin) {
		coin.setId(PublicUtil.getUUID());
		coin.setCreateTime(PublicUtil.getCurrentTimestamp());
		coin.setDeletedFlag("n");

		Map<String, Object> returnMap = new HashMap<String, Object>();
		//新增币种
		int result = coinMapper.insertSelective(coin);
		if (result <= 0) {
			throw new RuntimeException("插入币种信息失败");
		}
		//新增币种-交易所关系
		PlatCoin platCoin = new PlatCoin();
		platCoin.setId(PublicUtil.getUUID());
		platCoin.setCoinId(coin.getId());
		//platCoin.setPlatformId(coin.getPlatformId());
		platCoin.setCreateTime(PublicUtil.getCurrentTimestamp());
		platCoin.setDeletedFlag("n");
		result = platCoinMapper.insertSelective(platCoin);
		if (result <= 0) {
			throw new RuntimeException("新增币种交易所关联关系失败");
		}
		
		returnMap.put("code", result);
		returnMap.put("id", coin.getId());
		return returnMap;
	}

	/**
	 * 修改币种信息
	 * @param coin
	 * @return
	 */
	public int updateCoin(Coin coin,Admin admin) {
		// 查询是否存在
		Coin record = coinMapper.selectByPrimaryKey(coin.getId());
		if (record == null || record.getDeletedFlag().trim().toLowerCase().equalsIgnoreCase("y")) {
			return -2;
		}

		coin.setUpdateBy(admin.getId());
		coin.setUpdateTime(PublicUtil.getCurrentTimestamp());

		return coinMapper.updateByPrimaryKeySelective(coin);
	}
	
	public int showCoin(Coin coin, Admin admin) {
		// 查询是否存在
		Coin record = coinMapper.selectByPrimaryKey(coin.getId());
		if (record == null) {
			return -2;
		}

		coin.setUpdateBy(admin.getId());
		coin.setUpdateTime(PublicUtil.getCurrentTimestamp());

		return coinMapper.updateByPrimaryKeySelective(coin);
	}

	/**
	 * 删除币种
	 * 
	 * @param id
	 * @return
	 */
	public int deleteCoin(String id) {
		Coin coin = coinMapper.selectByPrimaryKey(id);
		// 判断是否已经删除
		if (coin == null || coin.getDeletedFlag().trim().toLowerCase().equalsIgnoreCase("y")) {
			return -2;
		}
		int result = -1;
		coin = new Coin();
		coin.setId(id);
		coin.setUpdateTime(PublicUtil.getCurrentTimestamp());

		result = coinMapper.deleteByPrimaryKey(coin);

		return result;
	}

	public Map<String, Object> queryChartData(String id) {
		Coin coin = coinMapper.selectByPrimaryKey(id);
		if(coin == null || StringUtils.isBlank(coin.getCode())){
			return null;
		}
		
		String symbol = coin.getCode().replace("-", "_");
		PriceDto dto = new PriceDto();
		dto.setTableName(symbol+"_price");
		dto.setSymbol(symbol);
		dto.setType(1);
		
		List<PriceEntity> priceUsdList;
		List<PriceEntity> priceBtcList;
		List<PriceEntity> marketList;
		List<PriceEntity> volumeList;
		try{
			priceUsdList = priceMapper.listPrice(dto);
			
			dto.setType(2);
			priceBtcList = priceMapper.listPrice(dto);
			
			dto.setType(3);
			marketList = priceMapper.listPrice(dto);
			
			dto.setType(4);
			volumeList = priceMapper.listPrice(dto);
		}catch(Exception ex){
			priceUsdList = null;
			priceBtcList = null;
			marketList = null;
			volumeList = null;
		}
		
		Map<String, Object> map = Maps.newHashMap();
		map.put("price_usd", formatChart(priceUsdList));
		map.put("price_btc", formatChart(priceBtcList));
		map.put("market_usd", formatChart(marketList));
		map.put("volume_usd", formatChart(volumeList));
		
		return map;
	}
	
	@SuppressWarnings("rawtypes")
	private List<ArrayList> formatChart(List<PriceEntity> list){
		if(CollectionUtil.isNotEmpty(list)){
			List<ArrayList> resultList = new ArrayList<ArrayList>();
			ArrayList<Number> arrayList;
			for (PriceEntity priceEntity : list) {
				arrayList = new ArrayList(2);
				arrayList.add(priceEntity.getmTime());
				arrayList.add(priceEntity.getPrice());
				resultList.add(arrayList);
			}
			
			return resultList;
		}
		
		return null;
	}

}
