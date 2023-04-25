package com.my.shop.controller;

import com.my.shop.common.CommonResult;
import com.my.shop.pojo.Order;
import com.my.shop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Gzy
 * @version 1.0
 * @Description
 * @date create on 2023/4/20
 */
@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 确认订单
     * @param order
     * @return
     */
    @PostMapping("/order/confirmOrder")
    @ResponseBody
    CommonResult confirmOrder(@RequestParam("order") Order order){
        orderService.confirmOrder(order);
        return new CommonResult(200,"确认订单成功");
    }

}
