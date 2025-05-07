package com.example.system.service.impl;


import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.example.common.entity.LoginUser;
import com.example.common.enums.BizCode;
import com.example.common.enums.SendCode;
import com.example.common.exception.BizException;
import com.example.common.util.CommonUtil;
import com.example.common.util.JsonData;
import com.example.system.controller.request.LoginRequest;
import com.example.system.controller.request.RegisterRequest;
import com.example.system.controller.request.ResetPwdRequest;
import com.example.system.manager.SysUserManager;
import com.example.system.model.dto.SysUserDTO;
import com.example.system.model.entity.SysUser;
import com.example.system.service.NotifyService;
import com.example.system.service.SysUserService;
import com.fasterxml.jackson.databind.util.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.Md5Crypt;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class SysUserServiceImpl implements SysUserService {
    @Resource
    private SysUserManager sysUserManager;

    @Resource
    private NotifyService notifyService;

    @Override
    @Transactional
    public JsonData register(RegisterRequest registerRequest) {
        String email = registerRequest.getEmail();
        String code = registerRequest.getCode();

        // Check the code.
        boolean checkCode = notifyService.checkCode(SendCode.USER_REGISTER, email, code);
        if (!checkCode) {
            return JsonData.buildError(BizCode.CODE_ERROR);
        }

        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(registerRequest, sysUser);

        sysUser.setAccount(CommonUtil.generateUUID())
                .setEmail(email)
                .setUsername(registerRequest.getUsername());

        // Encrypt.
        String salt = "$1$" + CommonUtil.getStringNumRandom(8);
        sysUser.setSalt(salt);
        String encryptedPassword = Md5Crypt.md5Crypt(registerRequest.getPassword().getBytes(), salt);
        sysUser.setPassword(encryptedPassword);

        try {
            int insertRow = sysUserManager.insert(sysUser);
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
        SysUser sysUser = sysUserManager.selectOne(loginRequest.getEmail());

        if (sysUser == null) {
            return JsonData.buildError(BizCode.ACCOUNT_UNREGISTER);
        }

        // Match password.
        String secret = sysUser.getSalt();
        String loginPwd = loginRequest.getPassword();
        String encryptedPwd = Md5Crypt.md5Crypt(loginPwd.getBytes(), secret);
        if (!encryptedPwd.equals(sysUser.getPassword())) {
            return JsonData.buildError(BizCode.ACCOUNT_PWD_ERROR);
        }

        // Login success.
        Long userId = sysUser.getId();
        StpUtil.login(userId);

        // Generate token.
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

        // Return the current user info.
        LoginUser loginUser = new LoginUser();
        BeanUtils.copyProperties(sysUser, loginUser);

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("tokenInfo", tokenInfo);
        resultMap.put("userInfo",loginUser);
        return JsonData.buildSuccess(resultMap);
    }

    @Override
    public JsonData getUserById(String id) {
        return null;
    }

    @Override
    public JsonData selectAll(int pageNum, int pageSize) {
        return null;
    }

    @Override
    public JsonData reset(ResetPwdRequest resetUserRequest) {
        // Check email exits or not.
        SysUser sysUser = sysUserManager.selectOne(resetUserRequest.getEmail());
        if (sysUser == null) {
            return JsonData.buildError(BizCode.ACCOUNT_UNREGISTER);
        }

        // Verify the email code.
        String email = resetUserRequest.getEmail();
        String code = resetUserRequest.getCode();
        boolean checkCode = notifyService.checkCode(SendCode.USER_RESET_PASSWORD, email, code);
        if (!checkCode) {
            return JsonData.buildError(BizCode.CODE_ERROR);
        }

        // Check the reset times are more than 5 times in 24 hours or not.

        // Record the times the user has tried to reset password in the Redis.

        return null;
    }

    @Override
    public JsonData selectAllUsers() {
        List<SysUser> sysUsers = sysUserManager.selectAllUsers();

        List<SysUserDTO> list = new ArrayList<>();

        for (SysUser sysUser : sysUsers) {
            SysUserDTO sysUserDTO = new SysUserDTO();
            BeanUtils.copyProperties(sysUser, sysUserDTO);
            list.add(sysUserDTO);
        }

        return JsonData.buildSuccess(list);
    }
}

