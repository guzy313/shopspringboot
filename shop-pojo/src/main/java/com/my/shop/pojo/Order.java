package com.my.shop.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;

/**
 * @author Gzy
 * @version 1.0
 * @Description 订单表
 * @date create on 2023/4/19
 */
public class Order implements Serializable {
    //主键
    private BigInteger id;
    //'用户id'
    private BigInteger user_id;
    //'订单状态 0未确认 1已确认 2已取消 3无效 4退款'
    private Integer order_status;
    //'支付状态 0未支付 1支付中 2已支付'
    private Integer pay_status;
    //'发货状态 0未发货 1已发货 2已收货 3已退货'
    private Integer shipping_status;
    //'收货地址'
    private String address;
    //'收货人'
    private String consignee;
    //'商品ID'
    private BigInteger goods_id;
    //'商品数量'
    private Integer goods_number;
    //'商品价格'
    private BigDecimal goods_price;
    //'商品总价'
    private BigDecimal goods_amount;
    //'运费'
    private BigDecimal shipping_fee;
    //'订单总价'
    private BigDecimal order_amount;
    //'优惠券ID'
    private BigInteger coupon_id;
    //'已付金额'
    private BigDecimal money_paid;
    //'支付金额'
    private BigDecimal pay_amount;
    //'创建时间'
    private Timestamp add_time;
    //'订单确认时间'
    private Timestamp confirm_time;
    //'支付时间'
    private Timestamp pay_time;


    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public BigInteger getUser_id() {
        return user_id;
    }

    public void setUser_id(BigInteger user_id) {
        this.user_id = user_id;
    }

    public Integer getOrder_status() {
        return order_status;
    }

    public void setOrder_status(Integer order_status) {
        this.order_status = order_status;
    }

    public Integer getPay_status() {
        return pay_status;
    }

    public void setPay_status(Integer pay_status) {
        this.pay_status = pay_status;
    }

    public Integer getShipping_status() {
        return shipping_status;
    }

    public void setShipping_status(Integer shipping_status) {
        this.shipping_status = shipping_status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public BigInteger getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(BigInteger goods_id) {
        this.goods_id = goods_id;
    }

    public Integer getGoods_number() {
        return goods_number;
    }

    public void setGoods_number(Integer goods_number) {
        this.goods_number = goods_number;
    }

    public BigDecimal getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(BigDecimal goods_price) {
        this.goods_price = goods_price;
    }

    public BigDecimal getGoods_amount() {
        return goods_amount;
    }

    public void setGoods_amount(BigDecimal goods_amount) {
        this.goods_amount = goods_amount;
    }

    public BigDecimal getShipping_fee() {
        return shipping_fee;
    }

    public void setShipping_fee(BigDecimal shipping_fee) {
        this.shipping_fee = shipping_fee;
    }

    public BigDecimal getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(BigDecimal order_amount) {
        this.order_amount = order_amount;
    }

    public BigInteger getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(BigInteger coupon_id) {
        this.coupon_id = coupon_id;
    }

    public BigDecimal getMoney_paid() {
        return money_paid;
    }

    public void setMoney_paid(BigDecimal money_paid) {
        this.money_paid = money_paid;
    }

    public BigDecimal getPay_amount() {
        return pay_amount;
    }

    public void setPay_amount(BigDecimal pay_amount) {
        this.pay_amount = pay_amount;
    }

    public Timestamp getAdd_time() {
        return add_time;
    }

    public void setAdd_time(Timestamp add_time) {
        this.add_time = add_time;
    }

    public Timestamp getConfirm_time() {
        return confirm_time;
    }

    public void setConfirm_time(Timestamp confirm_time) {
        this.confirm_time = confirm_time;
    }

    public Timestamp getPay_time() {
        return pay_time;
    }

    public void setPay_time(Timestamp pay_time) {
        this.pay_time = pay_time;
    }
}
