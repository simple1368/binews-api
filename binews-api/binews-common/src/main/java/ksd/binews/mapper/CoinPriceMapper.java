package ksd.binews.mapper;

import java.util.List;
import java.util.Map;

import ksd.binews.entity.CoinPrice;

public interface CoinPriceMapper {
	
	List<CoinPrice> selectByName(Map<String, Object> paramMap);
}