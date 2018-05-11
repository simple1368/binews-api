package ksd.binews.resolver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import ksd.binews.annotation.CurrentAdmin;
import ksd.binews.constants.IConstants;
import ksd.binews.entity.Admin;
import ksd.binews.mapper.AdminMapper;

/**
 * 增加方法注入，将含有CurrentUser注解的方法参数注入当前登录用户
 * @author simple
 * @date 2018年4月11日下午6:14:19
 */
@Component
@Configuration
public class CurrentUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        //如果参数类型是User并且有CurrentUser注解则支持
        if (parameter.getParameterType().isAssignableFrom(Admin.class) &&
                parameter.hasParameterAnnotation(CurrentAdmin.class)) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        //取出鉴权时存入的登录用户Id
        String currentUserId = (String) webRequest.getAttribute(IConstants.CURRENT_USER_ID, RequestAttributes.SCOPE_REQUEST);
        if (currentUserId != null) {
            //从数据库中查询并返回
            return adminMapper.selectByPrimaryKey(currentUserId);
        }
        throw new MissingServletRequestPartException(IConstants.CURRENT_USER_ID);
    }
}
