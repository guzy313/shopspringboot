package com.my.shop.controller;

import com.my.shop.common.CommonResult;
import com.my.shop.pojo.Coupon;
import com.my.shop.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

/**
 * @author Gzy
 * @version 1.0
 * @Description
 * @date create on 2023/4/19
 */
@Controller
public class CouponController {
    @Autowired
    private CouponService couponService;

    @GetMapping("/coupon/getCouponList")
    @ResponseBody
    public CommonResult getCouponList() throws Exception{
        return new CommonResult(200,"查询成功",couponService.getCouponList());
    }

    @GetMapping("/coupon/findById")
    @ResponseBody
    Coupon findById(@RequestParam("id")BigInteger id) throws Exception{
        return couponService.findById(id);
    }

    /**
     * 使用优惠券
     * @param coupon
     */
    @PostMapping("/coupon/useCoupon")
    @ResponseBody
    CommonResult useCoupon(@RequestBody Coupon coupon){
        return couponService.useCoupon(coupon);
    }

    /**
     * 生成优惠券
     * @param coupon
     */
    @PostMapping("/coupon/create")
    @ResponseBody
    void create(@RequestBody Coupon coupon){
        couponService.create(coupon);
    }

}
