package ksd.binews.mapper;

import ksd.binews.entity.LoggerInfo;

public interface LoggerInfoMapper {
    
	int insertSelective(LoggerInfo loggerInfo);
	
}