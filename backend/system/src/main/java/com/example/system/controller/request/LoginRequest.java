package com.example.frame.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: RainbowJier
 * @Description: 👺🐉😎用户账号登录
 * @Date: 2024/10/5 16:12
 * @Version: 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    /**
     * 手机号
     */
    private String phone;

    /**
     * 账号
     */
    private String account;

    /**
     * 密码
     */
    private String pwd;
}
