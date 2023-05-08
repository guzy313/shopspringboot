package com.my.shop.mq;

import com.alibaba.fastjson.JSON;
import com.my.shop.common.constant.MQMessageConstant;
import com.my.shop.common.constant.ShopCode;
import com.my.shop.common.dto.MQShopMessageDto;
import com.my.shop.common.exception.CastException;
import com.my.shop.common.util.UUIDWorker;
import com.my.shop.mapper.MqMessageConsumerLogMapper;
import com.my.shop.mapper.OrderMapper;
import com.my.shop.pojo.MqMessageConsumerLog;
import com.my.shop.pojo.Order;
import com.my.shop.pojo.Payment;
import com.my.shop.service.OrderService;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @author Gzy
 * @version 1.0
 * @Description order message consumer
 * @date create on 2023/5/8
 */
@RocketMQMessageListener(consumerGroup = "${rocketmq.consumer.group}",
        topic = MQMessageConstant.TOPIC,
        selectorType = SelectorType.TAG,
        selectorExpression = MQMessageConstant.TAG_UN_CONFIRM_ORDER +
                "||" + MQMessageConstant.TAG_PAYMENT_SUCCESS,
        messageModel = MessageModel.BROADCASTING,
        consumeMode = ConsumeMode.CONCURRENTLY)
@Component
public class MessageConsumer  implements RocketMQListener<MessageExt> {

    @Value("${rocketmq.consumer.group}")
    private String consumerGroup;

    @Resource
    private MqMessageConsumerLogMapper mqMessageConsumerLogMapper;

    @Resource
    private OrderService orderService;

    @Resource
    private OrderMapper orderMapper;

    @Override
    public void onMessage(MessageExt messageExt) {
        String topic = messageExt.getTopic();
        String tags = messageExt.getTags();
        String keys = messageExt.getKeys();
        String msgId = messageExt.getMsgId();
        String body = new String(messageExt.getBody());
        if(MQMessageConstant.TAG_UN_CONFIRM_ORDER.equals(tags)){
            this.UnConfirmOrder(topic,tags,keys,msgId,body);
        }
        if(MQMessageConstant.TAG_PAYMENT_SUCCESS.equals(tags)){
            this.paySuccess(topic,tags,keys,msgId,body);
        }

    }


    private void paySuccess(String topic,String tags,String keys,String msgId,String body){
        Payment messageBody = JSON.parseObject(body,Payment.class);
        System.out.println("开始消费 付款成功回调 消息");

        //根据订单ID查询订单对象
        Order order = orderMapper.findById(messageBody.getOrder_id());
        if(order != null){
            //订单存在
            //更改订单状态为已支付
            order.setPay_status(ShopCode.SHOP_ORDER_PAY_STATUS_IS_PAY.getCode());
            order.setPay_time(Timestamp.valueOf(LocalDateTime.now()));
            //更新订单数据到数据库
            Integer effectOrderRows = orderMapper.updateByPrimaryKey(order);
            if(effectOrderRows > 0){
                System.out.println("更改订单状态为已支付");
            }
        }else{
            //订单不存在
            CastException.cast(ShopCode.SHOP_ORDER_INVALID);
        }



    }



    /**
     * 业务处理
     * 订单服务-消息订阅 确认订单失败回退(删除订单)
     * @param topic
     * @param tags
     * @param keys
     * @param msgId
     * @param body
     */
    private void UnConfirmOrder(String topic,String tags,String keys,String msgId,String body){
        MQShopMessageDto mqShopMessageDto = (MQShopMessageDto) JSON.parseObject(body,MQShopMessageDto.class);

        //消息消费日志查询条件
        MqMessageConsumerLog mqMessageConsumerLogCondition = new MqMessageConsumerLog();
        mqMessageConsumerLogCondition.setGroup_name(consumerGroup);
        mqMessageConsumerLogCondition.setMsg_topic(topic);
        mqMessageConsumerLogCondition.setMsg_key(keys);
        mqMessageConsumerLogCondition.setMsg_tag(tags);

        MqMessageConsumerLog mqMessageConsumerLog = mqMessageConsumerLogMapper.findByParams(mqMessageConsumerLogCondition);
        if(mqMessageConsumerLog != null){
            //已经消费过的情况
            //消费成功情况
            if(ShopCode.SHOP_MQ_MESSAGE_STATUS_SUCCESS.getCode().intValue() == mqMessageConsumerLog.getConsumer_status().intValue()){
                System.out.println("当前消息已经消费成功,无需重复消费");
                return;
            }
            //正在消费情况
            if(ShopCode.SHOP_MQ_MESSAGE_STATUS_PROCESSING.getCode().intValue() == mqMessageConsumerLog.getConsumer_status().intValue()){
                //正在处理情况
                System.out.println("当前消息正在处理");
                return;
            }
            //消费失败情况
            if(ShopCode.SHOP_MQ_MESSAGE_STATUS_FAIL.getCode().intValue() == mqMessageConsumerLog.getConsumer_status().intValue()){
                //小于3次情况,再次尝试消费
                if(mqMessageConsumerLog.getConsumer_times().intValue() < 3){

                }else{
                    System.out.println("消费失败超过3次,请手动处理");
                    return;
                }
            }
        }else{
            //未进行消费的情况
            //构建消费消息日志对象
            mqMessageConsumerLog = new MqMessageConsumerLog();
            mqMessageConsumerLog.setGroup_name(consumerGroup);
            mqMessageConsumerLog.setMsg_topic(topic);
            mqMessageConsumerLog.setMsg_tag(tags);
            mqMessageConsumerLog.setMsg_key(keys);
            mqMessageConsumerLog.setMsg_body(body);
            mqMessageConsumerLog.setConsumer_times(0);
            mqMessageConsumerLog.setId(UUIDWorker.getUUIDFormat());
            mqMessageConsumerLog.setConsumer_time(Timestamp.valueOf(LocalDateTime.now()));

            //开始删除订单[真正业务]
            Integer effectOrderRows = orderService.deleteOrderByPrimaryKey(mqShopMessageDto.getOrderId());
            if(effectOrderRows > 0){
                //回退(删除)订单成功
                System.out.println("订单回退成功");
                mqMessageConsumerLog.setConsumer_status(ShopCode.SHOP_MQ_MESSAGE_STATUS_SUCCESS.getCode());
            }else {
                //回退(删除订单失败)
                System.out.println("订单回退失败");
                mqMessageConsumerLog.setConsumer_status(ShopCode.SHOP_MQ_MESSAGE_STATUS_FAIL.getCode());
                mqMessageConsumerLog.setConsumer_times(1);
            }
            Integer addLogs = mqMessageConsumerLogMapper.add(mqMessageConsumerLog);

        }


    }



}
