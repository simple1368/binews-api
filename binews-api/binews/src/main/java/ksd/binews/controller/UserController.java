package ksd.binews.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ksd.binews.annotation.Authorization;
import ksd.binews.annotation.CurrentUser;
import ksd.binews.constants.IConstants;
import ksd.binews.constants.MsgConstants;
import ksd.binews.dto.BaseResponse;
import ksd.binews.entity.MobileCode;
import ksd.binews.entity.TokenModel;
import ksd.binews.entity.User;
import ksd.binews.service.MobileCodeService;
import ksd.binews.service.SmsService;
import ksd.binews.service.UserService;
import ksd.binews.utils.PublicUtil;
import ksd.binews.utils.TokenUtil;

@RestController
public class UserController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;
	@Autowired
	private TokenUtil tokenUtil;
	@Autowired
	private SmsService smsService;
	@Autowired
	private MobileCodeService mobileCodeService;
	
	/**
	 * 注册
	 * @param user
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(path="/register",method=RequestMethod.POST)
	public BaseResponse register(@ModelAttribute @Validated User model
			, BindingResult bindingResult) {
		if (model == null || StringUtils.isBlank(model.getMobile())
				|| StringUtils.isBlank(model.getPassword()) || StringUtils.isBlank(model.getRePassword())
				|| StringUtils.isBlank(model.getCheckCode())) {
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_MISSING);
		}
		
		if (bindingResult.hasErrors()) {
			for (FieldError err : bindingResult.getFieldErrors()) {
				logger.info("error code = [" + err.getCode() + "]");
				return BaseResponse.error(StringUtils.isBlank(err.getDefaultMessage()) ? 
						MsgConstants.MSG_PARAMETER_PATTERN_BAD : err.getDefaultMessage());
			}
		}
		if (!model.getPassword().equals(model.getRePassword())) {
			return BaseResponse.error("两次密码不一致");
		}
		
		if (!checkCode(model.getMobile(), model.getCheckCode())) {
			return BaseResponse.error("验证码错误，或验证码已失效");
		}
		
		model.setPassword(PublicUtil.password(model.getPassword()));
		
		Map<String, Object> resultMap = userService.register(model);
		
		if ((Integer)resultMap.get("code") == -2) {
			return BaseResponse.error("该手机号已经注册过了");
		}
		if ((Integer)resultMap.get("code") <= 0) {
			return BaseResponse.error(MsgConstants.MSG_OPERATION_FAIL);
		}
		
		Map<String, Object> returnData = new HashMap<String, Object>();
		returnData.put("id", resultMap.get("id"));
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS, returnData);
	}
	
	/**
	 * 登录
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(path="/login",method=RequestMethod.POST)
	public BaseResponse login(@ModelAttribute User model, HttpServletRequest request) {
		if (model == null || StringUtils.isBlank(model.getMobile()) || StringUtils.isBlank(model.getPassword())) {
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_MISSING);
		}
		
		User user = new User();
		user.setMobile(model.getMobile());
		User query_user = userService.queryByCondition(user);
		if (query_user == null) {
			return BaseResponse.error("该用户不存在");
		}
		if (!query_user.getPassword().equals(PublicUtil.password(model.getPassword()))) {
			return BaseResponse.error("密码错误");
		}

		//生成一个token，保存用户登录状态
        TokenModel tokenModel = tokenUtil.createToken(query_user.getId());
		
        Map<String, Object> returnData = new HashMap<String, Object>();
		returnData.put("token", tokenModel.getToken());
		return BaseResponse.success("登录成功",returnData);
	}
	
	/**
	 * 发送验证码
	 * @param mobile
	 * @return
	 */
	@RequestMapping(path="/code",method = RequestMethod.GET)
	public BaseResponse sendCode(@RequestParam("mobile") String mobile) {
		if (StringUtils.isBlank(mobile)) {
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_MISSING);
		}
		
		Pattern pattern = Pattern.compile(IConstants.PHONE_REG);
		if (!pattern.matcher(mobile).find()) {
			return BaseResponse.error(MsgConstants.MSG_PARAMETER_PATTERN_BAD);
		}
		
		String code = PublicUtil.getRandom() + "";
		String content = String.format(MsgConstants.MSG_REGISTER_TEMPLATE, code);
		
		MobileCode mobileCode = new MobileCode();
		mobileCode.setMobile(mobile);
		mobileCode.setCode(code);
		mobileCode.setSendTime(PublicUtil.getCurrentTimestamp());
		
		int count = mobileCodeService.insertSelective(mobileCode);
		if (count <= 0) {
			return BaseResponse.error(MsgConstants.MSG_OPERATION_FAIL);
		}
		
		String result = smsService.sendMessage(mobile, content);
		if (Integer.valueOf(result) != 100) {
			logger.error("短信发送失败{code：" + result + "，mobile：" + mobile + "}");
			return BaseResponse.error(MsgConstants.MSG_OPERATION_FAIL);
		}
		
		return BaseResponse.success(MsgConstants.MSG_OPERATION_SUCCESS);
	}
	
	/**
	 * 注销
	 * @param user
	 * @return
	 */
	@RequestMapping(path="/logout",method = RequestMethod.DELETE)
    @Authorization
    public BaseResponse logout(@CurrentUser User user) {
        tokenUtil.deleteToken(user.getId());
        return BaseResponse.success();
    }
	
	private boolean checkCode(String phone, String code){
		if (StringUtils.isBlank(phone) || StringUtils.isBlank(code)) {
			return false;
		}
		
		MobileCode mobileCode = mobileCodeService.queryMobileCode(phone);
		if (mobileCode == null || !mobileCode.getCode().equals(code)) {
			return false;
		}
		
		if (PublicUtil.getCurrentTimestamp().getTime() 
				- mobileCode.getSendTime().getTime() > 30 * 1000 * 60) {
			return false;
		}
		return true;
	}
}
