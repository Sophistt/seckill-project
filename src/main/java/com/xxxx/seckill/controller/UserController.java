package com.xxxx.seckill.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.xxxx.seckill.entity.User;
import com.xxxx.seckill.vo.RespBean;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Claude Code Generator
 * @since 2025-09-07
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/info")
    @ResponseBody
    public RespBean info(User user) {
        return RespBean.success("success");
    }
}
