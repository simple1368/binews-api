package ksd.binews.utils;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import ksd.binews.constants.IConstants;
import ksd.binews.entity.Audience;
import ksd.binews.entity.TokenModel;

/**
 * 通过Redis存储和验证token的实现类
 * @author simple
 * @date 2018年4月16日下午5:36:37
 */
@Component
@SuppressWarnings({"rawtypes","unchecked"})
public class TokenUtil {
	
	@Autowired
    private RedisTemplate redisTemplate;
	@Autowired
	private Audience audience;

	public TokenModel createToken(String userId) {
        //使用jwt作为源token
        String token = JwtUtil.createJWT(userId, audience.getClientId(), audience.getName(), audience.getExpiresSecond(), audience.getBase64Secret());
        //token = token + PublicUtil.getCurrentTimestamp().getTime();
        TokenModel model = new TokenModel(userId, token);
        //存储到redis并设置过期时间
        redisTemplate.boundValueOps(userId).set(token, IConstants.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
        return model;
    }

    public TokenModel getToken(String authentication) {
        if (StringUtils.isBlank(authentication)) {
            return null;
        }
        Claims claims = JwtUtil.parseJWT(authentication, audience.getBase64Secret());
        if (claims == null) {
        	return null;
		}
        //使用userId和源token简单拼接成的token，可以增加加密措施
        String userId = claims.get("userid") + "";
        String token = authentication;
        return new TokenModel(userId, token);
    }

    public boolean checkToken(TokenModel model) {
        if (model == null) {
            return false;
        }
        String token = (String) redisTemplate.boundValueOps(model.getUserId()).get();
        if (token == null || !token.equals(model.getToken())) {
            return false;
        }
        //如果验证成功，说明此用户进行了一次有效操作，延长token的过期时间
        redisTemplate.boundValueOps(model.getUserId()).expire(IConstants.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
        return true;
    }

    public void deleteToken(String userId) {
    	redisTemplate.delete(userId);
    }
}
