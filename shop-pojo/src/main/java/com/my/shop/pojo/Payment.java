package com.my.shop.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Gzy
 * @version 1.0
 * @Description
 * @date create on 2023/4/19
 */
public class Payment implements Serializable {
    //'支付编号'
    private BigInteger id;
    //'订单编号'
    private BigInteger order_id;
    //'支付金额'
    private Integer pay_amount;
    //'是否已支付 0否 1是'
    private Integer is_paid;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public BigInteger getOrder_id() {
        return order_id;
    }

    public void setOrder_id(BigInteger order_id) {
        this.order_id = order_id;
    }

    public Integer getPay_amount() {
        return pay_amount;
    }

    public void setPay_amount(Integer pay_amount) {
        this.pay_amount = pay_amount;
    }

    public Integer getIs_paid() {
        return is_paid;
    }

    public void setIs_paid(Integer is_paid) {
        this.is_paid = is_paid;
    }
}
