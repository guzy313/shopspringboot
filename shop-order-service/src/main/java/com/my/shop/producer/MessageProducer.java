package com.my.shop.producer;

import com.my.shop.common.CommonResult;
import com.my.shop.common.constant.MQMessageBack;
import com.my.shop.common.dto.MQShopMessageDto;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

/**
 * @author Gzy
 * @version 1.0
 * @Description MQ消息发送类
 * @date create on 2023/4/27
 */
@Component
public class MessageProducer {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 异步发送广播消息
     * @param body
     * @param topic
     * @param tag
     * @param key
     * @return
     */
    public CommonResult asyncSendBroadcast(String body,String topic,String tag,String key){
        Message message = new Message(topic,tag,key,body.getBytes());
        try {
            SendResult sendResult = rocketMQTemplate.getProducer().send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new CommonResult(MQMessageBack.SUCCESS.getCode(), MQMessageBack.SUCCESS.getMessage());
    }

}
