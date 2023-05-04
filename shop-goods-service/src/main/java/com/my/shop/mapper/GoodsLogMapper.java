package com.my.shop.mapper;


import com.my.shop.pojo.Goods;
import com.my.shop.pojo.GoodsLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

/**
 * @author Gzy
 * @version 1.0
 * @Description 商品日志数据操作层
 * @date create on 2023/4/21
 */
@Repository
public interface GoodsLogMapper {

    List<Goods> findAll();

    Goods findById(@Param("id") BigInteger id);

    Integer add(@Param("goodsLog") GoodsLog goodsLog);

    BigInteger findMaxId();

}
