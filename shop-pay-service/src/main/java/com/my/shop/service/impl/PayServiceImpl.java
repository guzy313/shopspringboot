package com.my.shop.service.impl;

import com.alibaba.fastjson.JSON;
import com.my.shop.common.CommonResult;
import com.my.shop.common.constant.MQMessageConstant;
import com.my.shop.common.constant.ShopCode;
import com.my.shop.common.exception.CastException;
import com.my.shop.common.util.IdWorker;
import com.my.shop.common.util.UUIDWorker;
import com.my.shop.mapper.MqMessageProducerLogMapper;
import com.my.shop.mapper.PaymentMapper;
import com.my.shop.mq.MessageProducer;
import com.my.shop.pojo.MqMessageProducerLog;
import com.my.shop.pojo.Payment;
import com.my.shop.service.PayService;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Gzy
 * @version 1.0
 * @Description 支付服务实现
 * @date create on 2023/5/5
 */
@Service
public class PayServiceImpl implements PayService {
    @Value("${rocketmq.producer.group}")
    private String producerGroup;

    @Resource
    private PaymentMapper paymentMapper;

    @Resource
    private MqMessageProducerLogMapper mqMessageProducerLogMapper;

    @Resource
    private MessageProducer messageProducer;

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public CommonResult createPayment(Payment payment) {
        if(payment == null){
            CastException.cast(ShopCode.SHOP_PAYMENT_NOT_FOUND);
        }
        if(StringUtils.isEmpty(payment.getOrder_id().toString()) ||
            StringUtils.isEmpty(payment.getIs_paid().toString())){
            //订单ID不能为空,支付状态不能为空
            CastException.cast(ShopCode.SHOP_REQUEST_PARAMETER_VALID);
        }
        //判断订单是否已支付
        //创建查询条件对象(查询当前订单 已支付的记录)
        Payment condition = new Payment();
        condition.setOrder_id(payment.getOrder_id());
        condition.setIs_paid(ShopCode.SHOP_ORDER_PAY_STATUS_IS_PAY.getCode());
        List<Payment> paymentList = paymentMapper.findByParams(condition);
        if(paymentList.size() > 0){
            //已支付无需重新支付
            CastException.cast(ShopCode.SHOP_PAYMENT_IS_PAID);
        }

        IdWorker idWorker = new IdWorker(paymentMapper.findAll().size());
        payment.setId(BigInteger.valueOf(idWorker.nextId()));//生成ID
        payment.setIs_paid(ShopCode.SHOP_ORDER_PAY_STATUS_NO_PAY.getCode());//设置当前订单为未付款
        //保存订单
        paymentMapper.add(payment);
        System.out.println("支付订单创建成功");
        return new CommonResult(ShopCode.SHOP_SUCCESS.getCode(), ShopCode.SHOP_SUCCESS.getMessage());
    }

    @Override
    public CommonResult callbackPayment(Payment payment) {
        if(payment == null || payment.getOrder_id() == null
                || payment.getIs_paid() == null){
            CastException.cast(ShopCode.SHOP_REQUEST_PARAMETER_VALID);
        }
        //1.判断用户支付状态(已支付则继续后续流程,否则不允许继续执行)
        if(ShopCode.SHOP_ORDER_PAY_STATUS_IS_PAY.getCode().intValue() != payment.getIs_paid().intValue()){
            CastException.cast(ShopCode.SHOP_ORDER_PAY_STATUS_NO_PAY);
        }else{
            Payment findPaymentById = paymentMapper.findById(payment.getId());
            if(findPaymentById == null){
                //支付订单未找到
                CastException.cast(ShopCode.SHOP_PAYMENT_NOT_FOUND);
            }
            //2.更新支付订单状态为已支付
            findPaymentById.setIs_paid(ShopCode.SHOP_ORDER_PAY_STATUS_IS_PAY.getCode());
            Integer effectPaymentRows = paymentMapper.updateByPrimaryKey(findPaymentById);
            if(effectPaymentRows > 0){
                //3.创建支付成功消息
                MqMessageProducerLog mqMessageProducerLog = new MqMessageProducerLog();
                mqMessageProducerLog.setId(UUIDWorker.getUUIDFormat());
                mqMessageProducerLog.setGroup_name(producerGroup);
                mqMessageProducerLog.setMsg_topic(MQMessageConstant.TOPIC);
                mqMessageProducerLog.setMsg_tag(MQMessageConstant.TAG_PAYMENT_SUCCESS);
                mqMessageProducerLog.setMsg_key(payment.getId().toString());
                mqMessageProducerLog.setMsg_status(ShopCode.SHOP_MQ_MESSAGE_STATUS_PROCESSING.getCode());
                mqMessageProducerLog.setMsg_body(JSON.toJSONString(findPaymentById));
                mqMessageProducerLog.setCreate_time(Timestamp.valueOf(LocalDateTime.now()));
                //4.将消息持久化到数据库
                Integer addLogs = mqMessageProducerLogMapper.add(mqMessageProducerLog);

                //使用线程池进行优化消息发送
                threadPoolTaskExecutor.createThread(()->{
                    //5.发送消息到MQ
                    CommonResult messageSendResult = messageProducer.asyncSendBroadcast(MQMessageConstant.TOPIC,
                            MQMessageConstant.TAG_PAYMENT_SUCCESS, payment.getId().toString(), JSON.toJSONString(findPaymentById));
                    //6.等待发送结果,如果MQ成功接收到消息,则删除已经发送成功的消息
                    if(ShopCode.SHOP_MQ_SEND_MESSAGE_SUCCESS.getCode().intValue() == messageSendResult.getCode().intValue()){
                        System.out.println("消息发送成功");
                        Integer deleteByPrimaryKey = mqMessageProducerLogMapper.deleteByPrimaryKey(mqMessageProducerLog.getId());
                        //开始删除消息
                        if(deleteByPrimaryKey > 0){
                            System.out.println("消息日志删除成功");
                        }
                    }else{
                        System.out.println("消息发送失败");
                        //TODO 可以后续考虑实现消息重发 (比如定时任务查询消息发送日志表 重新进行发送)
                    }
                }).run();

            }else{
                //支付失败
                CastException.cast(ShopCode.SHOP_PAYMENT_PAY_ERROR);
            }
        }
        return new CommonResult(ShopCode.SHOP_PAYMENT_IS_PAID.getCode(),ShopCode.SHOP_PAYMENT_IS_PAID.getMessage());
    }
}
