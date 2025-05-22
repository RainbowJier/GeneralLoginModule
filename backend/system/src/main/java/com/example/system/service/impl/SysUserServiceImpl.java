package com.example.system.service.impl;


import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.example.common.constant.RedisKey;
import com.example.common.model.entity.system.SystemUser;
import com.example.common.model.entity.LoginUser;
import com.example.common.enums.BizCode;
import com.example.common.enums.SendCode;
import com.example.common.exception.BizException;
import com.example.common.util.CommonUtil;
import com.example.common.util.JsonData;
import com.example.system.controller.request.LoginRequest;
import com.example.system.controller.request.RegisterRequest;
import com.example.system.controller.request.ResetPwdRequest;
import com.example.system.manager.SysUserManager;
import com.example.common.model.dto.SystemUserDTO;
import com.example.system.service.NotifyService;
import com.example.system.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.Md5Crypt;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SysUserServiceImpl implements SysUserService {
    @Resource
    private SysUserManager sysUserManager;

    @Resource
    private NotifyService notifyService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Value("${token.expiresTime:604800}")
    private Long tokenRememberExpire;

    @Override
    @Transactional
    public JsonData register(RegisterRequest registerRequest) {
        String email = registerRequest.getEmail();
        String code = registerRequest.getCode();

        // Check the code.
        boolean checkCode = notifyService.checkCode(SendCode.USER_REGISTER.name(), email, code);
        if (!checkCode) {
            return JsonData.buildError(BizCode.CODE_ERROR);
        }

        SystemUser systemUser = new SystemUser();
        BeanUtils.copyProperties(registerRequest, systemUser);

        systemUser.setAccount(CommonUtil.generateUUID())
                .setEmail(email)
                .setUsername(registerRequest.getUsername());

        // Encrypt.
        String salt = "$1$" + CommonUtil.getStringNumRandom(8);
        systemUser.setSalt(salt);
        String encryptedPassword = Md5Crypt.md5Crypt(registerRequest.getPassword().getBytes(), salt);
        systemUser.setPassword(encryptedPassword);

        try {
            int insertRow = sysUserManager.insert(systemUser);
            if (insertRow < 1) {
                return JsonData.buildError(BizCode.EMAIL_REPEAT);
            }

            return JsonData.buildSuccess();
        } catch (Exception e) {
            throw new BizException(BizCode.EMAIL_REPEAT);
        }
    }

    @Override
    public JsonData login(LoginRequest loginRequest) {
        // Check user exist
        SystemUser systemUser = sysUserManager.selectOne(loginRequest.getEmail());

        if (systemUser == null) {
            return JsonData.buildError(BizCode.ACCOUNT_UNREGISTER);
        }

        // Match password.
        String secret = systemUser.getSalt();
        String loginPwd = loginRequest.getPassword();
        String encryptedPwd = Md5Crypt.md5Crypt(loginPwd.getBytes(), secret);
        if (!encryptedPwd.equals(systemUser.getPassword())) {
            return JsonData.buildError(BizCode.ACCOUNT_PWD_ERROR);
        }

        // Login success.
        Long userId = systemUser.getId();

        boolean rememberMe = loginRequest.getRememberMe();
        if(rememberMe){
            StpUtil.login(userId, tokenRememberExpire);
        }else{
            StpUtil.login(userId);
        }

        // Generate token.
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

        // Return the current user info.
        LoginUser loginUser = new LoginUser();
        BeanUtils.copyProperties(systemUser, loginUser);

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("tokenInfo", tokenInfo);
        resultMap.put("userInfo", loginUser);
        return JsonData.buildSuccess(resultMap);
    }

    @Override
    @Transactional
    public JsonData reset(ResetPwdRequest resetUserRequest) {
        try {
            // Check email exits or not.
            SystemUser systemUser = sysUserManager.selectOne(resetUserRequest.getEmail());
            if (systemUser == null) {
                return JsonData.buildError(BizCode.ACCOUNT_UNREGISTER);
            }

            // Verify the email code.
            String email = resetUserRequest.getEmail();
            String code = resetUserRequest.getCode();
            boolean checkCode = notifyService.checkCode(SendCode.USER_RESET_PASSWORD.name(), email, code);
            if (!checkCode) {
                return JsonData.buildError(BizCode.CODE_ERROR);
            }

            // Check the reset times are more than 5 times in 24 hours or not.
            if (isOverLimit(systemUser.getEmail())) {
                return JsonData.buildError(BizCode.RESET_PASSWORD_LIMIT);
            }

            // Update password.
            String salt = systemUser.getSalt();
            String encryptedPassword = Md5Crypt.md5Crypt(resetUserRequest.getPassword().getBytes(), salt);
            systemUser.setPassword(encryptedPassword);

            int row = sysUserManager.update(systemUser);
            if (row < 1) {
                return JsonData.buildError(BizCode.RESET_PASSWORD_FAIL);
            }

            // Record the times the user has tried to reset password in the Redis.
            String key = String.format(RedisKey.LIMIT_RESET_PASSWORD_KEY, email);
            stringRedisTemplate.opsForValue().increment(key, 1);
            stringRedisTemplate.expire(key, 1, TimeUnit.DAYS);

            return JsonData.buildSuccess("重置成功");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private boolean isOverLimit(String email) {
        String key = String.format(RedisKey.LIMIT_RESET_PASSWORD_KEY, email);

        int count;
        if (stringRedisTemplate.opsForValue().get(key) == null) {
            count = 0;
        } else {
            count = Integer.parseInt(stringRedisTemplate.opsForValue().get(key));
        }
        return count >= 3;
    }

    @Override
    public JsonData selectAllUsers() {
        List<SystemUser> systemUsers = sysUserManager.selectAllUsers();

        List<SystemUserDTO> list = new ArrayList<>();

        for (SystemUser systemUser : systemUsers) {
            SystemUserDTO systemUserDTO = new SystemUserDTO();
            BeanUtils.copyProperties(systemUser, systemUserDTO);
            list.add(systemUserDTO);
        }

        return JsonData.buildSuccess(list);
    }
}

