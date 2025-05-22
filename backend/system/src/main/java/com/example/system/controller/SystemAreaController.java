package com.example.system.controller;


import com.example.system.service.SystemAreaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 行政区域表(SystemArea)表控制层
 *
 * @author makejava
 * @since 2025-05-22 17:45:41
 */
@RestController
@RequestMapping("area")
public class SystemAreaController {
    @Resource
    private SystemAreaService systemAreaService;


}

