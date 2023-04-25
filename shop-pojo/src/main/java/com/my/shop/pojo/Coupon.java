package com.my.shop.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;

/**
 * @author Gzy
 * @version 1.0
 * @Description 优惠券 类
 * @date create on 2023/4/19
 */
public class Coupon implements Serializable {
    //优惠券id
    private BigInteger id;
    //优惠券价格
    private BigDecimal coupon_price;
    //用户id
    private BigInteger user_id;
    //订单id
    private BigInteger order_id;
    //是否使用 0未使用 1已使用
    private Integer is_used;
    //使用时间
    private Timestamp used_time;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public BigDecimal getCoupon_price() {
        return coupon_price;
    }

    public void setCoupon_price(BigDecimal coupon_price) {
        this.coupon_price = coupon_price;
    }

    public BigInteger getUser_id() {
        return user_id;
    }

    public void setUser_id(BigInteger user_id) {
        this.user_id = user_id;
    }

    public BigInteger getOrder_id() {
        return order_id;
    }

    public void setOrder_id(BigInteger order_id) {
        this.order_id = order_id;
    }

    public Integer getIs_used() {
        return is_used;
    }

    public void setIs_used(Integer is_used) {
        this.is_used = is_used;
    }

    public Timestamp getUsed_time() {
        return used_time;
    }

    public void setUsed_time(Timestamp used_time) {
        this.used_time = used_time;
    }
}
