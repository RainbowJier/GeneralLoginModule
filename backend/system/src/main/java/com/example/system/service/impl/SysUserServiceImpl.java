package com.example.system.service.impl;


import com.example.common.enums.BizCode;
import com.example.common.enums.SendCodeEnum;
import com.example.common.exception.BizException;
import com.example.common.util.CommonUtil;
import com.example.common.util.JsonData;
import com.example.frame.controller.request.LoginRequest;
import com.example.frame.controller.request.RegisterRequest;
import com.example.frame.model.entity.SysUser;
import com.example.system.mapper.SysUserMapper;
import com.example.system.service.NotifyService;
import com.example.system.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.Md5Crypt;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * (SysUser)表服务实现类
 *
 * @author makejava
 * @since 2025-04-17 16:39:02
 */

@Slf4j
@Service
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private NotifyService notifyService;

    @Override
    @Transactional
    public JsonData register(RegisterRequest registerRequest) {
        String email = registerRequest.getEmail();
        String code = registerRequest.getCode();

        // 校验验证码
        boolean checkCode = notifyService.checkCode(SendCodeEnum.USER_REGISTER, email, code);
        if (!checkCode) {
            return JsonData.buildError(BizCode.CODE_ERROR);
        }

        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(registerRequest, sysUser);

        sysUser.setAccount(CommonUtil.generateUUID())
                .setEmail(email)
                .setUsername(registerRequest.getUsername());

        // 设置加密盐
        String salt = "$1$" + CommonUtil.getStringNumRandom(8);
        sysUser.setSalt(salt);
        String encryptedPassword = Md5Crypt.md5Crypt(registerRequest.getPassword().getBytes(), salt);
        sysUser.setPassword(encryptedPassword);

        try {
            int insertRow = sysUserMapper.insert(sysUser);
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
        return null;
    }

    @Override
    public JsonData getUserById(String id) {
        return null;
    }

    @Override
    public JsonData selectAll(int pageNum, int pageSize) {
        return null;
    }
}

