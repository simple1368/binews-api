package ksd.binews.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import ksd.binews.interceptor.AuthorizationInterceptor;
import ksd.binews.resolver.CurrentUserMethodArgumentResolver;

/**
 * 配置类，增加自定义拦截器和解析器
 * 
 * @author simple
 * @date 2018年4月11日下午1:59:33
 */
@Configuration
public class WebAppConfig extends WebMvcConfigurerAdapter {

	@Autowired
	private AuthorizationInterceptor authorizationInterceptor;
	@Autowired
	private CurrentUserMethodArgumentResolver currentUserMethodArgumentResolver;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authorizationInterceptor)
			.addPathPatterns("/**")
			.excludePathPatterns("/login/**","/error/**");
		super.addInterceptors(registry);
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(currentUserMethodArgumentResolver);
	}
}
