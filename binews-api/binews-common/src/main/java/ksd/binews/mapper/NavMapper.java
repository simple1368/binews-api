package ksd.binews.mapper;

import java.util.List;
import java.util.Map;

import ksd.binews.entity.Nav;
import ksd.binews.entity.NavExpand;

public interface NavMapper {
	
	Nav selectByPrimaryKey(String id);
	
	Nav selectDetailByIdFront(String id);
	
	Map<String, Object> selectNavCount();
	
	NavExpand selectDetailById(String id);
    
	List<Nav> selectList(NavExpand condition);
	
	int insertSelective(Nav nav);
	
	int updateByPrimaryKeySelective(Nav nav);
	
}