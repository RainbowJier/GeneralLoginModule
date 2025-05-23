package com.example.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.common.model.entity.system.SystemArea;
import com.example.common.model.entity.system.SystemAreaTree;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 行政区域表(SystemArea)表数据库访问层
 *
 * @author makejava
 * @since 2025-05-22 17:45:41
 */
@Mapper
public interface SystemAreaMapper extends BaseMapper<SystemArea> {

    List<SystemArea> getAreaTree();

    List<SystemArea> getAreaTreeNodeById(String id);
}

