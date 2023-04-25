package com.my.shop.mapper;

import com.my.shop.pojo.Order;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

/**
 * @author Gzy
 * @version 1.0
 * @Description
 * @date create on 2023/4/20
 */
@Repository
public interface OrderMapper {

    Order findById(@Param("id")BigInteger id);

    void add(@Param("order")Order order);

    BigInteger findMaxId();

    void updateByPrimaryKey(@Param("order")Order order);


}
