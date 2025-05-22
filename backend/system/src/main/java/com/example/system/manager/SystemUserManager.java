package com.example.system.manager;

import com.example.common.model.entity.system.SystemUser;

import java.util.List;


public interface SystemUserManager {
    int insert(SystemUser systemUser);

    SystemUser selectOne(String email);

    List<SystemUser> selectAllUsers();

    int update(SystemUser systemUser);
}
