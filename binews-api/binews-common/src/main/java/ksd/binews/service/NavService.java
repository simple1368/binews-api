package ksd.binews.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import ksd.binews.entity.Admin;
import ksd.binews.entity.DigitCurrency;
import ksd.binews.entity.Nav;
import ksd.binews.entity.NavCurrency;
import ksd.binews.entity.NavExpand;
import ksd.binews.mapper.DigitCurrencyMapper;
import ksd.binews.mapper.NavCurrencyMapper;
import ksd.binews.mapper.NavMapper;
import ksd.binews.utils.PublicUtil;

@Service
public class NavService {

	@Autowired
	private NavMapper navMapper;
	@Autowired
	private NavCurrencyMapper navCurrencyMapper;
	@Autowired
	private DigitCurrencyMapper digitCurrencyMapper;
	@Value("${server.baseUrl}")
	private String baseUrl;
	
	/**
	 * 前台查询详情
	 * @param id
	 * @return
	 */
	public Nav selectDetailByIdFront(String id) {
		Nav nav = navMapper.selectDetailByIdFront(id);
		if (nav != null && nav.getLogoUrl() != null) {
			nav.setLogoUrl(StringUtils.isBlank(nav.getLogoUrl()) ? "" : baseUrl + nav.getLogoUrl());
		}
		return nav;
	}
	
	/**
	 * 后台查询详情
	 * @param id
	 * @return
	 */
	public NavExpand selectDetailById(String id) {
		NavExpand nav = navMapper.selectDetailById(id);
		if (nav != null && nav.getLogoUrl() != null) {
			nav.setLogoUrl(StringUtils.isBlank(nav.getLogoUrl()) ? "" : baseUrl + nav.getLogoUrl());
		}
		return nav;
	}
	
	/**
	 * 查询数量
	 * @return
	 */
	public Map<String, Object> count() {
		return navMapper.selectNavCount();
	}

	/**
	 * 前台查询列表
	 * @param condition
	 * @return
	 */
	public List<Nav> getList(NavExpand condition) {
		List<Nav> list = navMapper.selectList(condition);
		for (Nav nav : list) {
			if (nav != null && nav.getLogoUrl() != null) {
				nav.setLogoUrl(StringUtils.isBlank(nav.getLogoUrl()) ? "" : baseUrl + nav.getLogoUrl());
			}
		}
		return list;
	}

	/**
	 * 后台查询列表分页
	 * @param pageNum
	 * @param pageSize
	 * @param condition
	 * @return
	 */
	public PageInfo getListPage(int pageNum, int pageSize, NavExpand condition) {
		PageHelper.startPage(pageNum,pageSize);
		List<Nav> list = navMapper.selectList(condition);
		for (Nav nav : list) {
			if (nav != null && nav.getLogoUrl() != null) {
				nav.setLogoUrl(StringUtils.isBlank(nav.getLogoUrl()) ? "" : baseUrl + nav.getLogoUrl());
			}
		}
		PageInfo pageInfo = new PageInfo(list);
		return pageInfo;
	}
	
	/**
	 * 添加
	 * @param nav
	 * @param admin
	 * @return
	 */
	@Transactional
	public int addNav(NavExpand nav,Admin admin) {
		nav.setId(PublicUtil.getUUID());
		nav.setCreateBy(admin.getId());
		nav.setCreateTime(PublicUtil.getCurrentTimestamp());
		nav.setDeletedFlag("n");
		if (!StringUtils.isBlank(nav.getCurrency())) {
			nav.setSupportNum(nav.getCurrency().split(",").length);
		}
		
		int result = navMapper.insertSelective(nav);
		if (result <= 0) {
			throw new RuntimeException("添加导航记录失败");
		}
		
		if (!StringUtils.isBlank(nav.getCurrency())) {
			//添加货币关系
			String[] currencyArr = nav.getCurrency().split(",");
			List<NavCurrency> list = new ArrayList<NavCurrency>();
			
			NavCurrency navCurrency = new NavCurrency();
			
			for (String string : currencyArr) {
				navCurrency = new NavCurrency();
				navCurrency.setNavId(nav.getId());
				navCurrency.setCreateBy(admin.getId());
				navCurrency.setCreateTime(PublicUtil.getCurrentTimestamp());
				navCurrency.setDeletedFlag("n");
				navCurrency.setId(PublicUtil.getUUID());
				navCurrency.setDigitCurrencyId(string);
				list.add(navCurrency);
			}
			
			result = navCurrencyMapper.insertSelectiveBatch(list);
			if (result <= 0) {
				throw new RuntimeException("添加导航-数字货币关系失败");
			}
		}
		return result;
	}
	
	/**
	 * 修改
	 * @param nav
	 * @param admin
	 * @return
	 */
	@Transactional
	public int updateNav(NavExpand nav,Admin admin) {
		if (nav.getId() == null) {
			return -1;
		}
		nav.setUpdateBy(admin.getId());
		nav.setUpdateTime(PublicUtil.getCurrentTimestamp());
		if (!StringUtils.isBlank(nav.getCurrency())) {
			nav.setSupportNum(nav.getCurrency().split(",").length);
		}
		
		int result = navMapper.updateByPrimaryKeySelective(nav);
		if (result <= 0) {
			throw new RuntimeException("修改导航记录失败");
		}
		
		if (!StringUtils.isBlank(nav.getCurrency())) {
			//删除
			NavCurrency navCurrency = new NavCurrency();
			navCurrency.setNavId(nav.getId());
			navCurrency.setUpdateBy(admin.getId());
			navCurrency.setUpdateTime(PublicUtil.getCurrentTimestamp());
			navCurrency.setDeletedFlag("y");
			result = navCurrencyMapper.updateByPrimaryKeySelective(navCurrency);
			
			//重新添加
			String[] currencyArr = nav.getCurrency().split(",");
			List<NavCurrency> list = new ArrayList<NavCurrency>();
			
			for (String string : currencyArr) {
				navCurrency = new NavCurrency();
				navCurrency.setNavId(nav.getId());
				navCurrency.setCreateBy(admin.getId());
				navCurrency.setCreateTime(PublicUtil.getCurrentTimestamp());
				navCurrency.setDeletedFlag("n");
				navCurrency.setId(PublicUtil.getUUID());
				navCurrency.setDigitCurrencyId(string);
				list.add(navCurrency);
			}
			
			result = navCurrencyMapper.insertSelectiveBatch(list);
			if (result <= 0) {
				throw new RuntimeException("添加导航-数字货币关系失败");
			}
		}
		return result;
	}
	
