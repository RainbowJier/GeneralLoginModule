package com.example.frame.controller;

import com.example.frame.aop.annotation.SysLogAnno;
import com.example.frame.controller.request.LoginRequest;
import com.example.frame.controller.request.RegisterRequest;
import com.example.frame.enums.OperationType;
import com.example.frame.model.JsonData;
import com.example.frame.service.SysUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("/api/v1/account")
public class LoginController {

    @Resource
    private SysUserService sysUserService;

    @SysLogAnno(description = "注册功能",operateType = OperationType.ADD)
    @PostMapping("register")
    public JsonData register(@RequestBody RegisterRequest registerRequest) {
        return sysUserService.register(registerRequest);
    }

    @SysLogAnno(description = "登录功能",operateType = OperationType.QUERY)
    @PostMapping("login")
    public JsonData login(@RequestBody LoginRequest loginRequest) {
        return sysUserService.login(loginRequest);
    }

    @SysLogAnno(description = "根据id获取用户信息",operateType = OperationType.QUERY)
    @GetMapping("{id}")
    public JsonData getUserById(@PathVariable String id) {
        return sysUserService.getUserById(id);
    }

    @SysLogAnno(description = "分页查询获取所有用户信息",operateType = OperationType.QUERY)
    @GetMapping()
    public JsonData selectAll(@RequestParam int pageNum, @RequestParam int pageSize) {
        return sysUserService.selectAll(pageNum, pageSize);
    }


}
