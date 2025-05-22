package com.example.system.manager.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.common.model.entity.system.SystemUser;
import com.example.system.manager.SystemUserManager;
import com.example.system.mapper.SystemUserMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;


@Component
public class SystemUserManagerImpl implements SystemUserManager {

    @Resource
    private SystemUserMapper systemUserMapper;


    @Override
    public int insert(SystemUser systemUser) {
        return systemUserMapper.insert(systemUser);
    }

    @Override
    public SystemUser selectOne(String email) {
        LambdaQueryWrapper<SystemUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemUser::getEmail, email)
                .eq(SystemUser::getStatus, 1);

        return systemUserMapper.selectOne(queryWrapper);
    }

    @Override
    public List<SystemUser> selectAllUsers() {
        List<SystemUser> systemUsers = systemUserMapper.selectList(null);
        return systemUsers;
    }


    @Override
    public int update(SystemUser systemUser) {
        LambdaUpdateWrapper<SystemUser> updateWrapper = new LambdaUpdateWrapper();
        updateWrapper.eq(SystemUser::getId, systemUser.getId())
                .eq(SystemUser::getStatus, 1);

        return systemUserMapper.update(systemUser,null);
    }
}
