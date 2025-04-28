package com.example.system.controller;

import com.example.common.constant.RedisKey;
import com.example.common.enums.BizCode;
import com.example.common.enums.OperationType;
import com.example.common.exception.BizException;
import com.example.common.util.CommonUtil;
import com.example.common.util.JsonData;
import com.example.frame.controller.request.LoginRequest;
import com.example.frame.controller.request.RegisterRequest;
import com.example.system.aop.annotation.SysLogAnno;
import com.example.system.component.EmailComponent;
import com.example.system.controller.request.SendCodeRequest;
import com.example.system.service.SysUserService;
import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


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