	/**
	 * 删除
	 * @param id
	 * @param admin
	 * @return
	 */
	@Transactional
	public int deleteNav(String id, Admin admin) {
		if (StringUtils.isBlank(id)) {
			return -1;
		}
		
		Nav record = navMapper.selectByPrimaryKey(id);
		if (record == null || record.getDeletedFlag().toString().toLowerCase().equals("y")) {
			return -2;
		}
		
		Nav nav = new Nav();
		nav.setId(id);
		nav.setUpdateBy(admin.getId());
		nav.setUpdateTime(PublicUtil.getCurrentTimestamp());
		nav.setDeletedFlag("y");
		int result = navMapper.updateByPrimaryKeySelective(nav);
		if (result <= 0) {
			throw new RuntimeException("删除导航失败");
		}
		//删除导航关联币种
		NavCurrency navCurrency = new NavCurrency();
		navCurrency.setNavId(nav.getId());
		navCurrency.setUpdateBy(admin.getId());
		navCurrency.setUpdateTime(PublicUtil.getCurrentTimestamp());
		navCurrency.setDeletedFlag("y");
		result = navCurrencyMapper.updateByPrimaryKeySelective(navCurrency);
		if (result <= 0) {
			throw new RuntimeException("解除导航-数字货币关系失败");
		}
		return result;
	}
	
	public List<DigitCurrency> digitList() {
		List<DigitCurrency> list = digitCurrencyMapper.selectList(null);
		return list;
	}
	
	public List<DigitCurrency> digitTopList() {
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("limit", " 0,11 ");
		List<DigitCurrency> list = digitCurrencyMapper.selectList(paraMap);
		return list;
	}
	
	public Map<String, List<DigitCurrency>> digitListFront() {
		Map<String, List<DigitCurrency>> returnMap = new HashMap<String, List<DigitCurrency>>();
		returnMap.put("abList", new ArrayList<DigitCurrency>());
		returnMap.put("cdefList", new ArrayList<DigitCurrency>());
		returnMap.put("ghijList", new ArrayList<DigitCurrency>());
		returnMap.put("klmnList", new ArrayList<DigitCurrency>());
		returnMap.put("opqrList", new ArrayList<DigitCurrency>());
		returnMap.put("stuvList", new ArrayList<DigitCurrency>());
		returnMap.put("wxyzList", new ArrayList<DigitCurrency>());
		
		List<DigitCurrency> list = digitCurrencyMapper.selectList(null);
		for (DigitCurrency digitCurrency : list) {
			String str = digitCurrency.getCode().toLowerCase();
			if (StringUtils.isBlank(str)) {
				continue;
			}
			if (str.startsWith("a",0) || str.startsWith("b",0)) {
				returnMap.get("abList").add(digitCurrency);
			}
			if (str.startsWith("c",0) || str.startsWith("d",0) || str.startsWith("e",0) || str.startsWith("f",0)) {
				returnMap.get("cdefList").add(digitCurrency);
			}
			if (str.startsWith("g",0) || str.startsWith("h",0) || str.startsWith("i",0) || str.startsWith("j",0)) {
				returnMap.get("ghijList").add(digitCurrency);
			}
			if (str.startsWith("k",0) || str.startsWith("l",0) || str.startsWith("m",0) || str.startsWith("n",0)) {
				returnMap.get("klmnList").add(digitCurrency);
			}
			if (str.startsWith("o",0) || str.startsWith("p",0) || str.startsWith("q",0) || str.startsWith("r",0)) {
				returnMap.get("opqrList").add(digitCurrency);
			}
			if (str.startsWith("s",0) || str.startsWith("t",0) || str.startsWith("u",0) || str.startsWith("v",0)) {
				returnMap.get("stuvList").add(digitCurrency);
			}
			if (str.startsWith("w",0) || str.startsWith("x",0) || str.startsWith("y",0) || str.startsWith("z",0)) {
				returnMap.get("wxyzList").add(digitCurrency);
			}
		}
		return returnMap;
	}
	
	public List<DigitCurrency> currency(String id) {
		List<DigitCurrency> list = digitCurrencyMapper.selectNavCurrency(id);
		return list;
	}
	
	public int insertDigit(String codes) {
		
		if (StringUtils.isBlank(codes)) {
			return -1;
		}
		
		String[] codeArr = codes.split(",");
		List<DigitCurrency> list = new ArrayList<DigitCurrency>();
		DigitCurrency digitCurrency = null;
		for (String str : codeArr) {
			digitCurrency = new DigitCurrency();
			digitCurrency.setId(PublicUtil.getUUID());
			digitCurrency.setCode(str);
			digitCurrency.setCreateBy("-1");
			digitCurrency.setCreateTime(PublicUtil.getCurrentTimestamp());
			digitCurrency.setDeletedFlag("n");
			
			list.add(digitCurrency);
		}
		
		return digitCurrencyMapper.insertBatch(list);
	}
}
