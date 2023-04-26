package com.my.shop.service.impl;

import com.my.shop.common.constant.ShopCode;
import com.my.shop.common.exception.CastException;
import com.my.shop.common.util.IdWorker;
import com.my.shop.mapper.GoodsLogMapper;
import com.my.shop.mapper.GoodsMapper;
import com.my.shop.pojo.Goods;
import com.my.shop.pojo.GoodsLog;
import com.my.shop.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Gzy
 * @version 1.0
 * @Description
 * @date create on 2023/4/23
 */
@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private GoodsLogMapper goodsLogMapper;

    @Override
    public Goods findById(BigInteger id) {
        return goodsMapper.findById(id);
    }

    @Override
    public void createGoods(Goods goods) {
        if(goods == null
                || goods.getGoods_name() == null
                || goods.getGoods_price() == null
                || goods.getGoods_number() == null
                || goods.getGoods_price().compareTo(BigDecimal.ZERO) < 0
                || goods.getGoods_number().compareTo(0) < 0){
            CastException.cast(ShopCode.SHOP_GOODS_ADD_FAIL);
        }
        IdWorker idWorker = new IdWorker(goodsLogMapper.findMaxId().longValue());
        goods.setId(BigInteger.valueOf(idWorker.nextId()));
        goods.setAdd_time(Timestamp.valueOf(LocalDateTime.now()));
        goodsMapper.add(goods);
        System.out.println("创建订单成功");
    }

    @Override
    public void reduceNum(GoodsLog goodsLog) {
        //判断扣减对象是否为空
        if(goodsLog == null){
            CastException.cast(ShopCode.SHOP_REDUCE_GOODS_NUM_EMPTY);
        }
        //判断必要参数是否传递
        if(goodsLog.getGoods_id() == null
        || goodsLog.getOrder_id() == null
        || goodsLog.getGoods_number() == null
        || goodsLog.getGoods_number().compareTo(0) < 0){
            CastException.cast(ShopCode.SHOP_REQUEST_PARAMETER_VALID);
        }
        //查询商品
        Goods goods = goodsMapper.findById(goodsLog.getGoods_id());
        //剩余库存商品数量
        Integer goods_number = goods.getGoods_number();
        if(goods_number.compareTo(goodsLog.getGoods_number()) < 0){
            //商品库存不足
            CastException.cast(ShopCode.SHOP_GOODS_NUM_NOT_ENOUGH);
        }else{
            //生成商品扣减日志ID
            goodsLog.setId(goodsLogMapper.findMaxId().add(BigInteger.ONE));
            goodsLog.setLog_time(new Date());

            //扣减商品库存
            goods.setGoods_number(goods_number - goodsLog.getGoods_number());
            //更新商品
            goodsMapper.updateByPrimaryKey(goods);
            //保存商品日志
            goodsLogMapper.add(goodsLog);
        }
        //输出扣减库存成功提示
        System.out.println(ShopCode.SHOP_REDUCE_GOODS_SUCCESS);
    }
}
