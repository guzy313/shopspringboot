package com.my.shop.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Gzy
 * @version 1.0
 * @Description 商品消息对象
 * @date create on 2023/4/28
 */
public class MQShopMessageDto implements Serializable {
    private BigInteger orderId;
    private BigInteger couponId;
    private BigInteger goodsId;
    private Integer goodsNumber;
    private BigInteger payId;
    private BigInteger userId;
    private BigDecimal useMoney;


    public BigInteger getOrderId() {
        return orderId;
    }

    public void setOrderId(BigInteger orderId) {
        this.orderId = orderId;
    }

    public BigInteger getCouponId() {
        return couponId;
    }

    public void setCouponId(BigInteger couponId) {
        this.couponId = couponId;
    }

    public BigInteger getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(BigInteger goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getGoodsNumber() {
        return goodsNumber;
    }

    public void setGoodsNumber(Integer goodsNumber) {
        this.goodsNumber = goodsNumber;
    }

    public BigInteger getPayId() {
        return payId;
    }

    public void setPayId(BigInteger payId) {
        this.payId = payId;
    }

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    public BigDecimal getUseMoney() {
        return useMoney;
    }

    public void setUseMoney(BigDecimal useMoney) {
        this.useMoney = useMoney;
    }

    @Override
    public String toString() {
        return "MQShopMessageDto{" +
                "orderId=" + orderId +
                ", couponId=" + couponId +
                ", goodsId=" + goodsId +
                ", goodsNumber=" + goodsNumber +
                ", payId=" + payId +
                ", userId=" + userId +
                ", useMoney=" + useMoney +
                '}';
    }
}
