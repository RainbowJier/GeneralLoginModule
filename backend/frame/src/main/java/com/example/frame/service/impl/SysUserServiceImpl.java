package com.example.frame.service.impl;


import com.example.frame.controller.request.LoginRequest;
import com.example.frame.controller.request.RegisterRequest;
import com.example.frame.enums.BizCode;
import com.example.frame.mapper.SysUserMapper;
import com.example.frame.model.JsonData;
import com.example.frame.model.entity.SysUser;
import com.example.frame.service.SysUserService;
import com.example.frame.utils.CommonUtil;
import jodd.util.StringUtil;
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


    @Override
    @Transactional
    public JsonData register(RegisterRequest registerRequest) {
        // 校验验证码
        boolean checkCode;
        String code = registerRequest.getCode();
        // checkCode = notifyService.checkCode(SendCodeEnum.USER_REGISTER, phone, code);

//            if (!checkCode) {
//                return JsonData.buildResult(BizCodeEnum.CODE_ERROR);
//            }

        // 对象封装
        String username = registerRequest.getUsername();
        String email = registerRequest.getEmail();

        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(registerRequest, sysUser);

        sysUser.setAccount(CommonUtil.generateUUID());
        // 设置加密盐
        String salt = "$1$" + CommonUtil.getStringNumRandom(8);
        sysUser.setSalt(salt);
        String encryptedPassword = Md5Crypt.md5Crypt(registerRequest.getPassword().getBytes(), salt);
        sysUser.setPassword(encryptedPassword);
        sysUser.setEmail(email);
        sysUser.setUsername(username);

        try {
            int insertRow = sysUserMapper.insert(sysUser);
            if (insertRow < 1) {
                return JsonData.buildError(BizCode.EMAIL_REPEAT);
            }

            return JsonData.buildSuccess();
        } catch (Exception e) {
            throw new RuntimeException(BizCode.EMAIL_REPEAT.getMessage());
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

