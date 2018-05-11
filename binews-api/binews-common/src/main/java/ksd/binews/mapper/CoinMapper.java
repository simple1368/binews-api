package ksd.binews.mapper;

import java.util.List;
import java.util.Map;

import ksd.binews.entity.Coin;
import ksd.binews.entity.CoinExpand;
import ksd.binews.entity.Example;

public interface CoinMapper {

	Coin selectByPrimaryKey(String id);
	
	List<Coin> selectListByCondition(Map<String, Object> paraMap);
	
	List<Coin> selectCoinRanking(Map<String, Object> paraMap);
	
	List<Coin> selectCoinSearchList(Map<String, Object> paraMap);
	
	List<CoinExpand> selectPlatCoin(Map<String, Object> paraMap);
	
	Coin selectCoinEssentialInfo(String id);
	
	int insertSelective(Coin coin);
	
	int insertCoinBatch(List<Coin> list);
	
	int selectCoinCount();
	
	Coin selectCoinInfo(String id);
	
	List<CoinExpand> selectMarketQuotation(Map<String, Object> paramMap);
	
	List<CoinExpand> selectCoinBanner();
	
	List<CoinExpand> selectCoinListCondition(Example example);
	
	int updateByPrimaryKeySelective(Coin coin);
	
	int updateCoinBatch(List<Coin> coins);
	
	int deleteByPrimaryKey(Coin coin);
}