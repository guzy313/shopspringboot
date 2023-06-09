package com.my.shop.service;

import com.my.shop.pojo.Goods;
import com.my.shop.pojo.GoodsLog;

import java.math.BigInteger;

/**
 * @author Gzy
 * @version 1.0
 * @Description
 * @date create on 2023/4/20
 */
public interface GoodsService {

    Goods findById(BigInteger id);

    void createGoods(Goods goods);

    void reduceNum(GoodsLog goodsLog);

    Integer unReduceNum(GoodsLog goodsLog);


}
