package ksd.binews.mapper;

import ksd.binews.entity.Admin;

public interface AdminMapper {
	
    Admin selectAdmin(Admin admin);
    
    Admin selectByPrimaryKey(String id);
}