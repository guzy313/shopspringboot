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
    public Integer useCoupon(Coupon coupon) {
        if(ShopCode.SHOP_COUPON_ISUSED.getCode().intValue() == coupon.getIs_used().intValue() ){
            CastException.cast(ShopCode.SHOP_COUPON_ISUSED);
        }
        coupon.setIs_used(ShopCode.SHOP_COUPON_ISUSED.getCode());
        coupon.setUsed_time(Timestamp.valueOf(LocalDateTime.now()));
        Integer effectRows = couponMapper.updateByPrimaryKey(coupon);
        return effectRows;
    }

    @Override
    public Integer unUseCoupon(BigInteger id) {
        Coupon coupon = couponMapper.findById(id);
        if(coupon == null){
            CastException.cast(ShopCode.SHOP_COUPON_NO_EXIST);
        }
        //回退消费券
        coupon.setOrder_id(null);
        coupon.setIs_used(ShopCode.SHOP_COUPON_UNUSED.getCode());
        coupon.setUsed_time(null);
        Integer effectRows = couponMapper.updateByPrimaryKey(coupon);
        return effectRows;
    }

    @Override
    public Integer create(Coupon coupon) {
        if(coupon == null
                ||coupon.getCoupon_price() == null
                ||coupon.getCoupon_price().compareTo(BigDecimal.ZERO) < 0
                || coupon.getUser_id() == null){
            CastException.cast(ShopCode.SHOP_COUPON_INVALIED);
        }
        IdWorker idWorker = new IdWorker(couponMapper.findAll().size() + 1);
        coupon.setId(BigInteger.valueOf(idWorker.nextId()));
        coupon.setIs_used(ShopCode.SHOP_COUPON_UNUSED.getCode());
        Integer add = couponMapper.add(coupon);
        System.out.println("创建优惠券成功");
        return add;
    }
}
