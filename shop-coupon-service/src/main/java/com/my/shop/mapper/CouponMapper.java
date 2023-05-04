package com.my.shop.mapper;

import com.my.shop.pojo.Coupon;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

/**
 * @author Gzy
 * @version 1.0
 * @Description
 * @date create on 2023/4/19
 */
@Repository
public interface CouponMapper {

    List<Coupon> findAll();

    Coupon findById(@Param("id") BigInteger id);

    Integer add(@Param("coupon")Coupon Coupon);

    Integer updateByPrimaryKey(@Param("coupon")Coupon Coupon);

    BigInteger findMaxId();

}
