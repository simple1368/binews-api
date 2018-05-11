package ksd.binews.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ksd.binews.entity.User;
import ksd.binews.mapper.UserMapper;
import ksd.binews.utils.PublicUtil;

@Service
public class UserService {
	
	@Autowired
	private UserMapper userMapper;
	
	public User queryByPrimaryKey(String id) {
		return userMapper.selectByPrimaryKey(id);
	}

	public User queryByCondition(User user) {
		return userMapper.selectByCondition(user);
	}
	
	public Map<String, Object> register(User user) {
		if (StringUtils.isBlank(user.getName())) {
			user.setName(user.getMobile());
		}
		user.setId(PublicUtil.getUUID());
		user.setRegisterTime(PublicUtil.getCurrentTimestamp());
		user.setCreateTime(PublicUtil.getCurrentTimestamp());
		user.setDeletedFlag("n");
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		User param = new User();
		param.setMobile(user.getMobile());
		if (userMapper.selectByCondition(param) != null) {
			returnMap.put("code", -2);
			return returnMap;
		}
		
		int result = userMapper.insertSelective(user);
		
		returnMap.put("code", result);
		returnMap.put("id", user.getId());
		return returnMap;
	}
	
}
