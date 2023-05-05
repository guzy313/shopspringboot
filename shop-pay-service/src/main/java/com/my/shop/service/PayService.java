package com.my.shop.service;

import com.my.shop.common.CommonResult;
import com.my.shop.pojo.Payment;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Gzy
 * @version 1.0
 * @Description 支付服务
 * @date create on 2023/5/5
 */
public interface PayService {

    /**
     * 创建支付订单
     * @param payment
     */
    CommonResult createPayment(Payment payment);

    /**
     * 三方支付-回调方法
     * @param payment
     * @return
     */
    CommonResult callbackPayment(Payment payment);

}
