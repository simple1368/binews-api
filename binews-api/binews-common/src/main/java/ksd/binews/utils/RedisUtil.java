package ksd.binews.utils;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@SuppressWarnings({"rawtypes","unchecked"})
@Component
public class RedisUtil {

	
	@Autowired
	private RedisTemplate redisTemplate;
	
	
	public void set(String key, Object value) {
		ValueOperations<String, Object> vo = redisTemplate.opsForValue();
		vo.set(key, value);
	}
	
	public void set(String key, Object value, long timeout, TimeUnit unit) {
		redisTemplate.boundValueOps(key).set(value, timeout, unit);;
	}
	
	public Object get(String key) {
		ValueOperations<String, Object> vo = redisTemplate.opsForValue();
		return vo.get(key);
	}
	
	
	public void del(String key){
		redisTemplate.delete(key);
	}
	
	public boolean exists(String key) {
		return redisTemplate.hasKey(key);
	}
}
