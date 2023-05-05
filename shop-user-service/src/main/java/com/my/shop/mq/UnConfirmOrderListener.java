package com.my.shop.mq;

import com.alibaba.fastjson.JSON;
import com.my.shop.common.constant.MQMessageConstant;
import com.my.shop.common.constant.ShopCode;
import com.my.shop.common.dto.MQShopMessageDto;
import com.my.shop.common.util.UUIDWorker;
import com.my.shop.mapper.MqMessageConsumerLogMapper;
import com.my.shop.pojo.MqMessageConsumerLog;
import com.my.shop.pojo.UserBalanceLog;
import com.my.shop.service.UserService;
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
 * @Description 用户服务-消息订阅 确认订单失败回退(回退用户余额)
 * @date create on 2023/5/4
 */
@RocketMQMessageListener(consumerGroup = "${rocketmq.consumer.group}",
        topic = MQMessageConstant.TOPIC,
        consumeMode = ConsumeMode.CONCURRENTLY,
        messageModel = MessageModel.BROADCASTING,
        selectorType = SelectorType.TAG,
        selectorExpression = MQMessageConstant.TAG_UN_CONFIRM_ORDER)
@Component
public class UnConfirmOrderListener implements RocketMQListener<MessageExt> {
    @Value("${rocketmq.consumer.group}")
    private String consumerGroup;
    @Resource
    private UserService userService;
    @Resource
    private MqMessageConsumerLogMapper mqMessageConsumerLogMapper;

    @Override
    public void onMessage(MessageExt messageExt) {
        String topic = messageExt.getTopic();
        String tags = messageExt.getTags();
        String keys = messageExt.getKeys();
        String body = new String(messageExt.getBody());
        MQShopMessageDto mqShopMessageDto = (MQShopMessageDto)JSON.parseObject(body,MQShopMessageDto.class);

        //构建用户余额日志对象
        UserBalanceLog userBalanceLog = new UserBalanceLog();
        userBalanceLog.setUse_money(mqShopMessageDto.getUseMoney());
        userBalanceLog.setUser_id(mqShopMessageDto.getUserId());
        userBalanceLog.setOrder_id(mqShopMessageDto.getOrderId());
        userBalanceLog.setMoney_log_type(ShopCode.SHOP_USER_MONEY_REFUND.getCode());
        userBalanceLog.setCreate_time(Timestamp.valueOf(LocalDateTime.now()));

        //构建消息日志查询对象
        MqMessageConsumerLog mqMessageConsumerLogCondition = new MqMessageConsumerLog();
        mqMessageConsumerLogCondition.setMsg_topic(topic);
        mqMessageConsumerLogCondition.setGroup_name(consumerGroup);
        mqMessageConsumerLogCondition.setMsg_tag(tags);
        mqMessageConsumerLogCondition.setMsg_key(keys);
        MqMessageConsumerLog mqMessageConsumerLog = mqMessageConsumerLogMapper.findByParams(mqMessageConsumerLogCondition);
        //判断是否已经消费过
        if(mqMessageConsumerLog != null){
            //已经消费过的情况
            //已经消费成功的情况
            if(ShopCode.SHOP_MQ_MESSAGE_STATUS_SUCCESS.getCode().intValue() == mqMessageConsumerLog.getConsumer_status().intValue()){
                //已经消费成功
                System.out.println("当前消息已经消费成功,无需重复消费");
                return;
            }
            //正在处理中
            if(ShopCode.SHOP_MQ_MESSAGE_STATUS_PROCESSING.getCode().intValue() == mqMessageConsumerLog.getConsumer_status().intValue()){
                //正在处理情况
                System.out.println("当前消息正在处理");
                return;
            }
            //消费失败情况
            if(ShopCode.SHOP_MQ_MESSAGE_STATUS_FAIL.getCode().intValue() == mqMessageConsumerLog.getConsumer_status().intValue()){
                //消息消费失败
                //失败小于3次,重新进行消费
                if(mqMessageConsumerLog.getConsumer_times().intValue() < 3){
                    mqMessageConsumerLog.setConsumer_status(ShopCode.SHOP_MQ_MESSAGE_STATUS_PROCESSING.getCode());
                    mqMessageConsumerLog.setConsumer_time(Timestamp.valueOf(LocalDateTime.now()));
                    Integer logEffectRows = mqMessageConsumerLogMapper.updateByPrimaryKeyWithLock(mqMessageConsumerLog);
                    if(logEffectRows <= 0){
                        System.out.println("并发修改,稍后处理");
                    }else{
                        mqMessageConsumerLog = mqMessageConsumerLogMapper.findByParams(mqMessageConsumerLogCondition);
                        Integer effectUserRows = userService.unUseUserBalance(userBalanceLog);
                        if(effectUserRows.intValue() > 0){
                            System.out.println("回退用户余额成功");
                            mqMessageConsumerLog.setConsumer_status(ShopCode.SHOP_MQ_MESSAGE_STATUS_SUCCESS.getCode());
                        }else {
                            System.out.println("回退用户余额失败");
                            mqMessageConsumerLog.setConsumer_status(ShopCode.SHOP_MQ_MESSAGE_STATUS_FAIL.getCode());
                        }
                        //更新用户余额回退结果
                        mqMessageConsumerLogMapper.updateByPrimaryKey(mqMessageConsumerLog);
                    }

                }else{
                    System.out.println("消费失败超过3次,请手动处理");
                    return;
                }
            }
        }else {
            //未进行过消费的情况
            //消费消息日志记录
            mqMessageConsumerLog = new MqMessageConsumerLog();
            mqMessageConsumerLog.setGroup_name(consumerGroup);
            mqMessageConsumerLog.setMsg_tag(tags);
            mqMessageConsumerLog.setMsg_key(keys);
            mqMessageConsumerLog.setConsumer_status(ShopCode.SHOP_MQ_MESSAGE_STATUS_PROCESSING.getCode());
            mqMessageConsumerLog.setConsumer_times(0);
            mqMessageConsumerLog.setMsg_body(body);
            mqMessageConsumerLog.setId(UUIDWorker.getUUIDFormat());
            //开始回退商品库存[真正业务],并记录商品回退日志
            Integer unReduceNum = userService.unUseUserBalance(userBalanceLog);
            if(unReduceNum.intValue() > 0){
                mqMessageConsumerLog.setConsumer_status(ShopCode.SHOP_MQ_MESSAGE_STATUS_SUCCESS.getCode());
                System.out.println("用户余额回退成功");
            }else{
                mqMessageConsumerLog.setConsumer_status(ShopCode.SHOP_MQ_MESSAGE_STATUS_FAIL.getCode());
                System.out.println("用户余额回退失败");
            }
            Integer addLogs = mqMessageConsumerLogMapper.add(mqMessageConsumerLog);
        }

    }
}
