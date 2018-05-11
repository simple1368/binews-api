package ksd.binews.mapper;

import java.util.List;

import ksd.binews.entity.NavCurrency;

public interface NavCurrencyMapper {
	
	int insertSelectiveBatch(List<NavCurrency> list);
	
	int updateByPrimaryKeySelective(NavCurrency navCurrency);
	
    
}