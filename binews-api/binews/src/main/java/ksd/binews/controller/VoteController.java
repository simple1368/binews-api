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

import ksd.binews.constants.MsgConstants;
import ksd.binews.dto.BaseResponse;
import ksd.binews.entity.Vote;
import ksd.binews.service.VoteService;

@RestController
public class VoteController {
	
	private static final Logger logger = LoggerFactory.getLogger(VoteController.class);

	@Autowired
	private VoteService voteService;
	
	/**
	 * 投票
	 * @param newsId
	 * @param uuid
	 * @param type 1:看多 2：看空
	 * @return
	 */
	@RequestMapping(path="/vote",method=RequestMethod.GET)
	public BaseResponse vote(String newsId, String uuid,
			@RequestParam(value="type",defaultValue="0") int type) {
		
		if (StringUtils.isBlank(newsId) || StringUtils.isBlank(uuid) || (type != 1 && type != 2)) {
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_MISSING);
		}
		
		Vote vote = new Vote();
		vote.setNewsId(newsId);
		vote.setUuid(uuid);
		vote.setType(type);
		
		Map<String, Object> result;
		try {
			result = voteService.vote(vote);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("======>" + e.getMessage());
			return BaseResponse.error(MsgConstants.MSG_OPERATION_FAIL);
		}
		if ((Integer)result.get("code") <= 0) {
			return BaseResponse.error(MsgConstants.MSG_OPERATION_FAIL);
		}
		
		Map<String, Object> returnData = new HashMap<String, Object>();
		returnData.put("upCount", result.get("upCount"));
		returnData.put("downCount", result.get("downCount"));
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS, returnData);
	}
}
