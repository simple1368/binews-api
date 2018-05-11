package ksd.binews.mapper;

import ksd.binews.entity.Vote;

public interface VoteMapper {
    
	Vote selectByCondition(Vote vote);
	
	Vote selectByPrimaryKey(String id);
	
	int insertSelective(Vote vote);
	
	int updateByPrimaryKeySelective(Vote vote);
}