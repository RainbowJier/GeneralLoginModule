package com.example.system.manager.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.common.model.entity.system.SystemUser;
import com.example.system.manager.SysUserManager;
import com.example.system.mapper.SysUserMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;


@Component
public class SysUserManagerImpl implements SysUserManager {

    @Resource
    private SysUserMapper sysUserMapper;


    @Override
    public int insert(SystemUser systemUser) {
        return sysUserMapper.insert(systemUser);
    }

    @Override
    public SystemUser selectOne(String email) {
        LambdaQueryWrapper<SystemUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemUser::getEmail, email)
                .eq(SystemUser::getStatus, 1);

        return sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public List<SystemUser> selectAllUsers() {
        List<SystemUser> systemUsers = sysUserMapper.selectList(null);
        return systemUsers;
    }


    @Override
    public int update(SystemUser systemUser) {
        LambdaUpdateWrapper<SystemUser> updateWrapper = new LambdaUpdateWrapper();
        updateWrapper.eq(SystemUser::getId, systemUser.getId())
                .eq(SystemUser::getStatus, 1);

        return sysUserMapper.update(systemUser,null);
    }
}
