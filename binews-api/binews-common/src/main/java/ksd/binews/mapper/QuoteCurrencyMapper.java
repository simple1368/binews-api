package ksd.binews.mapper;

import java.util.List;
import java.util.Map;

import ksd.binews.entity.QuoteCurrency;

public interface QuoteCurrencyMapper {

    int insertSelective(QuoteCurrency record);
    
    int insertQuoteCurrencyBatch(List<QuoteCurrency> list);
    
    List<QuoteCurrency> selectListByCondition(Map<String, Object> param);

    QuoteCurrency selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(QuoteCurrency record);
}