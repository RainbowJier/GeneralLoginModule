package com.example.system.aop;


import com.alibaba.fastjson.JSON;
import com.example.common.constant.RedisKey;
import com.example.common.enums.BizCode;
import com.example.common.exception.BizException;
import com.example.common.util.CommonUtil;
import com.example.common.util.JsonUtil;
import com.example.system.aop.annotation.LimitFlowAnno;
import com.example.system.aop.annotation.SysLogAnno;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.Md5Crypt;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;

@Component
@Aspect
@Slf4j
@Order(2)
public class LimitFlowAop {

    private final long UNIT = 60 * 1000;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Before("@annotation(limitFlowAnno)")
    private void handleBefore(JoinPoint joinPoint, LimitFlowAnno limitFlowAnno) {
        String argJsonStr = JSON.toJSONString(joinPoint.getArgs()[0]);
        HashMap<String, String> requestMap = JsonUtil.jsonStrToObj(argJsonStr, HashMap.class);

        String email = requestMap.get("to");
        String behavior = limitFlowAnno.behavior();
        String key = String.format(RedisKey.LIMIT_FLOW_KEY, behavior, email);

        if (isAllow(key, limitFlowAnno)) {
            log.info("请求通过");
        } else {
            throw new BizException(BizCode.CONTROL_FLOW);
        }
    }

    private boolean isAllow(String key, LimitFlowAnno limitFlowAnno) {
        String luaScript = """
                local key = KEYS[1]
                local current_time = tonumber(ARGV[1])
                local window_size = tonumber(ARGV[2])
                local threshold = tonumber(ARGV[3])
                
                -- Remove outdated entries
                redis.call('ZREMRANGEBYSCORE', key, 0, current_time - window_size)
                
                -- Check current count
                local count = redis.call('ZCARD', key)
                if count >= threshold then
                    return "0"  -- reject
                end
                
                -- Allow and record this request
                redis.call('ZADD', key, current_time, current_time)
                redis.call('PEXPIRE', key, window_size)  -- auto-expire the key
                return "1"  -- allow
                """;

        long windowSizeMillis = limitFlowAnno.windowSize() * UNIT;
        long requestLimit = limitFlowAnno.requestLimit();
        long currentTimestamp = System.currentTimeMillis();

        String result = stringRedisTemplate.execute(
                new DefaultRedisScript<>(luaScript, String.class),
                Collections.singletonList(key),
                String.valueOf(currentTimestamp),
                String.valueOf(windowSizeMillis),
                String.valueOf(requestLimit)
        );

        return "1".equals(result);
    }

}
