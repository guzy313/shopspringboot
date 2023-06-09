package com.my.shop.mapper;

import com.my.shop.pojo.Order;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

/**
 * @author Gzy
 * @version 1.0
 * @Description
 * @date create on 2023/4/20
 */
@Repository
public interface OrderMapper {

    List<Order> findAll();

    Order findById(@Param("id")BigInteger id);

    Integer add(@Param("order")Order order);

    BigInteger findMaxId();

    Integer updateByPrimaryKey(@Param("order")Order order);

    Integer deleteByPrimaryKey(@Param("id")BigInteger id);


}
