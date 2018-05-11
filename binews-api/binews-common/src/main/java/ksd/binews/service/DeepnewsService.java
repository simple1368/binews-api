package ksd.binews.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import ksd.binews.entity.Admin;
import ksd.binews.entity.Deepnews;
import ksd.binews.mapper.DeepnewsMapper;
import ksd.binews.utils.PublicUtil;

@Service
public class DeepnewsService {
	
	private static final Logger logger = LoggerFactory.getLogger(DeepnewsService.class);

	@Autowired
	private DeepnewsMapper deepnewsMapper;
	@Value("${server.baseUrl}")
	private String baseUrl;
	
	/**
	 * 查询深度资讯列表(前台)
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryDeepnewsPageFront(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum,pageSize);
		
		List<Deepnews> list = deepnewsMapper.selectDeepnewsListFront();
		for (Deepnews deepnews : list) {
			if (deepnews == null) {
				continue;
			}
			deepnews.setPicUrl(StringUtils.isBlank(deepnews.getPicUrl()) ? "" : baseUrl + deepnews.getPicUrl());
			deepnews.setLogoUrl(StringUtils.isBlank(deepnews.getLogoUrl()) ? "" : baseUrl + deepnews.getLogoUrl());
		}
		PageInfo pageInfo = new PageInfo(list);
		return pageInfo;
	}
	
	/**
	 * 查询深度资讯列表（后台）
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryDeepnewsPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum,pageSize);
		
		List<Deepnews> list = deepnewsMapper.selectDeepnewsList();
		for (Deepnews deepnews : list) {
			if (deepnews == null) {
				continue;
			}
			deepnews.setPicUrl(StringUtils.isBlank(deepnews.getPicUrl()) ? "" : baseUrl + deepnews.getPicUrl());
			deepnews.setLogoUrl(StringUtils.isBlank(deepnews.getLogoUrl()) ? "" : baseUrl + deepnews.getLogoUrl());
		}
		
		PageInfo pageInfo = new PageInfo(list);
		return pageInfo;
	}
	
	/**
	 * 查询深度资讯详情（前台）
	 * @param id
	 * @return
	 */
	public Deepnews queryDeepnewsDetailFront(String id) {
		
		Deepnews deepnews = deepnewsMapper.selectDeepnewsDetail(id);
		
		if (deepnews != null) {
			deepnews.setLogoUrl(StringUtils.isBlank(deepnews.getLogoUrl()) ? "" : baseUrl + deepnews.getLogoUrl());
			
			//查询下一篇
			Map<String, Object> paramMap = new HashMap<String, Object>(16);
			paramMap.put("rank", deepnews.getRank() + "");
			paramMap.put("id", id);
			Deepnews next = deepnewsMapper.selectDeepnewsNext(paramMap);
			if (next != null) {
				
				next.setPicUrl(StringUtils.isBlank(next.getPicUrl()) ? "" : baseUrl + next.getPicUrl());
				
				deepnews.setNext(next);
			}
		}
		
		//添加浏览量
		int result = deepnewsMapper.updateBrowserCount(id);
		if (result <= 0) {
			logger.info("添加浏览量失败,id：" + id);
		}
		//为了不返回到前台
		deepnews.setRank(null);
		if (deepnews != null && deepnews.getNext() != null) {
			deepnews.getNext().setRank(null);
		}
		
		return deepnews;
	}
	
	/**
	 * 查询深度资讯详情（后台）
	 * @param id
	 * @return
	 */
	public Deepnews queryDeepnewsDetail(String id) {
		
		Deepnews deepnews = deepnewsMapper.selectByPrimaryKey(id);
		if (deepnews != null) {
			deepnews.setPicUrl(StringUtils.isBlank(deepnews.getPicUrl()) ? "" : baseUrl + deepnews.getPicUrl());
			deepnews.setLogoUrl(StringUtils.isBlank(deepnews.getLogoUrl()) ? "" : baseUrl + deepnews.getLogoUrl());
		}
		return deepnews; 
	}
	
	/**
	 * 添加深度资讯
	 * @param deepnews
	 * @param admin
	 * @return
	 */
	public int insertDeepnews(Deepnews deepnews,Admin admin) {
		deepnews.setId(PublicUtil.getUUID());
		deepnews.setCreateBy(admin.getId());
		deepnews.setCreateTime(PublicUtil.getCurrentTimestamp());
		deepnews.setUpdateBy(admin.getId());
		deepnews.setUpdateTime(PublicUtil.getCurrentTimestamp());
		deepnews.setDeletedFlag("n");
		
		int result = deepnewsMapper.insertSelective(deepnews);
		
		return result;
	}
	
	/**
	 * 更新深度资讯
	 * @param deepnews
	 * @param admin
	 * @return
	 */
	public int updateDeepnews(Deepnews deepnews,Admin admin) {
		
		if (StringUtils.isBlank(deepnews.getId())) {
			return -1;
		}
		deepnews.setUpdateBy(admin.getId());
		deepnews.setUpdateTime(PublicUtil.getCurrentTimestamp());
		
		int result = deepnewsMapper.updateByPrimaryKeySelective(deepnews);
		
		return result;
	}
	
	/**
	 * 删除深度资讯
	 * @param id
	 * @return
	 */
	public int deleteDeepnews(String id) {
		
		if (StringUtils.isBlank(id)) {
			return -1;
		}
		
		Deepnews deepnews = deepnewsMapper.selectByPrimaryKey(id);
		if (deepnews == null || deepnews.getDeletedFlag().trim().toLowerCase().equalsIgnoreCase("y")) {
			return -2;
		}
		
		int result = deepnewsMapper.deleteByPrimaryKey(id);
		
		return result;
	}
}
