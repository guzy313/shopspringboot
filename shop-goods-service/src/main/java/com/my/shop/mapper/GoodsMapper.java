package com.my.shop.mapper;


import com.my.shop.pojo.Goods;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

/**
 * @author Gzy
 * @version 1.0
 * @Description 商品数据操作层
 * @date create on 2023/4/21
 */
@Repository
public interface GoodsMapper {

    Goods findById(@Param("id") BigInteger id);

    void add(@Param("goods")Goods goods);

    void updateByPrimaryKey(@Param("goods")Goods goods);

    BigInteger findMaxId();

}
