package ksd.binews.mapper;

import java.util.List;
import java.util.Map;

import ksd.binews.entity.DigitCurrency;

public interface DigitCurrencyMapper {
	
	List<DigitCurrency> selectList(Map<String, Object> paraMap);
	
	int insertBatch(List<DigitCurrency> list);
	
	List<DigitCurrency> selectNavCurrency(String id);
    
}