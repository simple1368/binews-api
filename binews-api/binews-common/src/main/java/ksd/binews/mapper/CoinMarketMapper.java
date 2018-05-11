package ksd.binews.mapper;

import java.util.List;
import java.util.Map;

import ksd.binews.entity.CoinMarket;

public interface CoinMarketMapper {
	
    int insertSelective(CoinMarket coinMarket);
    
    CoinMarket selectByPlatformCoinId(String platformCoinId);
    
    List<CoinMarket> selectListCondition(Map<String, Object> condition);
    
    int updateByPrimaryKeySelective(CoinMarket coinMarket);
    
    int insertBatch(List<CoinMarket> list);
    
    int updateBatch(List<CoinMarket> list);
}