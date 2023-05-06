package com.my.shop.mq;

import com.my.shop.common.CommonResult;
import com.my.shop.common.constant.ShopCode;
import com.my.shop.common.exception.CastException;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Gzy
 * @version 1.0
 * @Description
 * @date create on 2023/5/5
 */
@Component
public class MessageProducer {
    @Resource
    private RocketMQTemplate rocketMQTemplate;

    public CommonResult asyncSendBroadcast(String topic,String tag,String key,String body){
        if(StringUtils.isEmpty(topic)){
            CastException.cast(ShopCode.SHOP_MQ_TOPIC_IS_EMPTY);
        }
        if(StringUtils.isEmpty(body)){
            CastException.cast(ShopCode.SHOP_MQ_MESSAGE_BODY_IS_EMPTY);
        }
        Message message = new Message(topic,tag,key,body.getBytes());
        try {
            SendResult sendResult = rocketMQTemplate.getProducer().send(message);
            if(SendStatus.SEND_OK.compareTo(sendResult.getSendStatus()) == 0){
                //发送成功情况
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(ShopCode.SHOP_MQ_SEND_MESSAGE_FAIL.getCode(),
                    ShopCode.SHOP_MQ_SEND_MESSAGE_FAIL.getMessage());
        }
        return new CommonResult(ShopCode.SHOP_MQ_SEND_MESSAGE_SUCCESS.getCode(),
                ShopCode.SHOP_MQ_SEND_MESSAGE_SUCCESS.getMessage());
    }




}
