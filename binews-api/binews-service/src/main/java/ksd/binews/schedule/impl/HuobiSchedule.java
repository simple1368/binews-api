package ksd.binews.schedule.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import ksd.binews.api.HuobiApi;
import ksd.binews.entity.Coin;
import ksd.binews.entity.CoinMarket;
import ksd.binews.entity.Platform;
import ksd.binews.entity.PlatformCoinExtends;
import ksd.binews.schedule.ITaskSchedule;
import ksd.binews.service.HuobiService;
import ksd.binews.service.PlatformService;
import ksd.binews.utils.PublicUtil;

@Component
@PropertySource("classpath:api.properties")
public class HuobiSchedule implements ITaskSchedule {

	private static final Logger logger = LoggerFactory.getLogger(HuobiSchedule.class);

	private final long intervel = 1000 * 60 * 60;
	
	private final int maxListSize = 10;

	@Value("${huobi.common.symbols.url}")
	private String symbolsUrl;

	@Value("${huobi.market.detail.url}")
	private String marketDetailUrl;

	@Value("${huobi.platform.id}")
	private String platformId;
	
	@Autowired
	private Executor executor;
	
	@Autowired
	private HuobiService huobiService;

	@Autowired
	private PlatformService platformService;

	@Override
	@Scheduled(fixedRate = intervel)
	public void listCoins() {
		Platform platform = platformService.queryByPrimaryKey(platformId);
		if (platform == null || platform.getDeletedFlag().toLowerCase().equals("y")) {
			return;
		}

		Timestamp ts = PublicUtil.getCurrentTimestamp();

		logger.info("==>HuobiSchedule.listCoins()\t" + ts + "\t开始执行");
		try {
			String jsonStr = HuobiApi.sendGet(symbolsUrl, null);
			
			JSONObject obj = JSONObject.parseObject(jsonStr);
			if (obj == null || !obj.get("status").toString().toLowerCase().equals("ok")) {
				logger.error("==>HuobiSchedule.listCoins()\t" + ts + "\t获取数据异常" + jsonStr);
				return;
			}

			ObjectMapper mapper = new ObjectMapper();
			List<Map<String, Object>> data = mapper.readValue(obj.getString("data"), List.class);

			if (huobiService.listCoins(data, platformId) < 0) {
				logger.error("==>HuobiSchedule.listCoins()\t" + ts + "\t添加币种失败");
				return;
			}
			logger.info("==>HuobiSchedule.listCoins()\t" + ts + "\t成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("==>HuobiSchedule.listCoins()\t" + ts + "\t" + e.getMessage());
		}
	}

	@Override
	@Scheduled(fixedRate = intervel)
	public void listMarket() {
		Platform platform = platformService.queryByPrimaryKey(platformId);
		if (platform == null || platform.getDeletedFlag().toLowerCase().equals("y")) {
			return;
		}
		
		Timestamp ts = PublicUtil.getCurrentTimestamp();
		try {
			//查询当前交易所的所有交易对
			List<PlatformCoinExtends> symbolList =  huobiService.queryAllSymbol(platformId);
			if (symbolList == null || symbolList.isEmpty()) {
				return;
			}
			logger.info("==>HuobiSchedule.listMarket()\t" + ts + "\t开始执行");
			//拆分list进行分批处理
			List<List<PlatformCoinExtends>> newList = splitList(symbolList, maxListSize);
			CountDownLatch latch = new CountDownLatch(newList.size());
			for (List<PlatformCoinExtends> list : newList) {
				//多线程执行
				executor.execute(new Thread() {
					public void run() {
						try {
							marketAsync(list, latch, ts);
						} catch (Exception e) {
							e.printStackTrace();
							logger.error("==>HuobiSchedule.listMarket()\t" + ts + "\t" + Thread.currentThread().getName() + "\t" + e.getMessage());
						}
					};
				});
			}
			latch.await();
			
			//更新当前交易所的24小时交易量
			int result = huobiService.updateTotalDay(platformId);
			if (result <= 0) {
				logger.info("==>HuobiSchedule.listMarket()\t" + ts + "\t更新交易所交易量失败");
			}
			logger.info("==>HuobiSchedule.listMarket()\t" + ts + "\t成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("==>HuobiSchedule.listMarket()\t" + ts + "\t" + e.getMessage());
		}
	}
	
	private List<List<PlatformCoinExtends>> splitList(List<PlatformCoinExtends> list, int len) {
		if (list == null || list.size() == 0 || len < 1) {
			return null;
		}

		List<List<PlatformCoinExtends>> result = new ArrayList<List<PlatformCoinExtends>>();

		int size = list.size();
		int count = (size + len - 1) / len;

		for (int i = 0; i < count; i++) {
			List<PlatformCoinExtends> subList = list.subList(i * len, ((i + 1) * len > size ? size : len * (i + 1)));
			result.add(subList);
		}
		return result;
	}

	private void marketAsync(List<PlatformCoinExtends> symbolList, CountDownLatch latch, Timestamp ts) throws Exception{
		if (symbolList == null || symbolList.isEmpty()) {
			latch.countDown();
			return;
		}
		logger.info("==>HuobiSchedule.listMarket()\t" + ts + "\t" + Thread.currentThread().getName() + "\t开始");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		List<Coin> coinList = new ArrayList<Coin>();
		List<CoinMarket> marketList = new ArrayList<CoinMarket>();
		JSONObject obj = null;
		Coin coin = null;
		JSONObject tick = null;
		CoinMarket coinMarket = null;
		for (PlatformCoinExtends pce : symbolList) {
			paraMap.clear();
			paraMap.put("symbol", pce.getBaseCurrency() + pce.getQuoteCurrency());
			
			String jsonStr = HuobiApi.sendGet(marketDetailUrl, paraMap);
			obj = JSONObject.parseObject(jsonStr);
			if (obj == null || !obj.get("status").toString().toLowerCase().equals("ok")) {
				logger.error("==>HuobiSchedule.listMarket()\t" + ts + "\t" + Thread.currentThread().getName() + "\t获取数据异常" + jsonStr);
				continue;
			}
			tick = obj.getJSONObject("tick");
			coin = new Coin();
			coin.setId(pce.getCoinId());
			coin.setPrice(tick.getDoubleValue("close"));
			coin.setDayRise(0f);
			coin.setDayTradeAmount(tick.getDoubleValue("amount"));
			coin.setUpdateTime(PublicUtil.getCurrentTimestamp());
			coinList.add(coin);
			
			coinMarket = new CoinMarket();
			coinMarket.setPlatformCoinId(pce.getId());
			coinMarket.setLastPrice(new BigDecimal(tick.getDoubleValue("close")));
			coinMarket.setFirstPrice(BigDecimal.ZERO);
			coinMarket.setTotalDay(new BigDecimal(tick.getDoubleValue("amount")));
			coinMarket.setFlotRange(new BigDecimal(0f));
			coinMarket.setMarketPrice(new BigDecimal(0));
			marketList.add(coinMarket);
		}
		if (huobiService.listMarket(coinList, marketList) <= 0) {
			logger.info("==>HuobiSchedule.listMarket()\t" + ts + "\t" + Thread.currentThread().getName() + "\t修改币种行情失败");
		}
		logger.info("==>HuobiSchedule.listMarket()\t" + ts + "\t" + Thread.currentThread().getName() + "\t执行完毕");
		latch.countDown();
	}
}
