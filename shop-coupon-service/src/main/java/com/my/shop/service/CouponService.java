package com.my.shop.service;

import com.my.shop.pojo.Coupon;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.util.List;

/**
 * @author Gzy
 * @version 1.0
 * @Description
 * @date create on 2023/4/19
 */
public interface CouponService {

    List<Coupon> getCouponList() throws Exception;

    Coupon findById(BigInteger id) throws Exception;

    void useCoupon(Coupon coupon);

    void create(Coupon coupon);

}
