package com.xxxx.seckill.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * HomeController的单元测试
 * 测试首页和hello页面的访问功能
 */
@WebMvcTest(HomeController.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testHomePageAccess() throws Exception {
        // 测试首页访问
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("title", "秒杀系统"))
                .andExpect(model().attribute("message", "欢迎使用秒杀系统！"));
    }

    @Test
    void testHomePageModelAttributes() throws Exception {
        // 单独测试Model属性设置
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("title"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("title", "秒杀系统"))
                .andExpect(model().attribute("message", "欢迎使用秒杀系统！"));
    }

    @Test
    void testHelloPageAccess() throws Exception {
        // 测试hello页面访问
        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(view().name("hello"))
                .andExpect(model().attribute("name", "xxxx"));
    }

    @Test
    void testHelloPageModelAttributes() throws Exception {
        // 单独测试hello页面的Model属性
        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("name"))
                .andExpect(model().attribute("name", "xxxx"));
    }

    @Test
    void testHomePageReturnsCorrectView() throws Exception {
        // 测试首页返回正确的视图名称
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void testHelloPageReturnsCorrectView() throws Exception {
        // 测试hello页面返回正确的视图名称
        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(view().name("hello"));
    }

    @Test
    void testMultipleHomePageRequests() throws Exception {
        // 测试多次访问首页的一致性
        for (int i = 0; i < 3; i++) {
            mockMvc.perform(get("/"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("index"))
                    .andExpect(model().attribute("title", "秒杀系统"))
                    .andExpect(model().attribute("message", "欢迎使用秒杀系统！"));
        }
    }

    @Test
    void testMultipleHelloPageRequests() throws Exception {
        // 测试多次访问hello页面的一致性
        for (int i = 0; i < 3; i++) {
            mockMvc.perform(get("/hello"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("hello"))
                    .andExpect(model().attribute("name", "xxxx"));
        }
    }
}