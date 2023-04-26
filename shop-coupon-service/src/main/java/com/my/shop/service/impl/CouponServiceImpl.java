package com.my.shop.service.impl;

import com.my.shop.common.constant.ShopCode;
import com.my.shop.common.exception.CastException;
import com.my.shop.common.util.IdWorker;
import com.my.shop.mapper.CouponMapper;
import com.my.shop.pojo.Coupon;
import com.my.shop.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Gzy
 * @version 1.0
 * @Description
 * @date create on 2023/4/19
 */
@Service
public class CouponServiceImpl implements CouponService {
    @Autowired
    private CouponMapper couponMapper;


    @Override
    public List<Coupon> getCouponList() throws Exception {
        return couponMapper.findAll();
    }

    @Override
    public Coupon findById(BigInteger id) throws Exception {
        return couponMapper.findById(id);
    }

    @Override
    public void useCoupon(Coupon coupon) {
        if(ShopCode.SHOP_COUPON_ISUSED.getCode().intValue() == coupon.getIs_used().intValue() ){
            CastException.cast(ShopCode.SHOP_COUPON_ISUSED);
        }
        coupon.setIs_used(ShopCode.SHOP_COUPON_ISUSED.getCode());
        coupon.setUsed_time(Timestamp.valueOf(LocalDateTime.now()));
        couponMapper.updateByPrimaryKey(coupon);
    }

    @Override
    public void create(Coupon coupon) {
        if(coupon == null
                ||coupon.getCoupon_price() == null
                ||coupon.getCoupon_price().compareTo(BigDecimal.ZERO) < 0 ){
            CastException.cast(ShopCode.SHOP_COUPON_INVALIED);
        }
        IdWorker idWorker = new IdWorker(couponMapper.findAll().size() + 1);
        coupon.setId(BigInteger.valueOf(idWorker.nextId()));
        coupon.setIs_used(ShopCode.SHOP_COUPON_UNUSED.getCode());
        couponMapper.add(coupon);
        System.out.println("创建优惠券成功");
    }
}
