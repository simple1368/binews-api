package ksd.binews.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ksd.binews.constants.MsgConstants;
import ksd.binews.dto.BaseResponse;
import ksd.binews.entity.User;
import ksd.binews.service.LikedService;

/**
 * 点赞
 * @author Administrator
 *
 */
@RestController
public class LikedController {
	private static final Logger log = LoggerFactory.getLogger(LikedController.class);

	@Autowired
	private LikedService likedService;

	/**
	 * 点赞或取消点赞
	 * 
	 * @param newsId
	 * @return
	 */
	@RequestMapping(path = "/liked", method = RequestMethod.POST)
	public BaseResponse liked(String newsId) {

		User user = new User("f863b4930c3f4869b3cb6d6eea6ec7c5", "张三");

		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			resultMap = likedService.liked(user, newsId);
		} catch (Exception e) {
			e.printStackTrace();
			return BaseResponse.error(MsgConstants.MSG_OPERATION_FAIL);
		}

		if ((Integer) resultMap.get("code") <= 0) {
			return BaseResponse.error(MsgConstants.MSG_OPERATION_FAIL);
		}

		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS);
	}

}
