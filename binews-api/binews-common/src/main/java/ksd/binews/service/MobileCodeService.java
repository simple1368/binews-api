package ksd.binews.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ksd.binews.entity.MobileCode;
import ksd.binews.mapper.MobileCodeMapper;
import ksd.binews.utils.PublicUtil;

@Service
public class MobileCodeService {

	@Autowired
	private MobileCodeMapper mobileCodeMapper;
	
	public MobileCode queryMobileCode(String mobile){
		return mobileCodeMapper.selectByMobile(mobile);
	}
	
	public int insertSelective(MobileCode mobileCode) {
		mobileCode.setId(PublicUtil.getUUID());
		mobileCode.setCreateTime(PublicUtil.getCurrentTimestamp());
		mobileCode.setDeletedFlag("n");
		return mobileCodeMapper.insertSelective(mobileCode);
	}
	
}
