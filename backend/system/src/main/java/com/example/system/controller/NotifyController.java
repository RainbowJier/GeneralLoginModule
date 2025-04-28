package com.example.system.controller;


import com.example.common.constant.RedisKey;
import com.example.common.enums.BizCode;
import com.example.common.enums.OperationType;
import com.example.common.enums.SendCodeEnum;
import com.example.common.exception.BizException;
import com.example.common.util.CommonUtil;
import com.example.common.util.JsonData;
import com.example.system.aop.annotation.SysLogAnno;
import com.example.system.controller.request.SendCodeRequest;
import com.example.system.service.NotifyService;
import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Common verify module.
 */
@Slf4j
@RestController
@RequestMapping("/api/notify")
public class NotifyController {

    @Resource
    private Producer captchaProducer;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Resource
    private NotifyService notifyService;

    @Value("${captcha.expiredTime}")
    private Long CAPTCHA_EXPIRE_TIME;

    /**
     * Seng Captcha code
     */
    @SysLogAnno(description = "Captcha code", operateType = OperationType.OTHER)
    @GetMapping("/captcha")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) {
        String key = getCaptchaKey(request);
        String value = captchaProducer.createText();
        log.info("Captcha code: {}", value);

        // 保存到Redis
        redisTemplate.opsForValue()
                .set(key, value, CAPTCHA_EXPIRE_TIME, TimeUnit.SECONDS);

        BufferedImage bufferedImage = captchaProducer.createImage(value);
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            ImageIO.write(bufferedImage, "jpg", outputStream);

            outputStream.flush();
        } catch (IOException e) {
            log.error("Send captcha error: {}", e.getMessage());
            throw new BizException(BizCode.CODE_CAPTCHA_EXCEPTION);
        }
    }

    public String getCaptchaKey(HttpServletRequest request) {
        String ip = CommonUtil.getIpAddr(request);
        String userAgent = request.getHeader("User-Agent");

        return String.format(RedisKey.REGISTER_CAPTCHA_KEY, CommonUtil.MD5(ip + userAgent));
    }


    /**
     * Send code.
     */
    @SysLogAnno(description = "Send phone / mial code", operateType = OperationType.OTHER)
    @PostMapping("sendEmailCode")
    public JsonData sendEmailCode(SendCodeRequest sendCodeRequest, HttpServletRequest request) throws MessagingException {
        // Obtain the captcha code.
        String key = getCaptchaKey(request);
        String codeCache = (String) redisTemplate.opsForValue().get(key);
        String code = sendCodeRequest.getCode();

        if (codeCache != null && code != null) {
            if (codeCache.equalsIgnoreCase(code)) {

                // delete captcha code.
                redisTemplate.delete(key);

                notifyService.sendCode(SendCodeEnum.USER_REGISTER, sendCodeRequest.getTo());

                return JsonData.buildSuccess();
            }
        }

        return JsonData.buildError("Error Captcha Code.");
    }
}
