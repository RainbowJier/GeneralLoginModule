package com.example.system.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendCodeRequest {

    /**
     * 验证码
     */
    private String code;

    /**
     * 邮箱/手机改号
     */
    private String to;

}
