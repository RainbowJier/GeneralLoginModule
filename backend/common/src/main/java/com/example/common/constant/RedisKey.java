package com.example.common.constant;


public class RedisKey {


    /**
     * 注册-图形验证码
     */
    public static final String REGISTER_CAPTCHA_KEY = "system:register:captcha:%s";

    /**
     * 登录-图形验证码
     */
    public static final String LOGIN_CAPTCHA_KEY = "system:login:captcha:%s";

    /**
     * 验证码存储key：
     * 第一个类型是类型，
     * 第二个类型唯一标识，手机号或者邮箱
     */
    public  static final String CHECK_CODE_KEY = "code:%s:%s";

    /**
     * 防止重复提交订单的token
     * 第一个：账号
     * 第二个：token
     */
    public  static final String  SUBMIT_ORDER_TOKEN_KEY = "order:submit:%s:%s";
}
