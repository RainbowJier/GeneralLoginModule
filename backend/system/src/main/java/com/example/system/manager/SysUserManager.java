package com.example.system.manager;

import com.example.system.model.entity.SysUser;

import java.util.List;




public interface SysUserManager {
    int insert(SysUser sysUser);

    SysUser selectList(String email);
}
