package com.my.shop.producer;

import com.my.shop.common.CommonResult;
import com.my.shop.common.constant.MQMessageBack;
import com.my.shop.common.constant.MQMessageConstant;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

/**
 * @author Gzy
 * @version 1.0
 * @Description 订单消息生产者
 * @date create on 2023/4/27
 */
@Component
public class OrderProducer {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 确认订单失败回退(删除订单) 发送消息
     */
    public CommonResult sendUnConfirmOrderMessage(BigInteger orderId){
        SendResult sendResult = rocketMQTemplate.syncSend(MQMessageConstant.TOPIC + ":" + MQMessageConstant.TAG_UN_CONFIRM_ORDER, orderId);
        SendStatus sendStatus = sendResult.getSendStatus();
        if(SendStatus.SEND_OK.compareTo(sendStatus) != 0){
            System.out.println(MQMessageBack.UN_CONFIRM_ORDER);
            return new CommonResult(MQMessageBack.UN_CONFIRM_ORDER.getCode(),MQMessageBack.UN_CONFIRM_ORDER.getMessage());
        }else {
            return new CommonResult(MQMessageBack.SUCCESS.getCode(),MQMessageBack.SUCCESS.getMessage());
        }
    }



}
