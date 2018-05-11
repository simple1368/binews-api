package ksd.binews.controller;

import java.sql.Date;
import java.text.SimpleDateFormat;
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
import ksd.binews.service.NewsService;
import ksd.binews.utils.PublicUtil;

/**
 * 资讯
 * 
 * @author Administrator
 *
 */
@RestController
public class NewsController {
	private static final Logger log = LoggerFactory.getLogger(NewsController.class);

	@Autowired
	private NewsService newsService;

	/**
	 * 查询资讯
	 * 
	 * @param pageNum
	 *            当前页
	 * @return
	 */
	@RequestMapping(path = "/news", method = RequestMethod.GET)
	public BaseResponse queryNewsFront(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum, 
			@RequestParam(value="uuid",defaultValue="",required=false) String uuid) {

		// 条件参数
		Map<String, Object> paramMap = new HashMap<String, Object>();
		PageInfo pageInfo = newsService.queryNewsPageInfoFront(pageNum, 10, paramMap);

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("pageInfo", pageInfo);
		if (StringUtils.isBlank(uuid)) {
			data.put("uuid", PublicUtil.getUUID());
		}
		return BaseResponse.success(data);
	}

	/**
	 * 定时查询新资讯
	 * 
	 * @param time
	 * @return
	 */
	@RequestMapping(path = "/news/timing", method = RequestMethod.GET)
	public BaseResponse queryNewsTiming(String time) {

		if (StringUtils.isBlank(time)) {
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_MISSING);
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		int count = newsService.queryNewsPageInfoTiming(sdf.format(new Date(new Long(time))));

		Map<String, Object> returnData = new HashMap<String, Object>();
		returnData.put("count", count);
		return BaseResponse.success(returnData);
	}

}
