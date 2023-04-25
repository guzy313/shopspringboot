package com.my.shop.mapper;

import com.my.shop.pojo.Payment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

/**
 * @author Gzy
 * @version 1.0
 * @Description 支付数据操作层
 * @date create on 2023/4/21
 */
@Repository
public interface PaymentMapper {

    List<Payment> findAll();

    Payment findById(@Param("id") BigInteger id);

    void add(@Param("payment")Payment payment);

    BigInteger findMaxId();

}
