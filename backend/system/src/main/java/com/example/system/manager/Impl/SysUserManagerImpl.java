package com.example.system.manager.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.system.manager.SysUserManager;
import com.example.system.mapper.SysUserMapper;
import com.example.system.model.entity.SysUser;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;


@Component
public class SysUserManagerImpl implements SysUserManager {

    @Resource
    private SysUserMapper sysUserMapper;


    @Override
    public int insert(SysUser sysUser) {
        return sysUserMapper.insert(sysUser);
    }

    @Override
    public SysUser selectList(String email) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getEmail, email)
                .eq(SysUser::getStatus, 1);

        return sysUserMapper.selectOne(queryWrapper);
    }
}
