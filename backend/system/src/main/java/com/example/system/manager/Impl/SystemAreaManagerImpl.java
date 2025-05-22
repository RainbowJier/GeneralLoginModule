package com.example.system.manager.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.model.entity.system.SystemArea;
import com.example.system.mapper.SystemAreaMapper;
import com.example.system.service.SystemAreaService;
import org.springframework.stereotype.Service;

/**
 * 行政区域表(SystemArea)表服务实现类
 *
 * @author makejava
 * @since 2025-05-22 17:45:42
 */
@Service("systemAreaService")
public class SystemAreaManagerImpl extends ServiceImpl<SystemAreaMapper, SystemArea> implements SystemAreaService {

}

