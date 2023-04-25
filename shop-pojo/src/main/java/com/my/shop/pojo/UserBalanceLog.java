package com.my.shop.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;

/**
 * @author Gzy
 * @version 1.0
 * @Description 用户余额日志 类
 * @date create on 2023/4/19
 */
public class UserBalanceLog implements Serializable {
    //'用户id'
    private BigInteger user_id;
    //'订单id'
    private BigInteger order_id;
    //'日志类型 1订单付款 2订单退款'
    private Integer money_log_type;
    //'操作金额'
    private BigDecimal use_money;
    //'日志时间'
    private Timestamp create_time;

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

    public Integer getMoney_log_type() {
        return money_log_type;
    }

    public void setMoney_log_type(Integer money_log_type) {
        this.money_log_type = money_log_type;
    }

    public BigDecimal getUse_money() {
        return use_money;
    }

    public void setUse_money(BigDecimal use_money) {
        this.use_money = use_money;
    }

    public Timestamp getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Timestamp create_time) {
        this.create_time = create_time;
    }
}
