package com.example.common.model.entity.system;

import java.util.ArrayList;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 行政区域表(SystemArea)表实体类
 *
 * @author Frank
 * @since 2025-05-22 17:45:42
 */


@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SystemArea implements Serializable {
    /**
     * 主键
     */
    private String id;
    /**
     * 名称
     */
    private String name;
    /**
     * 父节点id
     */
    private String pid;
    /**
     * 0-无效，1-有效
     */
    private Integer isValid;
    /**
     * 0-省份，1-市，2-区/县，3-乡镇，4-村
     */
    private Integer grade;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 精度
     */
    private Double lng;

    /**
     * 纬度
     */
    private Double lat;
}

