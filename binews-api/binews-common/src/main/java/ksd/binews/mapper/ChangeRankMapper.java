package ksd.binews.mapper;

import java.util.List;
import java.util.Map;

import ksd.binews.entity.ChangeRank;

public interface ChangeRankMapper {
	
	List<ChangeRank> selectList(Map<String, Object> condition);
}