package com.wang.ustc.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping("testHot")
    public String test(){
        return "test";
    }
}
