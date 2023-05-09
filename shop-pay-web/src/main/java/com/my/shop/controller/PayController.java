package com.my.shop.controller;

import com.my.shop.common.CommonResult;
import com.my.shop.pojo.Payment;
import com.my.shop.service.PayService;
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
public class PayController {
    @Resource
    private PayService payService;

    /**
     * 创建支付订单
     * @param payment
     */
    @PostMapping("/pay/createPayment")
    @ResponseBody
    CommonResult createPayment(@RequestBody Payment payment){
        return payService.createPayment(payment);
    }


    /**
     * 三方支付-回调方法
     * @param payment
     * @return
     */
    @PostMapping("/pay/callbackPayment")
    @ResponseBody
    CommonResult callbackPayment(@RequestBody Payment payment){
        return payService.callbackPayment(payment);
    }

}
