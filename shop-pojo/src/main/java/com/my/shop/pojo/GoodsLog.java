package com.my.shop.pojo;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

/**
 * @author Gzy
 * @version 1.0
 * @Description 订单日志 类
 * @date create on 2023/4/19
 */
public class GoodsLog implements Serializable {
    //'主键'
    private Integer id;
    //'商品ID'
    private BigInteger goods_id;
    //'订单ID'
    private BigInteger order_id;
    //'商品数量'
    private Integer goods_number;
    //'记录时间'
    private Date log_time;

    public GoodsLog() {
    }

    public GoodsLog(BigInteger goods_id, BigInteger order_id, Integer goods_number) {
        this.goods_id = goods_id;
        this.order_id = order_id;
        this.goods_number = goods_number;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigInteger getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(BigInteger goods_id) {
        this.goods_id = goods_id;
    }

    public BigInteger getOrder_id() {
        return order_id;
    }

    public void setOrder_id(BigInteger order_id) {
        this.order_id = order_id;
    }

    public Integer getGoods_number() {
        return goods_number;
    }

    public void setGoods_number(Integer goods_number) {
        this.goods_number = goods_number;
    }

    public Date getLog_time() {
        return log_time;
    }

    public void setLog_time(Date log_time) {
        this.log_time = log_time;
    }
}
