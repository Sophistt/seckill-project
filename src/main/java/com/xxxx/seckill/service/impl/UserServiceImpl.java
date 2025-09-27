package com.xxxx.seckill.service.impl;

import com.xxxx.seckill.entity.User;
import com.xxxx.seckill.mapper.UserMapper;
import com.xxxx.seckill.service.IUserService;
import com.xxxx.seckill.utils.CookieUtil;
import com.xxxx.seckill.utils.MD5Util;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.xxxx.seckill.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.xxxx.seckill.vo.LoginVo;
import com.xxxx.seckill.vo.RespBean;
import com.xxxx.seckill.vo.RespBeanEnum;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * <p>
 * 用户服务实现类
 * </p>
 * <p>
 * 负责用户相关的核心业务逻辑：
 * 1. 用户登录认证和密码验证
 * 2. 基于Redis的分布式会话管理
 * 3. Cookie和Ticket的生成与验证
 * 4. 用户状态的缓存和持久化
 * </p>
 * <p>
 * 技术特点：
 * - 使用双重MD5加密确保密码安全
 * - 集成Redis实现高性能会话存储
 * - 支持分布式环境下的用户状态共享
 * </p>
 *
 * @author Claude Code Generator
 * @since 2025-09-07
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    /**
     * 用户数据访问层
     * 负责用户基础数据的CRUD操作
     */
    @Autowired
    private UserMapper userMapper;

    /**
     * Redis操作模板
     * 用于用户会话缓存和分布式存储
     */
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 用户登录认证处理
     * <p>
     * 执行完整的登录流程：
     * 1. 根据手机号查询用户信息
     * 2. 验证密码正确性（双重MD5加密）
     * 3. 生成唯一会话票据（UUID）
     * 4. 将用户信息缓存到Redis
     * 5. 设置浏览器Cookie用于后续认证
     * </p>
     *
     * @param loginVo  登录请求对象，包含手机号和密码
     * @param request  HTTP请求对象，用于获取请求信息
     * @param response HTTP响应对象，用于设置Cookie
     * @return RespBean 统一响应对象，成功时返回ticket，失败时返回错误信息
     */
    @Override
    public RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();

        // 根据手机号获取用户（手机号作为主键）
        User user = userMapper.selectById(mobile);
        if (null == user) {
            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
        }

        // 验证密码：将前端传来的密码进行二次MD5加密后与数据库中的密码比较
        // 密码加密流程：明文密码 -> 前端MD5 -> 后端MD5+盐值 -> 数据库存储
        if (!MD5Util.formPassToDBPass(password, user.getSalt()).equals(user.getPassword())) {
            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
        }

        // 生成用户会话票据（全局唯一标识）
        String ticket = UUIDUtil.uuid();

        // 将用户信息存储到Redis缓存，key格式：user:ticket
        // 这样可以实现分布式环境下的会话共享
        redisTemplate.opsForValue().set("user:" + ticket, user);

        // 设置客户端Cookie，存储票据用于后续请求认证
        CookieUtil.setCookie(request, response, "userTicket", ticket);

        return RespBean.success(ticket);
    }

    /**
     * 根据Cookie中的票据获取用户信息
     * <p>
     * 用于用户状态维持和身份认证：
     * 1. 验证票据有效性（非空检查）
     * 2. 从Redis缓存中获取用户信息
     * 3. 刷新Cookie有效期（活跃用户延长会话）
     * 4. 返回用户对象用于后续业务处理
     * </p>
     * <p>
     * 注意事项：
     * - 如果票据无效或过期，返回null
     * - 成功获取用户时会自动刷新Cookie延长会话
     * - 适用于需要用户登录状态的所有接口
     * </p>
     *
     * @param userTicket 用户会话票据，从Cookie中获取
     * @param request    HTTP请求对象，用于获取请求信息
     * @param response   HTTP响应对象，用于刷新Cookie
     * @return User 用户对象，如果票据无效则返回null
     */
    public User getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response) {
        // 票据为空时直接返回null，避免无效的Redis查询
        if (null == userTicket) {
            return null;
        }

        // 从Redis缓存中根据票据获取用户信息
        // key格式：user:ticket，与登录时的存储格式保持一致
        User user = (User) redisTemplate.opsForValue().get("user:" + userTicket);

        // 如果用户信息存在，刷新Cookie有效期
        // 这样可以为活跃用户自动延长会话时间
        if (user != null) {
            CookieUtil.setCookie(request, response, "userTicket", userTicket);
        }

        return user;
    }
}
