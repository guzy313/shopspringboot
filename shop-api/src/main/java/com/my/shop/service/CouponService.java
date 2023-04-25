package com.my.shop.service;

import com.my.shop.common.CommonResult;
import com.my.shop.pojo.Coupon;
import feign.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigInteger;
import java.util.List;

/**
 * @author Gzy
 * @version 1.0
 * @Description
 * @date create on 2023/4/19
 */
@FeignClient(value = "shopspringboot-coupon-service")
@Service
public interface CouponService {

    @GetMapping("/coupon/getCouponList")
    List<Coupon> getCouponList();

    @GetMapping("/coupon/findById")
    Coupon findById(@Param("id") BigInteger id);

    @PostMapping("/coupon/useCoupon")
    CommonResult useCoupon(@Param("coupon") Coupon coupon);

    @PostMapping("/coupon/create")
    void create(@Param("coupon") Coupon coupon);

}
