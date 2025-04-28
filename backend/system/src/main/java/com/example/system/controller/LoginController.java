package com.example.system.controller;

import com.example.common.enums.OperationType;
import com.example.common.util.JsonData;
import com.example.system.aop.annotation.SysLogAnno;
import com.example.system.controller.request.LoginRequest;
import com.example.system.controller.request.RegisterRequest;
import com.example.system.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * Login Module.
 */
@Slf4j
@RestController
@RequestMapping("/api/account")
public class LoginController {
    @Resource
    private SysUserService sysUserService;


    /**
     * Register
     */
    @SysLogAnno(description = "Register", operateType = OperationType.ADD)
    @PostMapping("register")
    public JsonData register(RegisterRequest registerRequest) {
        return sysUserService.register(registerRequest);
    }


    /**
     * Login
     */
    @SysLogAnno(description = "Login", operateType = OperationType.QUERY)
    @PostMapping("login")
    public JsonData login(LoginRequest loginRequest) {
        return sysUserService.login(loginRequest);
    }


}
