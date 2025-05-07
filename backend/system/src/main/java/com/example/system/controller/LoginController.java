package com.example.system.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.example.common.enums.OperationType;
import com.example.common.util.JsonData;
import com.example.system.aop.annotation.SysLogAnno;
import com.example.system.controller.request.LoginRequest;
import com.example.system.controller.request.RegisterRequest;
import com.example.system.controller.request.ResetPwdRequest;
import com.example.system.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * Login Module.
 */
@Slf4j
@RestController
@RequestMapping("/system")
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


    /**
     * Reset password
     */
    @SysLogAnno(description = "Reset UserInfo", operateType = OperationType.UPDATE)
    @PostMapping("reset")
    public JsonData reset(ResetPwdRequest resetPwdRequest) {
        return sysUserService.reset(resetPwdRequest);
    }

    @PostMapping("checkToken")
    public JsonData checkToken(String token){
        return JsonData.buildSuccess();
    }

    @GetMapping("selectAllUsers")
    public JsonData selectAllUsers(){
        return sysUserService.selectAllUsers();
    }




}
