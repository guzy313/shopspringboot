package com.my.shop.mapper;

import com.my.shop.pojo.MqMessageProducerLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

/**
 * @author Gzy
 * @version 1.0
 * @Description 消息发送日志
 * @date create on 2023/5/5
 */
@Component
public interface MqMessageProducerLogMapper {

    List<MqMessageProducerLog> findAll();

    MqMessageProducerLog findByParams(@Param("mqMessageProducerLog") MqMessageProducerLog mqMessageProducerLog);

    Integer add(@Param("mqMessageProducerLog")MqMessageProducerLog mqMessageProducerLog);

    Integer updateByPrimaryKey(@Param("mqMessageProducerLog")MqMessageProducerLog mqMessageProducerLog);

    Integer deleteByPrimaryKey(@Param("id")String id);

}
