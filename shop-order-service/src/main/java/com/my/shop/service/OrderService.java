package com.my.shop.service;

import com.my.shop.common.CommonResult;
import com.my.shop.pojo.Order;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigInteger;

/**
 * @author Gzy
 * @version 1.0
 * @Description
 * @date create on 2023/4/20
 */
public interface OrderService {

    /**
     * 确认订单
     * @param order
     * @return
     */
    CommonResult confirmOrder(Order order);

    /**
     * 删除订单
     * @return
     */
    Integer deleteOrderByPrimaryKey(BigInteger id);

}
