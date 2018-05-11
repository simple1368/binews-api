package ksd.binews.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import ksd.binews.entity.LoggerInfo;
import ksd.binews.utils.PublicUtil;

//@Aspect
//@Component
public class LoggerAspect {

	private static final Logger logger = LoggerFactory.getLogger(LoggerAspect.class);

	//@Autowired
	//private LoggerInfoMapper loggerInfoMapper;
	@Autowired
	private StringRedisTemplate redisTemplate;
	@Autowired
	private CountDownLatch latch;
	
	@Pointcut("execution(public * ksd.binews.controller.*.*(..))")
	public void webLog() {
		
	}

	@Before("webLog()")
	public void doBefore(JoinPoint joinPoint) {
		// 接收到请求，记录请求内容
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();

		LoggerInfo loggerInfo = new LoggerInfo();
		
		// 记录下请求内容

		List<Map<String, Object>> paramList = new ArrayList<Map<String,Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		// 获取所有参数方法一：
		Enumeration<String> enu = request.getParameterNames();
		while (enu.hasMoreElements()) {
			String paraName = (String) enu.nextElement();
			map.clear();
			map.put(paraName, request.getParameter(paraName));
			paramList.add(map);
		}
		loggerInfo.setClientIp(request.getRemoteAddr());
		loggerInfo.setUri(request.getRequestURL().toString());
		loggerInfo.setMethod(request.getMethod());
		loggerInfo.setParamData(Arrays.asList(joinPoint.getArgs()) + JSONArray.toJSONString(paramList));
		loggerInfo.setCreateTime(PublicUtil.getCurrentTimestamp());
		
		//持久化
		//loggerInfoMapper.insertSelective(loggerInfo);
		redisTemplate.convertAndSend("log", JSONObject.toJSONString(loggerInfo).toString());
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
			logger.info(e.getMessage());
		}
	}

	@AfterReturning("webLog()")
	public void doAfterReturning(JoinPoint joinPoint) {
		// 处理完请求，返回内容
	}

}
