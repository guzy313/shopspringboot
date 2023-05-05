package com.my.shop.common.constant;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author Gzy
 * @version 1.0
 * @Description MQ消息常量类
 * @date create on 2023/4/27
 */
public class MQMessageConstant {

    //主题-整个系统的通用
    public final static String TOPIC = "ShopTopic";

    //标签-确认订单失败回退(删除订单) 发送消息
    public final static String TAG_UN_CONFIRM_ORDER = "unConfirmOrder";

    //标签-订单支付成功 发送消息
    public final static String TAG_PAYMENT_SUCCESS = "paySuccess";



}
