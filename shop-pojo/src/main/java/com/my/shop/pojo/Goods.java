package com.my.shop.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;

/**
 * @author Gzy
 * @version 1.0
 * @Description 商品类
 * @date create on 2023/4/19
 */
public class Goods implements Serializable {
    //'商品id'
    private BigInteger id;
    //'商品名称'
    private String goods_name;
    //'商品库存'
    private Integer goods_number;
    //'商品价格'
    private BigDecimal goods_price;
    //'商品描述'
    private String goods_desc;
    //'添加时间'
    private Timestamp add_time;


    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
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

    public String getGoods_desc() {
        return goods_desc;
    }

    public void setGoods_desc(String goods_desc) {
        this.goods_desc = goods_desc;
    }

    public Timestamp getAdd_time() {
        return add_time;
    }

    public void setAdd_time(Timestamp add_time) {
        this.add_time = add_time;
    }
}
