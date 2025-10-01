package com.xxxx.seckill.controller;

import com.xxxx.seckill.entity.Goods;
import com.xxxx.seckill.entity.User;
import com.xxxx.seckill.service.IGoodsService;
import com.xxxx.seckill.service.IUserService;
import com.xxxx.seckill.vo.GoodsVo;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 商品控制器
 *
 * 本控制器已重构为使用自动参数解析模式，展示了现代Spring MVC的最佳实践。
 *
 * 重构前后对比：
 * 【重构前】：每个方法都需要手动处理Cookie解析和用户验证
 * 【重构后】：通过UserArgumentResolver自动注入User对象
 *
 * 这种重构的优势：
 * 1. 代码简洁：控制器方法减少了20-30行重复代码
 * 2. 关注分离：控制器专注于业务逻辑，认证逻辑集中处理
 * 3. 一致性：所有需要用户认证的方法使用统一的解析机制
 * 4. 可维护性：认证逻辑修改时只需要更改一处代码
 * 5. 可测试性：可以轻松模拟User参数进行单元测试
 *
 * @author Linus
 * @since 2025-09-17
 */
@Controller
@RequestMapping("/goods")
@Slf4j
public class GoodsController {

    @Autowired
    @Qualifier("userServiceImpl")
    private IUserService userService;

    @Autowired
    @Qualifier("goodsServiceImpl")
    private IGoodsService goodsService;

    /**
     * 跳转到商品列表页面
     *
     * 这种设计模式在高并发的秒杀系统中特别重要：
     * - 减少重复代码，降低出错概率
     * - 统一认证机制，便于安全策略调整
     * - 提高开发效率，让开发者专注于业务逻辑
     *
     * @param model 视图模型，用于传递数据到前端页面
     * @param user  当前登录用户（由UserArgumentResolver自动注入）
     * @return 视图名称，跳转到商品列表页面
     */
    @RequestMapping("/toList")
    public String toList(Model model, User user) {
        // User对象已经由参数解析器自动注入，直接使用即可
        // 如果用户未登录，user会是null，可以在这里处理重定向到登录页面
        if (user == null) {
            return "login";
        }

        // 将用户信息传递给视图
        model.addAttribute("user", user);
        model.addAttribute("goodsList", goodsService.findGoodsVo());
        return "goodsList";
    }

    @RequestMapping("/toDetail/{goodsId}")
    public String toDetail(Model model, User user, @PathVariable Long goodsId) {
        if (user == null) {
            return "login";
        }
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
        log.info("goods = " + goods);
        model.addAttribute("user", user);
        model.addAttribute("goods", goods);
        return "goodsDetail";
    }
}   
