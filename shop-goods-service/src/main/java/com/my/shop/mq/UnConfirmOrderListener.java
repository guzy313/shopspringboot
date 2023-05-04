package com.my.shop.mq;

import com.alibaba.fastjson.JSON;
import com.my.shop.common.constant.MQMessageConstant;
import com.my.shop.common.constant.ShopCode;
import com.my.shop.common.dto.MQShopMessageDto;
import com.my.shop.common.util.UUIDWorker;
import com.my.shop.mapper.GoodsMapper;
import com.my.shop.mapper.MqMessageConsumerLogMapper;
import com.my.shop.pojo.Goods;
import com.my.shop.pojo.GoodsLog;
import com.my.shop.pojo.MqMessageConsumerLog;
import com.my.shop.service.GoodsService;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @author Gzy
 * @version 1.0
 * @Description 商品服务-消息订阅 确认订单失败回退
 * @date create on 2023/5/4
 */
@RocketMQMessageListener(consumerGroup = "${rocketmq.consumer.group}",
        topic = MQMessageConstant.TOPIC,
        selectorType = SelectorType.TAG,
        selectorExpression = MQMessageConstant.TAG_UN_CONFIRM_ORDER,
        consumeMode = ConsumeMode.CONCURRENTLY,
        messageModel = MessageModel.BROADCASTING //广播模式
)
@Component
public class UnConfirmOrderListener implements RocketMQListener<MessageExt> {
    @Value("${rocketmq.consumer.group}")
    private String consumerGroup;

    @Resource
    private GoodsService goodsService;

    @Resource
    private GoodsMapper goodsMapper;

    @Resource
    private MqMessageConsumerLogMapper mqMessageConsumerLogMapper;

    @Override
    public void onMessage(MessageExt messageExt) {
        String topic = messageExt.getTopic();
        String tags = messageExt.getTags();
        String msgId = messageExt.getMsgId();
        String keys = messageExt.getKeys();

        try {
            String messageBody = new String(messageExt.getBody(), "UTF-8");
            MQShopMessageDto mqShopMessageDto = (MQShopMessageDto)JSON.parse(messageBody);
            //查询消息消费日志的查询条件对象
            MqMessageConsumerLog mqMessageConsumerLogCondition = new MqMessageConsumerLog();
            mqMessageConsumerLogCondition.setGroup_name(consumerGroup);
            mqMessageConsumerLogCondition.setMsg_topic(topic);
            mqMessageConsumerLogCondition.setMsg_key(keys);
            mqMessageConsumerLogCondition.setMsg_tag(tags);

            //创建商品日志对象 goodsLog
            GoodsLog goodsLog = new GoodsLog();
            //此处为回退,所以设置负值
            goodsLog.setGoods_number(- mqShopMessageDto.getGoodsNumber().intValue());
            goodsLog.setGoods_id(mqShopMessageDto.getGoodsId());
            goodsLog.setOrder_id(mqShopMessageDto.getOrderId());

            MqMessageConsumerLog mqMessageConsumerLog = mqMessageConsumerLogMapper.findByParams(mqMessageConsumerLogCondition);
            if(mqMessageConsumerLog != null){
                //已经消费过的情况
                if(ShopCode.SHOP_MQ_MESSAGE_STATUS_SUCCESS.getCode().intValue() == mqMessageConsumerLog.getConsumer_status().intValue()){
                    //已经消费成功
                    System.out.println("当前消息已经消费成功,无需重复消费");
                    return;
                }
                if(ShopCode.SHOP_MQ_MESSAGE_STATUS_PROCESSING.getCode().intValue() == mqMessageConsumerLog.getConsumer_status().intValue()){
                    //正在处理情况
                    System.out.println("当前消息正在处理");
                    return;
                }
                if(ShopCode.SHOP_MQ_MESSAGE_STATUS_FAIL.getCode().intValue() == mqMessageConsumerLog.getConsumer_status().intValue()){
                    //消费失败情况
                    //消费次数小于3次
                    if(mqMessageConsumerLog.getConsumer_times().intValue() < 3){
                        mqMessageConsumerLog.setConsumer_time(Timestamp.valueOf(LocalDateTime.now()));
                        mqMessageConsumerLog.setConsumer_status(ShopCode.SHOP_MQ_MESSAGE_STATUS_PROCESSING.getCode());
                        Integer mqLogEffectRows = mqMessageConsumerLogMapper.updateByPrimaryKeyWithLock(mqMessageConsumerLog);
                        if(mqLogEffectRows <= 0){
                            System.out.println("并发修改,稍后处理");
                        }else{
                            //此处查询出上面日志表的更新后的数据
                            mqMessageConsumerLog = mqMessageConsumerLogMapper.findByParams(mqMessageConsumerLogCondition);
                            //开始回退商品库存[真正业务],并记录商品回退日志
                            Integer unReduceNum = goodsService.unReduceNum(goodsLog);
                            if(unReduceNum.intValue() > 0){
                                mqMessageConsumerLog.setConsumer_status(ShopCode.SHOP_MQ_MESSAGE_STATUS_SUCCESS.getCode());
                                System.out.println("商品库存回退成功");
                            }else{
                                mqMessageConsumerLog.setConsumer_status(ShopCode.SHOP_MQ_MESSAGE_STATUS_FAIL.getCode());
                                System.out.println("商品库存回退失败");
                            }
                            //此处更新商品库存消息的处理结果
                           mqMessageConsumerLogMapper.updateByPrimaryKey(mqMessageConsumerLog);
                        }
                    }else{
                        return;
                    }
                }


            }else{
                //如果没有消费过
                //消费消息日志记录
                mqMessageConsumerLog = new MqMessageConsumerLog();
                mqMessageConsumerLog.setGroup_name(consumerGroup);
                mqMessageConsumerLog.setMsg_tag(tags);
                mqMessageConsumerLog.setMsg_key(keys);
                mqMessageConsumerLog.setConsumer_status(ShopCode.SHOP_MQ_MESSAGE_STATUS_PROCESSING.getCode());
                mqMessageConsumerLog.setConsumer_times(0);
                mqMessageConsumerLog.setMsg_body(messageBody);
                mqMessageConsumerLog.setId(UUIDWorker.getUUIDFormat());
                //开始回退商品库存[真正业务],并记录商品回退日志
                Integer unReduceNum = goodsService.unReduceNum(goodsLog);
                if(unReduceNum.intValue() > 0){
                    mqMessageConsumerLog.setConsumer_status(ShopCode.SHOP_MQ_MESSAGE_STATUS_SUCCESS.getCode());
                    System.out.println("商品库存回退成功");
                }else{
                    mqMessageConsumerLog.setConsumer_status(ShopCode.SHOP_MQ_MESSAGE_STATUS_FAIL.getCode());
                    System.out.println("商品库存回退失败");
                }
                Integer addLogs = mqMessageConsumerLogMapper.add(mqMessageConsumerLog);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
