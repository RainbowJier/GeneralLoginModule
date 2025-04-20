package com.example.system.component;

import com.example.common.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @Description：TODO
 * @Author： RainbowJier
 * @Data： 2024/8/24 11:45
 */

@Slf4j
@Component
public class SmsComponent {

    @Resource
    private RestTemplate restTemplate;


    /**
     * Send verification code by SMS.
     * 异步 + http 连接池
     */
    @Async("threadPoolTaskExecutor")
    public void sendEmail(String to, String templateId,String value) {
        // todo:

    }
}
