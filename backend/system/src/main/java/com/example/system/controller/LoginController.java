package com.example.system.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.example.common.enums.OperationType;
import com.example.common.util.JsonData;
import com.example.system.aop.annotation.SysLogAnno;
import com.example.system.controller.request.LoginRequest;
import com.example.system.controller.request.RegisterRequest;
import com.example.system.controller.request.ResetPwdRequest;
import com.example.system.service.SystemUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * Login Module.
 */
@Slf4j
@RestController
@RequestMapping("/system")
public class LoginController {
    @Resource
    private SystemUserService systemUserService;

    @Resource
    HttpServletRequest request;

    /**
     * Register
     */
    @SysLogAnno(description = "Register", operateType = OperationType.ADD)
    @PostMapping("register")
    public JsonData register(RegisterRequest registerRequest) {
        return systemUserService.register(registerRequest);
    }

    /**
     * Login
     */
    @SysLogAnno(description = "Login", operateType = OperationType.QUERY)
    @PostMapping("login")
    public JsonData login(LoginRequest loginRequest) {
        return systemUserService.login(loginRequest);
    }

    /**
     * logout
     */
    @SysLogAnno(description = "Logout", operateType = OperationType.OTHER)
    @PostMapping("logout")
    public JsonData logout() {
        Long userId = Long.valueOf(request.getHeader("loginId"));
        StpUtil.logout(userId);

        return JsonData.buildSuccess("退出登录");
    }

    /**
     * Reset password
     */
    @SysLogAnno(description = "Reset UserInfo", operateType = OperationType.UPDATE)
    @PostMapping("resetPwd")
    public JsonData resetPwd(ResetPwdRequest resetPwdRequest) {
        return systemUserService.reset(resetPwdRequest);
    }

    /**
     * Get all users.
     */
    @GetMapping("selectAllUsers")
    public JsonData selectAllUsers() {
        return systemUserService.selectAllUsers();
    }


}
