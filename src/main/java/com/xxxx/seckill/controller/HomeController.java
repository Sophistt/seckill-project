package com.xxxx.seckill.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "秒杀系统");
        model.addAttribute("message", "欢迎使用秒杀系统！");
        log.info("访问首页");
        return "index";
    }

    @RequestMapping("/hello")
    public String helloString(Model model) {
        model.addAttribute("name", "xxxx");
        return "hello";
    }
}