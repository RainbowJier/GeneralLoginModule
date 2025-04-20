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
 * 登录模块
 */
@Slf4j
@RestController
@RequestMapping("/api/account")
public class LoginController {

    @Autowired
    private Producer captchaProducer;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Resource
    private SysUserService sysUserService;

    /**
     * Captcha expire time 10 minutes, unit is milliseconds.
     */
    @Value("${captcha.expiredTime}")
    private String CAPTCHA_EXPIRE_TIME;

    /**
     * 获取登录图形验证码
     */
    @SysLogAnno(description = "发送图像验证码", operateType = OperationType.OTHER)
    @GetMapping("/captcha")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) {
        String key = getCaptchaKey(request);
        String value = captchaProducer.createText();
        log.info("图形验证码：{}", value);

        // 保存到Redis
        redisTemplate.opsForValue()
                .set(key, value, Long.parseLong(CAPTCHA_EXPIRE_TIME), TimeUnit.SECONDS);

        BufferedImage bufferedImage = captchaProducer.createImage(value);
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            ImageIO.write(bufferedImage, "jpg", outputStream);

            outputStream.flush();
        } catch (IOException e) {
            log.error("图形验证码发送异常: {}", e.getMessage());
            throw new BizException(BizCode.CODE_CAPTCHA_EXCEPTION);
        }
    }

    public String getCaptchaKey(HttpServletRequest request) {
        String ip = CommonUtil.getIpAddr(request);
        String userAgent = request.getHeader("User-Agent");

        return String.format(RedisKey.REGISTER_CAPTCHA_KEY,CommonUtil.MD5(ip + userAgent));
    }


    /**
     * 发送邮箱
     */
    @SysLogAnno(description = "发送邮箱验证码", operateType = OperationType.OTHER)
    @PostMapping("sendCode")
    public JsonData sendCode(){
        // 校验图形验证码

        // 生成 6 位

        // 发送邮箱


        return JsonData.buildSuccess();
    }


    /**
     * 用户注册
     */
    @SysLogAnno(description = "注册功能", operateType = OperationType.ADD)
    @PostMapping("register")
    public JsonData register(RegisterRequest registerRequest) {
        return sysUserService.register(registerRequest);
    }


    /**
     * 用户登录
     */
    @SysLogAnno(description = "登录功能", operateType = OperationType.QUERY)
    @PostMapping("login")
    public JsonData login(LoginRequest loginRequest) {
        return sysUserService.login(loginRequest);
    }


}
