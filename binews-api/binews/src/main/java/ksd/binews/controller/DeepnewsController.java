package ksd.binews.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;

import ksd.binews.constants.MsgConstants;
import ksd.binews.dto.BaseResponse;
import ksd.binews.service.DeepnewsService;
import ksd.binews.utils.PublicUtil;

/**
 * 深度资讯
 * @author Administrator
 *
 */
@RestController
public class DeepnewsController {
	
	private static final Logger logger = LoggerFactory.getLogger(DeepnewsController.class);

	@Autowired
	private DeepnewsService deepnewsService;
	
	
	/**
	 * 查询深度资讯列表
	 * @param pageNum
	 * @return
	 */
	@RequestMapping(path="/deepnews/list",method=RequestMethod.GET)
	public BaseResponse queryDeepnewsPageFront(@RequestParam(value="pageNum",defaultValue="1")int pageNum) {
		
		pageNum = pageNum < 1 ? 1 : pageNum;
		pageNum = pageNum > 1000 ? 1000 : pageNum;
		
		PageInfo pageInfo = deepnewsService.queryDeepnewsPageFront(pageNum, 10);
		
		Map<String, Object> returnData = new HashMap<String, Object>();
		returnData.put("pageInfo", pageInfo);
		
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS,returnData);
	}
	
	/**
	 * 查询深度资讯详情（前台）
	 * @param id
	 * @return
	 */
	@RequestMapping(path="/deepnews",method=RequestMethod.GET)
	public BaseResponse queryDeepnewsDetailFront(@RequestParam("id") String id) {
		
		if (StringUtils.isBlank(id)) {
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_MISSING);
		}
		
		Map<String, Object> returnData = new HashMap<String, Object>();
		returnData = PublicUtil.ConvertObjToMap(deepnewsService.queryDeepnewsDetailFront(id));
		
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS,returnData);
	}
	
}
