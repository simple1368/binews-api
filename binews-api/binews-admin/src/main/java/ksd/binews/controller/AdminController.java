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
import org.springframework.web.bind.annotation.RestController;

import ksd.binews.annotation.Authorization;
import ksd.binews.annotation.CurrentAdmin;
import ksd.binews.constants.MsgConstants;
import ksd.binews.dto.BaseResponse;
import ksd.binews.entity.Admin;
import ksd.binews.entity.TokenModel;
import ksd.binews.entity.User;
import ksd.binews.service.AdminService;
import ksd.binews.utils.PublicUtil;
import ksd.binews.utils.TokenUtil;

/**
 * 管理员
 * 
 * @author simple
 * @date 2018年4月9日上午11:04:06
 */
@RequestMapping("/admin")
@RestController
public class AdminController {
	
	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
	
	@Autowired
	private AdminService adminService;
	@Autowired
	private TokenUtil tokenUtil;

	/**
	 * 管理员登录
	 * @param username
	 * @param password
	 * @param request
	 * @return
	 */
	@RequestMapping(path="/login",method=RequestMethod.POST)
	public BaseResponse adminLogin(@RequestBody @Validated Admin admin,
			BindingResult bindingResult) {
		
		if (admin == null || StringUtils.isBlank(admin.getName()) || StringUtils.isBlank(admin.getPassword())) {
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_MISSING);
		}
		
		if (bindingResult.hasErrors()) {
			for (FieldError err : bindingResult.getFieldErrors()) {
				logger.info("error code = [" + err.getCode() + "]");
				return BaseResponse.error(StringUtils.isBlank(err.getDefaultMessage()) ? 
						MsgConstants.MSG_PARAMETER_PATTERN_BAD : err.getDefaultMessage());
			}
		}
		
		admin.setPassword(PublicUtil.password(admin.getPassword()));
		Admin query_admin = adminService.queryAdmin(admin);
		if (query_admin == null) {
			return BaseResponse.error("用户名或密码错误");
		}

		//生成一个token，保存用户登录状态
        TokenModel tokenModel = tokenUtil.createToken(query_admin.getId());
		
        Map<String, Object> returnData = new HashMap<String, Object>();
		returnData.put("token", tokenModel.getToken());
		return BaseResponse.success(returnData);
	}
	
	/**
	 * 注销
	 * @param token
	 * @return
	 */
	@Authorization
	@RequestMapping(path = "/logOut", method = RequestMethod.POST)
    public BaseResponse logout(@CurrentAdmin User user) {
        tokenUtil.deleteToken(user.getId());
        return BaseResponse.success();
    }

}
