package ksd.binews.entity;

/**
 * Token的Model类
 * @author simple
 * @date 2018年4月11日下午1:26:19
 */
public class TokenModel {

    private String userId;

    private String token;

    public TokenModel(String userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
