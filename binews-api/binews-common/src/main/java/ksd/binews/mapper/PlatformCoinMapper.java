package ksd.binews.mapper;

import java.util.List;
import java.util.Map;

import ksd.binews.entity.PlatformCoin;
import ksd.binews.entity.PlatformCoinExtends;

public interface PlatformCoinMapper {

    int insert(PlatformCoin record);
    
    int insertPlatformCoinBatch(List<PlatformCoin> list);
    
    List<PlatformCoinExtends> selectListByCondition(Map<String, Object> param); 

    PlatformCoin selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PlatformCoin record);
}