package com.example.common.model.entity.system;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SystemAreaTree implements Serializable {
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

    @TableField(exist = false)
    private List<SystemAreaTree> children = new ArrayList<>();


    public static List<SystemAreaTree> buildAreaTree(List<SystemArea> areaList) {
        // 1. SystemArea -> SystemAreaTree
        Map<String, SystemAreaTree> treeNodeMap = areaList.stream().map(area -> {
            SystemAreaTree node = new SystemAreaTree();
            node.setId(area.getId());
            node.setName(area.getName());
            node.setPid(area.getPid());
            node.setIsValid(area.getIsValid());
            node.setGrade(area.getGrade());
            return node;
        }).collect(Collectors.toMap(SystemAreaTree::getId, node -> node));

        List<SystemAreaTree> rootList = new ArrayList<>();

        // 2. 构建树形结构
        for (SystemAreaTree node : treeNodeMap.values()) {
            if (node.getPid() == null || "0".equals(node.getPid())) {
                // 根节点
                rootList.add(node);
            } else {
                SystemAreaTree parent = treeNodeMap.get(node.getPid());
                if (parent != null) {
                    parent.getChildren().add(node);
                } else {
                    // 没找到父节点也可以作为根节点返回
                    rootList.add(node);
                }
            }
        }
        return rootList;
    }

}
