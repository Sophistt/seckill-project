package com.xxxx.seckill.mapper;

import com.xxxx.seckill.entity.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.seckill.vo.GoodsVo;

import java.util.List;

/**
* @author ubuntu
* @description 针对表【t_goods】的数据库操作Mapper
* @createDate 2025-09-27 21:53:20
* @Entity com.xxxx.seckill.entity.Goods
*/
public interface GoodsMapper extends BaseMapper<Goods> {

    /**
     * 获取商品列表
     * @return
     */
    List<GoodsVo> findGoodsVo();
}




