package com.my.shop.service;

import com.my.shop.common.CommonResult;
import com.my.shop.pojo.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Gzy
 * @version 1.0
 * @Description 订单接口
 * @date create on 2023/5/9
 */
@Service
@FeignClient(value = "shopspringboot-api-service")
public interface OrderService {

    /**
     * 确认订单
     * @param order
     * @return
     */
    @PostMapping("/order/confirmOrder")
    CommonResult confirmOrder(@RequestBody Order order);

}
