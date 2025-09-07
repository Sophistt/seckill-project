package com.xxxx.seckill.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/login")
@Slf4j
public class LoginController {
    
    /**
     * 跳转登录页面
     *
     * @param
     * @return java.lang.String
     **/
    @RequestMapping("/toLogin")
    public String toLogin() {
        return "login";
    }
}
