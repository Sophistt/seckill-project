package com.xxxx.seckill.service;

import com.xxxx.seckill.entity.Goods;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.seckill.vo.GoodsVo;

/**
* @author ubuntu
* @description 针对表【t_goods】的数据库操作Service
* @createDate 2025-09-27 21:53:20
*/
public interface IGoodsService extends IService<Goods> {

    /**
     * 获取商品列表
     * @return
     */
    List<GoodsVo> findGoodsVo();
}
