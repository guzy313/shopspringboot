package com.my.shop.service.impl;

import com.alibaba.fastjson.JSON;
import com.my.shop.common.CommonResult;
import com.my.shop.common.constant.MQMessageConstant;
import com.my.shop.common.constant.ShopCode;
import com.my.shop.common.dto.MQShopMessageDto;
import com.my.shop.common.exception.CastException;
import com.my.shop.common.util.IdWorker;
import com.my.shop.mapper.OrderMapper;
import com.my.shop.pojo.*;
import com.my.shop.producer.MessageProducer;
import com.my.shop.producer.OrderProducer;
import com.my.shop.service.CouponService;
import com.my.shop.service.GoodsService;
import com.my.shop.service.OrderService;
import com.my.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @author Gzy
 * @version 1.0
 * @Description
 * @date create on 2023/4/20
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private CouponService couponService;

    @Autowired
    private MessageProducer messageProducer;

    @Override
    public CommonResult confirmOrder(Order order) {
        //1.校验订单
        this.checkOrder(order);
        //2.生成预订单
        this.saveAndGeneratePreOrder(order);

        try {
            //3.扣减库存
            this.reduceGoodsNum(order);
            //4.扣减优惠券
            this.useCoupon(order);
            //5.使用余额
            if(order.getMoney_paid().compareTo(BigDecimal.ZERO) > 0){
                //判断是否使用余额
                this.useUserBalance(order);
            }
            //6.确认订单(将订单更新为可见)
            this.updateOrderStatus(order);
           //7.返回成功状态
            return new CommonResult(ShopCode.SHOP_ORDER_CONFIRM.getCode(),ShopCode.SHOP_ORDER_CONFIRM.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            //1.确认订单失败,发送消息
            //订单
            BigInteger orderId = order.getId();
            //优惠券
            BigInteger coupon_id = order.getCoupon_id();
            //商品库存
            BigInteger goods_id = order.getGoods_id();
            //商品日志
            //用户余额
            BigInteger user_id = order.getUser_id();
            //用户余额日志
            MQShopMessageDto mqShopMessageDto = new MQShopMessageDto();
            mqShopMessageDto.setCouponId(coupon_id);
            mqShopMessageDto.setOrderId(orderId);
            mqShopMessageDto.setUserId(user_id);
            mqShopMessageDto.setGoodsId(goods_id);
            mqShopMessageDto.setGoodsNumber(order.getGoods_number());
            mqShopMessageDto.setUseMoney(order.getMoney_paid());
            String body = JSON.toJSONString(mqShopMessageDto);
            messageProducer.asyncSendBroadcast(body,
                    MQMessageConstant.TOPIC,
                    MQMessageConstant.TAG_UN_CONFIRM_ORDER,mqShopMessageDto.getOrderId().toString());
            //2.返回失败状态
            System.out.println("订单失败,开始进行数据回退");
            return new CommonResult(ShopCode.SHOP_ORDER_CONFIRM_FAIL.getCode(),ShopCode.SHOP_ORDER_CONFIRM_FAIL.getMessage());
        }
    }

    /**
     * 校验订单
     * @param order
     */
    private void checkOrder(Order order){
        //订单是否为空
        if(order == null){
            CastException.cast(ShopCode.SHOP_ORDER_INVALID);
        }
        //商品是否存在
        Goods goods = goodsService.findById(order.getGoods_id());
        if(goods == null){
            CastException.cast(ShopCode.SHOP_GOODS_NO_EXIST);
        }
        //订单内的用户是否存在
        User user = userService.getUserById(order.getUser_id());
        if(user == null){
            CastException.cast(ShopCode.SHOP_USER_NO_EXIST);
        }
        //订单价格是否合法
        if(BigDecimal.valueOf(0).compareTo(order.getOrder_amount()) > 0 ){
            CastException.cast(ShopCode.SHOP_ORDER_AMOUNT_INVALID);
        }
        //购买商品数量是否合法
        if(order.getGoods_number() <= 0){
            CastException.cast(ShopCode.SHOP_GOODS_NUM_NOT_ENOUGH);
        }
    }

    /**
     * 生成预订单:
     * (设置订单状态不可见--订单未确认,
     * 设置订单ID,
     * 核算运费是否正常,
     * 核算订单总价是否正确,
     * 是否使用用户余额,
     * 是否使用优惠券，
     * 核算总价，
     * 设置订单时间，
     * 保存订单到数据库)
     */
    private Order saveAndGeneratePreOrder(Order order){
        //设置订单状态不可见--订单未确认
        order.setOrder_status(ShopCode.SHOP_ORDER_NO_CONFIRM.getCode());
        //设置订单ID
        IdWorker idWorker = new IdWorker(orderMapper.findAll().size());
        order.setId(BigInteger.valueOf(idWorker.nextId()));
        //核算运费是否正常
        BigDecimal shippingFee = this.calculateShippingFee(order.getOrder_amount());
        if(order.getShipping_fee().compareTo(shippingFee) != 0){
            //比较当前订单的运费和实际正确的运费
            CastException.cast(ShopCode.SHOP_ORDER_SHIPPINGFEE_INVALID);
        }
        //核算订单总价是否正确(商品单价*商品数量+运费)
        if(order.getGoods_price().multiply(BigDecimal.valueOf(order.getGoods_number()).add(shippingFee))
                .compareTo(order.getGoods_amount()) != 0){
            CastException.cast(ShopCode.SHOP_ORDER_AMOUNT_INVALID);
        }
        //是否使用余额
        BigDecimal money_paid = order.getMoney_paid();
        if(money_paid != null){
            //余额小于0
            if(money_paid.compareTo(BigDecimal.ZERO) < 0){
                CastException.cast(ShopCode.SHOP_MONEY_PAID_LESS_ZERO);
            }
            //余额大于0
            if(money_paid.compareTo(BigDecimal.ZERO) > 0){
               //与数据库中用户的余额进行比较
                if(money_paid.compareTo(userService.getUserById(order.getUser_id()).getUser_money()) > 0) {
                    //订单要使用的余额大于用户余额,非法
                    CastException.cast(ShopCode.SHOP_MONEY_PAID_INVALID);
                }
            }
        }else{
            order.setMoney_paid(BigDecimal.ZERO);
        }


        //是否使用优惠券-->是——>判断优惠券是否合法(是否已使用,暂不考虑是否已过期)
        Coupon coupon = null;
        if(order.getCoupon_id() != null){
            coupon = couponService.findById(order.getCoupon_id());
            //查询优惠券是否存在
            if(coupon == null){
                CastException.cast(ShopCode.SHOP_COUPON_NO_EXIST);
            }
            //判断优惠券是否已使用
            if(coupon.getIs_used().compareTo(ShopCode.SHOP_COUPON_ISUSED.getCode()) == 0){
                CastException.cast(ShopCode.SHOP_COUPON_ISUSED);
            }
        }

        //核算总价（减去当前订单所用的用户的余额，减去优惠券价格）
        if(coupon == null){
            //不用优惠券情况
            BigDecimal payAmount = order.getOrder_amount()
                    .subtract(money_paid);
            order.setPay_amount(payAmount);
        }else{
            BigDecimal payAmount = order.getOrder_amount()
                    .subtract(money_paid)
                    .subtract(coupon.getCoupon_price());
            //如果扣除余额和优惠券之后小于0,则重新设置扣除用户的余额为(商品总价 - 优惠券价格)
            if(payAmount.compareTo(BigDecimal.ZERO) < 0){
                order.setMoney_paid(order.getGoods_amount().subtract(coupon.getCoupon_price()));
                order.setPay_amount(BigDecimal.ZERO);
            }else{
                order.setPay_amount(payAmount);
            }

        }

        //设置订单时间
        order.setAdd_time(Timestamp.valueOf(LocalDateTime.now()));
        //保存订单到数据库
        orderMapper.add(order);
        return order;
    }

    /**
     * 计算运费
     * @param orderAmount
     * @return
     */
    private BigDecimal calculateShippingFee(BigDecimal orderAmount){
        //如果商品总价大于100,免订单费用,否则需要5块钱运费
        if(orderAmount.compareTo(BigDecimal.valueOf(100)) >= 0){
            return BigDecimal.ZERO;
        }else{
            return BigDecimal.valueOf(5);
        }
    }

    /**
     * 扣减库存
     */
    private void reduceGoodsNum(Order order){
        //传入订单日志类(订单ID,商品ID,商品数量)
        GoodsLog goodsLog = new GoodsLog(order.getGoods_id(),order.getId(),order.getGoods_number());
        CommonResult result = goodsService.reduceNum(goodsLog);
        if(ShopCode.SHOP_REDUCE_GOODS_SUCCESS.getCode().compareTo(Integer.valueOf(result.getCode())) != 0){
            //扣减库存失败情况
            CastException.cast(ShopCode.SHOP_REDUCE_GOODS_NUM_FAIL);
        }
        System.out.println("扣减库存成功");
    }

    /**
     * 使用优惠券
     * @param order
     */
    private void useCoupon(Order order){
        //优惠券校验在生成预订单的时候已经校验
        Coupon coupon = couponService.findById(order.getCoupon_id());
        //设置优惠券使用的订单ID
        coupon.setOrder_id(order.getId());
        //用户ID在发放优惠券时候需要指定
        CommonResult result = couponService.useCoupon(coupon);
        if(ShopCode.SHOP_COUPON_USE_SUCCESS.getCode().compareTo(result.getCode()) != 0){
            CastException.cast(ShopCode.SHOP_COUPON_USE_FAIL);
        }
        System.out.println("使用优惠券成功");
    }

    /**
     * 使用用户余额
     */
    private void useUserBalance(Order order){
        userService.useUserBalance(order);
    }

    private void updateOrderStatus(Order order){
        order.setPay_status(ShopCode.SHOP_ORDER_PAY_STATUS_NO_PAY.getCode());
        order.setOrder_status(ShopCode.SHOP_ORDER_CONFIRM.getCode());
        order.setConfirm_time(Timestamp.valueOf(LocalDateTime.now()));
        orderMapper.updateByPrimaryKey(order);
        System.out.println("订单:"+order.getId()+"更新成功");
    }

}
