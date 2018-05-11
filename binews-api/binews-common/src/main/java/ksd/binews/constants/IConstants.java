package ksd.binews.constants;

public final class IConstants {

	private IConstants(){};
	
	public static final String LOGIN_USER_URL = "login";
	
	public static final String LOGIN_ADMIN_URL = "admin/login";
	
	/**
     * 存储当前登录用户id的字段名
     */
    public static final String CURRENT_USER_ID = "current_user_id";

    /**
     * token有效期（小时）
     */
    public static final int TOKEN_EXPIRES_HOUR = 24;

    /**
     * 存放Authorization的header字段
     */
    public static final String AUTHORIZATION = "authorization";
    
    public static final String PHONE_REG = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$";
}
