package com.example.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.model.entity.system.SystemArea;
import com.example.common.model.entity.system.SystemAreaTree;
import com.example.system.manager.Impl.SystemAreaManagerImpl;
import com.example.system.manager.SystemAreaManager;
import com.example.system.mapper.SystemAreaMapper;
import com.example.system.service.SystemAreaService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 行政区域表(SystemArea)表服务实现类
 *
 * @author makejava
 * @since 2025-05-22 17:45:42
 */
@Service
public class SystemAreaServiceImpl implements SystemAreaService {

    @Resource
    private SystemAreaManager systemAreaManager;

    @Override
    public List<SystemAreaTree> getAreaTree() {
        List<SystemArea> list = systemAreaManager.getAreaTree();
        return SystemAreaTree.buildAreaTree(list);
    }

    @Override
    public List<SystemAreaTree> getAreaTreeNodeById(String id) {
        List<SystemArea> list = systemAreaManager.getAreaTreeNodeById(id);
        return SystemAreaTree.buildAreaTree(list);
    }


}

