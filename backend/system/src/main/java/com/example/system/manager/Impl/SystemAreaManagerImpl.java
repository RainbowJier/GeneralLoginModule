package com.example.system.manager.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.model.entity.system.SystemArea;
import com.example.common.model.entity.system.SystemAreaTree;
import com.example.system.manager.SystemAreaManager;
import com.example.system.mapper.SystemAreaMapper;
import com.example.system.service.SystemAreaService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 行政区域表(SystemArea)表服务实现类
 *
 * @author makejava
 * @since 2025-05-22 17:45:42
 */
@Component
public class SystemAreaManagerImpl implements SystemAreaManager {

    @Resource
    private SystemAreaMapper systemAreaMapper;

    @Override
    public List<SystemArea> getAreaTree() {
        return systemAreaMapper.getAreaTree();
    }

    @Override
    public List<SystemArea> getAreaTreeNodeById(String id) {
        return systemAreaMapper.getAreaTreeNodeById(id);
    }
}

