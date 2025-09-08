package com.xxxx.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.seckill.entity.User;

import com.xxxx.seckill.vo.LoginVo;
import com.xxxx.seckill.vo.RespBean;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Claude Code Generator
 * @since 2025-09-07
 */
public interface IUserService extends IService<User> {
    RespBean doLogin(LoginVo loginVo);
}
