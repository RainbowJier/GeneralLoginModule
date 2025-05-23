package com.example.system.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.model.entity.system.SystemArea;
import com.example.common.model.entity.system.SystemAreaTree;

import java.util.List;

/**
 * 行政区域表(SystemArea)表服务接口
 *
 * @author makejava
 * @since 2025-05-22 17:45:42
 */
public interface SystemAreaManager{

    List<SystemArea> getAreaTree();

    List<SystemArea> getAreaTreeNodeById(String id);
}

