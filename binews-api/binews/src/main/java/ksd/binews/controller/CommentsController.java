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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;

import ksd.binews.annotation.Authorization;
import ksd.binews.annotation.CurrentUser;
import ksd.binews.constants.MsgConstants;
import ksd.binews.dto.BaseResponse;
import ksd.binews.entity.Comments;
import ksd.binews.entity.News;
import ksd.binews.entity.User;
import ksd.binews.service.CommentsService;
import ksd.binews.service.NewsService;

/**
 * 评论
 * @author Administrator
 *
 */
@RestController
public class CommentsController {
	private static final Logger log = LoggerFactory.getLogger(CommentsController.class);

	@Autowired
	CommentsService commentsService;
	@Autowired
	NewsService NewsService;

	/**
	 * 查询资讯评论
	 * 
	 * @param pageNum
	 * @param newsId
	 * @return
	 */
	@RequestMapping(path = "/comment", method = RequestMethod.GET)
	public BaseResponse queryComments(@RequestParam(value="pageNum",defaultValue="1") int pageNum,
			String newsId) {

		if (StringUtils.isBlank(newsId)) {
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_MISSING);
		}

		News news = NewsService.queryById(newsId);
		
		PageInfo<Comments> pageInfo = commentsService.queryNewsComments(pageNum, 10, newsId);

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("pageInfo", pageInfo);
		data.put("news", news);
		return BaseResponse.success(data);
	}

	/**
	 * 发表评论
	 * 
	 * @param comments
	 * @return
	 */
	@Authorization
	@RequestMapping(value = "/comment/add", method = RequestMethod.POST,consumes={"application/x-www-form-urlencoded;charset=UTF-8"})
	public BaseResponse addComments(@Validated Comments comments, 
			BindingResult bindingResult,@CurrentUser User user) {

		if (comments == null || StringUtils.isBlank(comments.getNewsId()) || StringUtils.isBlank(comments.getContent())) {
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_MISSING);
		}
		if (bindingResult.hasErrors()) {
			for (FieldError err : bindingResult.getFieldErrors()) {
				log.info("error code = [" + err.getCode() + "]");
				return BaseResponse.error(
					err.getDefaultMessage() == null ? MsgConstants.MSG_PARAMETER_PATTERN_BAD : err.getDefaultMessage());
			}
		}

		comments.setCommentBy(user.getId());

		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			resultMap = commentsService.publishComments(comments);
		} catch (Exception e) {
			e.printStackTrace();
			return BaseResponse.error(MsgConstants.MSG_OPERATION_FAIL);
		}

		if ((Integer) resultMap.get("code") <= 0) {
			return BaseResponse.error(MsgConstants.MSG_OPERATION_FAIL);
		}
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("id", resultMap.get("id"));
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS, data);
	}

}
