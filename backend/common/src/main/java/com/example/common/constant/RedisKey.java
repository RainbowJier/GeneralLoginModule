package com.example.common.constant;


public class RedisKey {
    /**
     * 邮箱验证码
     * 第一个类型是类型，
     * 第二个类型唯一标识，手机号或者邮箱
     */
    public static final String CHECK_CODE_KEY = "code:%s:%s";

    /**
     * 图形验证码
     * 第一个类型是类型，（register、login、reset_password）
     * 第二个类型唯一标识，手机号或者邮箱
     */
    public static final String CHECK_CODE_CAPTCHA_KEY = "code:%s:%s";


    /**
     * 接口限流
     */
    public static final String LIMIT_FLOW_WINDOW_SIZE_KEY = "limit_flow:window_size:%s:%s";


    /**
     * 防止重复提交订单的token
     * 第一个：账号
     * 第二个：token
     */
    public static final String SUBMIT_ORDER_TOKEN_KEY = "order:submit:%s:%s";
}
