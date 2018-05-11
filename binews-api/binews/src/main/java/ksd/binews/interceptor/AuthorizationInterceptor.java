package ksd.binews.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSONObject;

import ksd.binews.annotation.Authorization;
import ksd.binews.constants.IConstants;
import ksd.binews.dto.BaseResponse;
import ksd.binews.entity.TokenModel;
import ksd.binews.utils.TokenUtil;

@Component
@Configuration
public class AuthorizationInterceptor extends HandlerInterceptorAdapter{
	
	@Autowired
	private TokenUtil tokenUtil;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
		
		//如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        //不需要验证权限
        if (method.getAnnotation(Authorization.class) == null) {
        	return true;
        }
        
        //从header中得到token
        String authorization = request.getHeader(IConstants.AUTHORIZATION);
        //验证token
        TokenModel model = tokenUtil.getToken(authorization);
        if (tokenUtil.checkToken(model)) {
            //如果token验证成功，将token对应的用户id存在request中，便于之后注入
            request.setAttribute(IConstants.CURRENT_USER_ID, model.getUserId());
            return true;
        }
        
        response.setCharacterEncoding("utf-8");
		BaseResponse data = new BaseResponse(300, "您还未登录或登录已失效，请重新登录", null);
        
        //如果验证token失败，并且方法注明了Authorization，返回401错误
        if (method.getAnnotation(Authorization.class) != null) {
            //response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().print(JSONObject.toJSONString(data));
            return false;
        }
        return true;
	}
}
