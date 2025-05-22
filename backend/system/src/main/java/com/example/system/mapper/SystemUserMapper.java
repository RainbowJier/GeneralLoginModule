package com.example.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.common.model.entity.system.SystemUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * (SystemUser)表数据库访问层
 *
 * @author makejava
 * @since 2025-04-17 16:39:01
 */

@Mapper
public interface SystemUserMapper extends BaseMapper<SystemUser> {


}

