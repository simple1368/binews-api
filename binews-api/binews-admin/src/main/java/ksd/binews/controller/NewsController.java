package ksd.binews.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;

import ksd.binews.annotation.Authorization;
import ksd.binews.annotation.CurrentAdmin;
import ksd.binews.constants.MsgConstants;
import ksd.binews.dto.BaseResponse;
import ksd.binews.entity.Admin;
import ksd.binews.entity.News;
import ksd.binews.service.NewsService;

/**
 * 资讯
 * @author Administrator
 *
 */
@RequestMapping("/admin")
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
	@Authorization
	@RequestMapping(path = "/news", method = RequestMethod.GET)
	public BaseResponse queryNews(@RequestParam(value="pageNum",defaultValue="1")int pageNum,
			@RequestParam(value="ti",required=false) String ti,
			@RequestParam(value="begin",required=false) String begin,
			@RequestParam(value="end",required=false) String end) {

		// 条件参数
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("title", ti);
		//paramMap.put("type", ty);
		paramMap.put("begin", begin);
		paramMap.put("end", end);
		PageInfo pageInfo = newsService.queryNewsPageInfo(pageNum, 10, paramMap);
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("pageInfo", pageInfo);

		return BaseResponse.success(data);
	}

	/**
	 * 新增资讯
	 * 
	 * @param newsDTO
	 * @return
	 */
	@Authorization
	@RequestMapping(path = "/news/add", method = RequestMethod.POST)
	public BaseResponse addNews(@RequestBody @Validated News news, 
			BindingResult bindingResult,@CurrentAdmin Admin admin) {
		log.info("====================");
		if (news == null || StringUtils.isBlank(news.getTitle()) || StringUtils.isBlank(news.getContent()) || news.getPublishStatus() == null
				|| StringUtils.isBlank(news.getPublishBy()) || StringUtils.isBlank(news.getCommentFlag())
				|| StringUtils.isBlank(news.getShareFlag()) || StringUtils.isBlank(news.getImportantFlag())) {
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_MISSING);
		}
		log.info("====================");
		if (bindingResult.hasErrors()) {
			for (FieldError err : bindingResult.getFieldErrors()) {
				log.info("error code = [" + err.getCode() + "]");
			}
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_PATTERN_BAD);
		}

		Map<String, Object> resultMap = newsService.insertNews(news, admin.getId());
		if ((Integer) resultMap.get("code") <= 0) {
			return BaseResponse.error(MsgConstants.MSG_OPERATION_FAIL);
		}
		
		Map<String, Object> returnData = new HashMap<String, Object>();
		returnData.put("id", resultMap.get("id"));
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS, returnData);
	}
	
	/**
	 * 修改
	 * @param news
	 * @param bindingResult
	 * @return
	 */
	@Authorization
	@RequestMapping(path = "/news/update", method = RequestMethod.POST)
	public BaseResponse updateNews(@RequestBody @Validated News news, 
			BindingResult bindingResult,@CurrentAdmin Admin admin) {

		if (news == null || StringUtils.isBlank(news.getId())) {
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_MISSING);
		}
		if (bindingResult.hasErrors()) {
			for (FieldError err : bindingResult.getFieldErrors()) {
				log.info("error code = [" + err.getCode() + "]");
			}
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_PATTERN_BAD);
		}

		Map<String, Object> resultMap = newsService.updateNews(news,admin.getId());
		if ((Integer) resultMap.get("code") == -2) {
			return BaseResponse.error("该资讯不存在，或者已经被删除");
		}
		if ((Integer) resultMap.get("code") <= 0) {
			return BaseResponse.error(MsgConstants.MSG_OPERATION_FAIL);
		}
		
		Map<String, Object> returnData = new HashMap<String, Object>();
		returnData.put("id", resultMap.get("id"));
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS, returnData);
	}

	/**
	 * 删除资讯
	 * 
	 * @param id
	 * @param req
	 * @return
	 */
	@Authorization
	@RequestMapping(path = "/news/del", method = RequestMethod.POST)
	public BaseResponse deleteNews(@RequestBody @Validated News news,
			@CurrentAdmin Admin admin) {
		
		if (StringUtils.isBlank(news.getId())) {
			return BaseResponse.error("请选择要删除的资讯");
		}
		
		int result = newsService.deleteNewsById(news.getId(), admin.getId());
		if (result == -2) {
			return BaseResponse.error("未找到该资讯，或者已经被删除");
		}
		if (result <= 0) {
			return BaseResponse.error(MsgConstants.MSG_OPERATION_FAIL);
		}
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS);
	}

}
