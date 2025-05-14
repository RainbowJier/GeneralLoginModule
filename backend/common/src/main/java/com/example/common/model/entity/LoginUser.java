package com.example.common.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: RainbowJier
 * @Description: 👺🐉😎
 * @Date: 2024/10/5 16:28
 * @Version: 1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginUser {
    private Long id;

    private String account;

    private String username;

    private String email;

    private String phone;
}
