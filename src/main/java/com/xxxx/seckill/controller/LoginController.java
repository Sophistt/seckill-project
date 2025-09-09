package com.xxxx.seckill.controller;

import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xxxx.seckill.vo.RespBean;
import com.xxxx.seckill.service.IUserService;
import com.xxxx.seckill.vo.LoginVo;

@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {

    @Autowired
    @Qualifier("userServiceImpl")
    private IUserService userService;
    
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

    @RequestMapping("/doLogin")
    @ResponseBody
    public RespBean doLogin(@Valid LoginVo loginVo) {
        log.info("{}", loginVo);
        return userService.doLogin(loginVo);
    }
}
