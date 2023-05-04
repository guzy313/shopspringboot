package com.my.shop.mq;

import com.alibaba.fastjson.JSON;
import com.my.shop.common.constant.MQMessageConstant;
import com.my.shop.common.constant.ShopCode;
import com.my.shop.common.dto.MQShopMessageDto;
import com.my.shop.common.util.IdWorker;
import com.my.shop.common.util.UUIDWorker;
import com.my.shop.mapper.MqMessageConsumerLogMapper;
import com.my.shop.pojo.MqMessageConsumerLog;
import com.my.shop.service.CouponService;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author Gzy
 * @version 1.0
 * @Description 优惠券处理确认订单失败 MQ消息监听 (回退优惠券)
 * @date create on 2023/4/28
 */
@Component
@RocketMQMessageListener(
        consumerGroup = "${rocketmq.consumer.group}",
        topic = MQMessageConstant.TOPIC,
        selectorType = SelectorType.TAG,
        selectorExpression = MQMessageConstant.TAG_UN_CONFIRM_ORDER,
        messageModel = MessageModel.BROADCASTING)
public class UnConfirmOrderListener implements RocketMQListener<MessageExt> {
    @Value("${rocketmq.consumer.group}")
    private String consumerGroup;

    @Resource
    private MqMessageConsumerLogMapper mqMessageConsumerLogMapper;

    @Resource
    private CouponService couponService;

    @Override
    public void onMessage(MessageExt messageExt) {
        String topic = messageExt.getTopic();
        String msgId = messageExt.getMsgId();
        String tags = messageExt.getTags();
        String keys = messageExt.getKeys();
       try {
           String body = new String(messageExt.getBody(), "UTF-8");
           MQShopMessageDto mqShopMessageDto = (MQShopMessageDto)JSON.parse(body);

           //消息消费日志
           MqMessageConsumerLog mqMessageConsumerLogCondition = new MqMessageConsumerLog();
           mqMessageConsumerLogCondition.setGroup_name(consumerGroup);
           mqMessageConsumerLogCondition.setMsg_tag(tags);
           mqMessageConsumerLogCondition.setMsg_key(keys);
           MqMessageConsumerLog mqMessageConsumerLogFind = mqMessageConsumerLogMapper.findByParams(mqMessageConsumerLogCondition);
           if(mqMessageConsumerLogFind !=null){
               //判断是否消费过
               //如果消费过
               Integer consumer_status = mqMessageConsumerLogFind.getConsumer_status();
               if(ShopCode.SHOP_MQ_MESSAGE_STATUS_SUCCESS.getCode().intValue() ==
                       consumer_status.intValue()){
                   System.out.println("当前消息已消费,msgId:" + msgId);
                   return;
               }
               if(ShopCode.SHOP_MQ_MESSAGE_STATUS_PROCESSING.getCode().intValue() ==
                       consumer_status.intValue()){
                   System.out.println("当前消息正在处理,msgId:" + msgId);
                   return;
               }
               if(ShopCode.SHOP_MQ_MESSAGE_STATUS_FAIL.getCode().intValue() ==
               consumer_status.intValue()){
                   //消息处理失败情况
                   if(mqMessageConsumerLogFind.getConsumer_times().intValue() < 3){

                       //处理失败次数小于3次,再次尝试处理
                       mqMessageConsumerLogFind.setConsumer_status(ShopCode.SHOP_MQ_MESSAGE_STATUS_PROCESSING.getCode());
                       mqMessageConsumerLogFind.setConsumer_time(Timestamp.valueOf(LocalDateTime.now()));

                       //更新日志记录
                       Integer effectRows = mqMessageConsumerLogMapper.updateByPrimaryKeyWithLock(mqMessageConsumerLogFind);
                       if(effectRows <= 0){
                           System.out.println("并发修改,稍后处理");
                       }else{
                           //此处重新查询更新之后的消息日志信息
                           mqMessageConsumerLogFind = mqMessageConsumerLogMapper.findByParams(mqMessageConsumerLogCondition);
                           //开始回退消费券[真正业务]
                           Integer unUseCouponRows = couponService.unUseCoupon(mqShopMessageDto.getCouponId());
                           if(unUseCouponRows > 0){
                               System.out.println("回退优惠券成功");
                               mqMessageConsumerLogFind.setConsumer_status(ShopCode.SHOP_MQ_MESSAGE_STATUS_SUCCESS.getCode());
                           }else {
                               System.out.println("回退优惠券失败");
                               mqMessageConsumerLogFind.setConsumer_status(ShopCode.SHOP_MQ_MESSAGE_STATUS_FAIL.getCode());
                           }
                           //此处更新业务处理结果 的 日志状态[不更新次数只更新状态,因此不用考虑重复执行问题]
                           mqMessageConsumerLogMapper.updateByPrimaryKey(mqMessageConsumerLogFind);
                       }
                   }else {
                       return;
                   }
               }


           }else {
               //消息未被消费过
               //设置消费者消息日志信息
               MqMessageConsumerLog mqMessageConsumerLog = new MqMessageConsumerLog();
               mqMessageConsumerLog.setGroup_name(consumerGroup);
               mqMessageConsumerLog.setMsg_topic(topic);
               mqMessageConsumerLog.setMsg_tag(tags);
               mqMessageConsumerLog.setConsumer_times(0);
               mqMessageConsumerLog.setMsg_key(keys);
               mqMessageConsumerLog.setConsumer_status(ShopCode.SHOP_MQ_MESSAGE_STATUS_PROCESSING.getCode());
               mqMessageConsumerLog.setConsumer_time(Timestamp.valueOf(LocalDateTime.now()));
               mqMessageConsumerLog.setId(UUIDWorker.getUUIDFormat());

               //开始回退消费券[真正业务]
               Integer effectRows = couponService.unUseCoupon(mqShopMessageDto.getCouponId());
               if(effectRows > 0){
                   System.out.println("回退优惠券成功");
                   mqMessageConsumerLog.setConsumer_status(ShopCode.SHOP_MQ_MESSAGE_STATUS_SUCCESS.getCode());
               }else{
                   //回退失败情况
                   System.out.println("回退优惠券失败");
                   mqMessageConsumerLog.setConsumer_status(ShopCode.SHOP_MQ_MESSAGE_STATUS_FAIL.getCode());
                   mqMessageConsumerLog.setConsumer_times(1);
               }
               //保存消息日志
               Integer addLogs = mqMessageConsumerLogMapper.add(mqMessageConsumerLog);
           }
       }catch (Exception e){
            e.printStackTrace();
       }
    }

}
