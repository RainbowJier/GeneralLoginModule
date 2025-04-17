package com.example.frame.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.frame.controller.request.LoginRequest;
import com.example.frame.controller.request.RegisterRequest;
import com.example.frame.model.JsonData;
import com.example.frame.model.entity.SysUser;


/**
 * (SysUser)表服务接口
 *
 * @author makejava
 * @since 2025-04-17 16:39:02
 */
public interface SysUserService {
    JsonData register(RegisterRequest registerRequest);

    JsonData login(LoginRequest loginRequest);

    JsonData getUserById(String id);

    JsonData selectAll(int pageNum, int pageSize);
}

