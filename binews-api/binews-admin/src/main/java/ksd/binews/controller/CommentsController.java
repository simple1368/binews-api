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
import ksd.binews.constants.MsgConstants;
import ksd.binews.dto.BaseResponse;
import ksd.binews.entity.Comments;
import ksd.binews.entity.CommentsExpand;
import ksd.binews.service.CommentsService;

/**
 * 评论
 * @author Administrator
 *
 */
@RequestMapping("/admin")
@RestController
public class CommentsController {
	private static final Logger log = LoggerFactory.getLogger(CommentsController.class);

	@Autowired
	private CommentsService commentsService;
	
	/**
	 * 查询待审核评论列表
	 * @param pageNum
	 * @return
	 */
	@Authorization
	@RequestMapping(path="/comment/audits",method=RequestMethod.GET)
	public BaseResponse auditList(@RequestParam int pageNum,
			@RequestParam(value="ti",required=false) String title,
			@RequestParam(value="show",required=false) String show,
			@RequestParam(value="begin",required=false) String begin,
			@RequestParam(value="end",required=false) String end,
			@RequestParam(value="by",required=false) String by) {
		
		CommentsExpand condition = new CommentsExpand();
		condition.setDeletedFlag("n");
		condition.setNewsTitle(title);
		condition.setShowFlag(show);
		condition.setBegin(begin);
		condition.setEnd(end);
		condition.setCommentBy(by);
		PageInfo pageInfo = commentsService.queryListPageCondition(pageNum, 10, condition);
		
		Map<String, Object> returnData = new HashMap<String, Object>();
		returnData.put("pageInfo", pageInfo);
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS, returnData);
	}

	/**
	 * 审核
	 * @param comments
	 * @param bindingResult
	 * @return
	 */
	@Authorization
	@RequestMapping(path="/comment/audit",method=RequestMethod.POST)
	public BaseResponse auditComments(@RequestBody @Validated Comments comments,
			BindingResult bindingResult) {
		if (comments == null || StringUtils.isBlank(comments.getId()) 
				|| StringUtils.isBlank(comments.getShowFlag() + "")) {
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_MISSING);
		}
		if (bindingResult.hasErrors()) {
			for (FieldError err : bindingResult.getFieldErrors()) {
				log.info("error code = [" + err.getCode() + "]");
			}
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_PATTERN_BAD);
		}
		
		int result = commentsService.audit(comments);
		if (result == -2) {
			return BaseResponse.error("该条记录不存在，或者已经被删除");
		}
		if (result <= 0) {
			return BaseResponse.error(MsgConstants.MSG_OPERATION_FAIL);
		}
		
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS);
	}
	
	/**
	 * 删除评论
	 * 
	 * @param id
	 * @return
	 */
	@Authorization
	@RequestMapping(path = "/comment/del", method = RequestMethod.POST)
	public BaseResponse deleteComments(@RequestBody Comments comments) {

		if (StringUtils.isBlank(comments.getId())) {
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_MISSING);
		}

		int result = -1;
		try {
			result = commentsService.deleteCommentsById(comments.getId());
		} catch (Exception e) {
			return BaseResponse.error(MsgConstants.MSG_OPERATION_FAIL);
		}

		if (result == -2) {
			return BaseResponse.error("未找到该条评论，或者已经被删除");
		}
		if (result <= 0) {
			return BaseResponse.error(MsgConstants.MSG_OPERATION_FAIL);
		}
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS);
	}
}
