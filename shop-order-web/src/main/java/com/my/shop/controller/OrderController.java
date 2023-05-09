package com.my.shop.controller;

import com.my.shop.common.CommonResult;
import com.my.shop.pojo.Order;
import com.my.shop.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author Gzy
 * @version 1.0
 * @Description
 * @date create on 2023/5/9
 */
@Controller
public class OrderController {
    @Resource
    private OrderService orderService;

    @PostMapping("/order/confirmOrder")
    @ResponseBody
    public CommonResult confirm(@RequestBody Order order){
        return orderService.confirmOrder(order);
    }


}
