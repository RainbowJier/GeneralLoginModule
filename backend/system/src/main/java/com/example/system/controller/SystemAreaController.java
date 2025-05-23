package com.example.system.controller;


import com.example.common.util.JsonData;
import com.example.system.service.SystemAreaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 行政区域表(SystemArea)
 *
 * @author Liuqijie
 * @since 2025-05-22 17:45:41
 */
@RestController
@RequestMapping("/area")
public class SystemAreaController {
    @Resource
    private SystemAreaService systemAreaService;

    /**
     * 获取行政区划树
     */
    @GetMapping("/tree")
    public JsonData getAreaTree() {
        return JsonData.buildSuccess(systemAreaService.getAreaTree());
    }


    /**
     * 获取行政区划树指定节点
     */
    @GetMapping("/treeById")
    public JsonData getAreaTree(String id) {
        return JsonData.buildSuccess(systemAreaService.getAreaTreeNodeById(id));
    }

}

