package com.my.shop.service;

import com.my.shop.common.CommonResult;
import com.my.shop.pojo.Payment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Gzy
 * @version 1.0
 * @Description 支付服务
 * @date create on 2023/5/5
 */
@Service
@FeignClient(value = "shopspringboot-pay-service")
public interface PayService {

    @PostMapping("/pay/createPayment")
    CommonResult createPayment(@RequestBody Payment payment);


}
