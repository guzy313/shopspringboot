package com.my.shop.mapper;

import com.my.shop.pojo.MqMessageConsumerLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Gzy
 * @version 1.0
 * @Description 消息消费日志
 * @date create on 2023/4/28
 */
@Component
public interface MqMessageConsumerLogMapper {

    List<MqMessageConsumerLog> findAll();

    MqMessageConsumerLog findByParams(@Param("mqMessageConsumerLog")MqMessageConsumerLog mqMessageConsumerLog);

    Integer add(@Param("mqMessageConsumerLog")MqMessageConsumerLog mqMessageConsumerLog);

    Integer updateByPrimaryKey(@Param("mqMessageConsumerLog")MqMessageConsumerLog mqMessageConsumerLog);

    Integer updateByPrimaryKeyWithLock(@Param("mqMessageConsumerLog")MqMessageConsumerLog mqMessageConsumerLog);

}
