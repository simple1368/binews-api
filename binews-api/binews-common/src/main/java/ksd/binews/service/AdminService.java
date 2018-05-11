package ksd.binews.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ksd.binews.entity.Admin;
import ksd.binews.mapper.AdminMapper;

@Service
public class AdminService {

	@Autowired
	private AdminMapper adminMapper;
	
	/**
	 * 查询管理员
	 * @param admin
	 * @return
	 */
	public Admin queryAdmin(Admin admin){
		return adminMapper.selectAdmin(admin);
	}
}
