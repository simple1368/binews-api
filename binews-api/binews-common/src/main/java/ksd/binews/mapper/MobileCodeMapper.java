package ksd.binews.mapper;

import ksd.binews.entity.MobileCode;

public interface MobileCodeMapper {
	
	MobileCode selectByMobile(String mobile);
	
	int insertSelective(MobileCode mobileCode);
}