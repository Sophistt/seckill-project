package com.xxxx.seckill.config;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.xxxx.seckill.entity.User;
import com.xxxx.seckill.service.IUserService;
import com.xxxx.seckill.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户参数自动解析器
 *
 * 实现Spring MVC的HandlerMethodArgumentResolver接口，用于自动解析控制器方法中的User参数。
 * 这是一个关键的架构组件，它实现了以下功能：
 *
 * 1. 统一用户认证逻辑：避免在每个控制器方法中重复编写Cookie解析和用户验证代码
 * 2. 参数注入：自动将已认证的User对象注入到控制器方法参数中
 * 3. 安全性提升：集中处理用户票据验证，减少安全漏洞的可能性
 * 4. 代码简化：让控制器专注于业务逻辑而不是认证细节
 *
 * 工作原理：
 * 当Spring MVC处理请求时，会检查控制器方法的参数类型。如果发现User类型的参数，
 * 就会调用这个解析器从Cookie中提取用户票据，验证后返回User对象。
 *
 * @author Linus
 * @since 2025-09-17
 */
@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    @Qualifier("userServiceImpl")
    private IUserService userService;

    /**
     * 判断是否支持解析此参数
     *
     * 这个方法告诉Spring MVC：当控制器方法参数类型是User时，使用这个解析器来处理。
     * 这种设计实现了类型安全的参数注入，避免了硬编码的参数名匹配。
     *
     * @param parameter 方法参数的元数据信息
     * @return true 如果参数类型是User类，false 其他情况
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> clazz = parameter.getParameterType();
        // 只有当参数类型严格等于User.class时才支持解析
        // 这种精确匹配确保了类型安全，避免意外解析其他类型
        return clazz == User.class;
    }

    /**
     * 解析参数，将User对象注入到控制器方法中
     *
     * 这是参数解析的核心方法，执行以下步骤：
     * 1. 从HTTP请求Cookie中提取用户票据(userTicket)
     * 2. 验证票据的有效性
     * 3. 通过用户服务获取完整的用户信息
     * 4. 返回User对象或null（如果认证失败）
     *
     * 这种设计的优势：
     * - 集中处理认证逻辑，确保一致性
     * - 失败时返回null而不抛异常，让控制器可以优雅处理未认证情况
     * - 支持Redis session管理，提升扩展性
     *
     * @param parameter      方法参数元数据
     * @param mavContainer   模型和视图容器
     * @param webRequest     Web请求包装器
     * @param binderFactory  数据绑定器工厂
     * @return User对象（认证成功）或null（认证失败）
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {

        // 获取原生的HTTP请求和响应对象
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpServletResponse response = (HttpServletResponse) webRequest.getNativeResponse();

        // 从Cookie中提取用户票据
        // 使用"userTicket"作为Cookie名称，这是系统的认证标准
        String ticket = CookieUtil.getCookieValue(request, "userTicket");
        if (StringUtils.isEmpty(ticket)) {
            // 没有票据说明用户未登录，返回null让控制器处理
            return null;
        }

        // 通过用户服务验证票据并获取用户信息
        // 这里支持Redis session管理，确保分布式环境下的session一致性
        return userService.getUserByCookie(ticket, request, response);
    }
}
