package ksd.binews.mapper;

import ksd.binews.entity.User;

public interface UserMapper {
	
    User selectByPrimaryKey(String id);
    
    User selectByCondition(User user);
    
    int insertSelective(User user);
}