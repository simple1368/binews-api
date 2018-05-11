package ksd.binews.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import ksd.binews.entity.Admin;
import ksd.binews.entity.CoinExpand;
import ksd.binews.entity.Example;
import ksd.binews.entity.Platform;
import ksd.binews.mapper.CoinMapper;
import ksd.binews.mapper.PlatformMapper;
import ksd.binews.utils.PublicUtil;

/**
 * 交易所
 * @author Administrator
 *
 */
@Service
public class PlatformService {
	
	@Autowired
	private PlatformMapper platformMapper;
	@Autowired
	private CoinMapper coinMapper;
	@Value("${server.baseUrl}")
	private String baseUrl;

	
	public Platform queryByPrimaryKey(String id) {
		Platform platform = platformMapper.selectByPrimaryKey(id);
		platform.setLogoUrl(StringUtils.isBlank(platform.getLogoUrl()) ? "" : baseUrl + platform.getLogoUrl());
		return platform;
	}
	
	public PageInfo listPlatformPage(int pageNum, int pageSize,Map<String, Object> condition) {
		PageHelper.startPage(pageNum, pageSize);
		List<Platform> list = platformMapper.selectListCondition(condition);
		for (Platform platform : list) {
			platform.setLogoUrl(StringUtils.isBlank(platform.getLogoUrl()) ? "" : baseUrl + platform.getLogoUrl());
		}
		PageInfo pageInfo = new PageInfo(list);
		return pageInfo;
	}
	
	/**
	 * 查询交易所详情
	 * @param id
	 * @return
	 */
	public Platform queryPlatformDetail(String id) {
		return platformMapper.selectPlatformDetail(id);
	}
	
	/**
	 * 查询交易所数量
	 * @return
	 */
	public int selectPlatformCount() {
		return platformMapper.selectPlatformCount();
	}
	
	/**
	 * 查询交易所市场行情
	 * @param platformId
	 * @param 
	 * @return
	 */
	public List<CoinExpand> queryPlatformCoins(String platformId,Example example) {
		
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("platformId", platformId);
		
		if (StringUtils.isBlank(example.getOrder())) {
			example.setOrder(" desc ");
		}
		if (StringUtils.isBlank(example.getOrderBy())) {
			example.setOrderBy(" day_trade_amount ");
		}
		switch (example.getOrderBy()) {
			case "dayTradeAmount":
				condition.put("orderBy", " day_trade_amount ");
				break;
			case "price":
				condition.put("orderBy", " price ");
				break;
			case "dayRise":
				condition.put("orderBy", " day_rise ");
				break;
			default:
				condition.put("orderBy", " day_trade_amount ");
				break;
		}
		switch (example.getOrder()) {
			case "up":
				condition.put("order", " asc ");
				break;
			case "down":
				condition.put("order", " desc ");
				break;
			default:
				condition.put("order", " desc ");
				break;
		}
		List<CoinExpand> coinList = coinMapper.selectPlatCoin(condition);
		
		long currentTimeLong = new Date().getTime();
		long mss = currentTimeLong;
		String time = "";
		for (CoinExpand coinExpand : coinList) {
			if (coinExpand == null) {
				continue;
			}
			if (coinExpand.getUpdateTime() != null) {
				mss = currentTimeLong - coinExpand.getUpdateTime().getTime();
			} else {
				mss = currentTimeLong - coinExpand.getCreateTime().getTime();
			}
			time = (mss / (1000 * 60)) + "分钟前";//分钟
			if ((mss / (1000 * 60)) >= 60) {
				time = (mss / (1000 * 60 * 60)) + "小时前";//小时
			}
			if ((mss / (1000 * 60 * 60)) >= 24) {
				time = (mss / (1000 * 60 * 60 * 24)) + "天前";//天
			}
			coinExpand.setRefreshTime(time);
			coinExpand.setCreateTime(null);
			coinExpand.setUpdateTime(null);
		}
		return coinList;
	}
	
	/**
	 * 查询交易所平台简介
	 * @param id
	 * @return
	 */
	public Platform queryPlatformRemark(String id) {
		return platformMapper.selectPlatformRemark(id);
	}
	
	/**
	 * 新增交易所
	 * @param platform
	 * @return
	 */
	public Map<String, Object> insertFlatform(Platform platform) {
		platform.setId(PublicUtil.getUUID());
		platform.setCreateTime(PublicUtil.getCurrentTimestamp());
		platform.setDeletedFlag("n");
		
		int result = platformMapper.insertSelective(platform);
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("code", result);
		returnMap.put("id",platform.getId());
		return returnMap;
	}
	
	/**
	 * 修改交易所信息
	 * @param platform
	 * @return
	 */
	public int updatePlatform(Platform platform,Admin admin) {
		//查询是否存在
		Platform record = platformMapper.selectByPrimaryKey(platform.getId());
		if (record == null) {
			return -2;
		}
		
		platform.setUpdateBy(admin.getId());
		platform.setUpdateTime(PublicUtil.getCurrentTimestamp());
		
		return platformMapper.updateByPrimaryKeySelective(platform);
	}
	
	/**
	 * 删除交易所
	 * @param id
	 * @return
	 */
	public int deletePlatform(String id) {
		Platform platform = platformMapper.selectByPrimaryKey(id);
		//判断是否已经删除
		if (platform == null || platform.getDeletedFlag().trim().toLowerCase().equalsIgnoreCase("y")) {
			return -2;
		}
		int result = -1;
		platform = new Platform();
		platform.setId(id);
		platform.setUpdateTime(PublicUtil.getCurrentTimestamp());
		
		result = platformMapper.deleteByPrimaryKey(platform);
		
		return result;
	}
}
