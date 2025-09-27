package com.xxxx.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.seckill.entity.Order;
import com.xxxx.seckill.service.IOrderService;
import com.xxxx.seckill.mapper.OrderMapper;
import org.springframework.stereotype.Service;

/**
* @author ubuntu
* @description 针对表【t_order】的数据库操作Service实现
* @createDate 2025-09-27 21:55:23
*/
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
    implements IOrderService {

}




