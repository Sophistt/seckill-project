package com.xxxx.seckill.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

import org.springframework.lang.NonNull;

/**
 * Spring MVC Web配置类
 *
 * 这个配置类是整个用户认证架构的关键组件，它负责：
 * 1. 注册自定义的参数解析器到Spring MVC框架
 * 2. 使UserArgumentResolver在整个应用中生效
 * 3. 实现了依赖注入的解耦设计
 *
 * 设计理念：
 * - 通过WebMvcConfigurer接口扩展Spring MVC功能，而不是覆盖默认配置
 * - 采用组合而非继承的方式，保持配置的灵活性
 * - 集中管理所有Web层面的自定义配置
 *
 * 这种架构模式被称为"配置分离原则"：
 * 将框架配置与业务逻辑分离，提高代码的可维护性和可测试性。
 *
 * @author Linus
 * @since 2025-09-17
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private UserArgumentResolver userArgumentResolver;

    /**
     * 注册自定义的方法参数解析器
     *
     * 这个方法将UserArgumentResolver添加到Spring MVC的参数解析器链中。
     * 当请求到达控制器时，Spring会按顺序遍历所有解析器，
     * 找到支持当前参数类型的解析器来处理参数注入。
     *
     * 为什么用List而不是单个注册？
     * - 支持注册多个解析器
     * - 保持扩展性，未来可以添加其他自定义解析器
     * - 遵循Spring的设计模式
     *
     * @param resolvers Spring MVC的参数解析器列表
     */
    @Override
    public void addArgumentResolvers(@NonNull List<HandlerMethodArgumentResolver> resolvers) {
        // 将用户参数解析器添加到解析器链中
        // 这使得所有控制器方法都可以自动注入User参数
        resolvers.add(userArgumentResolver);
    }
}
