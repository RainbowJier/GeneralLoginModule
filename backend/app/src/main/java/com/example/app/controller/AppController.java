package com.example.app.controller;

import com.example.common.util.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * app
 */
@Slf4j
@RestController
@RequestMapping("/app")
public class AppController {

    @GetMapping("test")
    public JsonData helloApp( ) {
        return JsonData.buildSuccess("hello world");
    }

}
