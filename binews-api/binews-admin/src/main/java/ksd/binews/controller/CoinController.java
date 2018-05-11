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
import ksd.binews.entity.Coin;
import ksd.binews.service.CoinService;
import ksd.binews.utils.PublicUtil;

/**
 * 币种
 * 
 * @author Administrator
 *
 */
@RequestMapping("/admin")
@RestController
public class CoinController {

	private static final Logger logger = LoggerFactory.getLogger(CoinController.class);

	@Autowired
	private CoinService coinService;

	/**
	 * 列表
	 * @param pageNum
	 * @return
	 */
	@Authorization
	@RequestMapping(path="/coin/list",method=RequestMethod.GET)
	public BaseResponse listCoin(@RequestParam(value="pageNum",defaultValue="1") int pageNum) {

		Map<String, Object> condition = new HashMap<String, Object>();
		//condition.put("deletedFlag", "n");
		PageInfo pageInfo = coinService.listCoinPage(pageNum, 10, condition);

		Map<String, Object> returnData = new HashMap<String, Object>();
		returnData.put("pageInfo", pageInfo);
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS, returnData);
	}
	
	/**
	 * 详情
	 * @param id
	 * @return
	 */
	@Authorization
	@RequestMapping(path="/coin",method=RequestMethod.GET)
	public BaseResponse Coin(String id) {

		if (StringUtils.isBlank(id)) {
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_MISSING);
		}
		
		Coin coin = coinService.queryByPrimaryKey(id);

		Map<String, Object> returnData = new HashMap<String, Object>();
		returnData = PublicUtil.ConvertObjToMap(coin);
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS, returnData);
	}
	
	/**
	 * 添加币种
	 * 
	 * @param coin
	 * @param bindingResult
	 * @return
	 */
	@Authorization
	//@RequestMapping(path = "/coin/add", method = RequestMethod.POST)
	public BaseResponse insertCoin(@RequestBody @Validated Coin coin, BindingResult bindingResult) {

		logger.info(coin.toString());

		if (coin == null || StringUtils.isBlank(coin.getCnName()) || StringUtils.isBlank(coin.getEnName())
				|| coin.getMarketValue() == null || StringUtils.isBlank(coin.getRemark()) || coin.getDayTradeAmount() == null) {
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_MISSING);
		}

		if (bindingResult.hasErrors()) {
			for (FieldError err : bindingResult.getFieldErrors()) {
				logger.info("error code = [" + err.getCode() + "]");
			}
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_PATTERN_BAD);
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			resultMap = coinService.insertCoin(coin);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return BaseResponse.error(MsgConstants.MSG_OPERATION_FAIL);
		}
		if ((Integer) resultMap.get("code") <= 0) {
			return BaseResponse.error(MsgConstants.MSG_OPERATION_FAIL);
		}

		Map<String, Object> returnData = new HashMap<String, Object>();
		returnData.put("id", resultMap.get("id"));
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS, returnData);
	}

	/**
	 * 修改币种信息
	 * @param coin
	 * @param bindingResult
	 * @return
	 */
	@Authorization
	@RequestMapping(path="/coin/update",method=RequestMethod.POST)
	public BaseResponse updateCoin(@RequestBody @Validated Coin coin, 
			BindingResult bindingResult, @CurrentAdmin Admin admin) {

		if (coin == null || StringUtils.isBlank(coin.getId())) {
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_MISSING);
		}

		if (bindingResult.hasErrors()) {
			for (FieldError err : bindingResult.getFieldErrors()) {
				logger.info("error code = [" + err.getCode() + "]");
			}
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_PATTERN_BAD);
		}

		int result = coinService.updateCoin(coin, admin);
		if (result == -2) {
			return BaseResponse.error("未找到该币种，或者已经被删除");
		}
		if (result <= 0) {
			return BaseResponse.error(MsgConstants.MSG_OPERATION_FAIL);
		}
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS);
	}
	
	@Authorization
	@RequestMapping(path = "/coin/show", method = RequestMethod.POST)
	public BaseResponse showCoin(@RequestBody @Validated Coin coin,
			BindingResult bindingResult, @CurrentAdmin Admin admin) {

		if (StringUtils.isBlank(coin.getId())) {
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_MISSING);
		}

		int result = coinService.showCoin(coin, admin);
		if (result == -2) {
			return BaseResponse.error("该条记录不存在");
		}
		if (result <= 0) {
			return BaseResponse.error(MsgConstants.MSG_OPERATION_FAIL);
		}
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS);
	}

	/**
	 * 删除币种
	 * 
	 * @param id
	 * @return
	 */
	@Authorization
	//@RequestMapping(path = "/coin/del", method = RequestMethod.POST)
	public BaseResponse deleteCoin(@RequestBody Coin coin) {

		if (StringUtils.isBlank(coin.getId())) {
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_MISSING);
		}

		int result = coinService.deleteCoin(coin.getId());
		if (result == -2) {
			return BaseResponse.error("未找到该币种，或者已经被删除");
		}
		if (result <= 0) {
			return BaseResponse.error(MsgConstants.MSG_OPERATION_FAIL);
		}
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS);
	}
}
