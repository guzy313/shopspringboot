package com.my.shop.mapper;

import com.my.shop.pojo.User;
import com.my.shop.pojo.UserBalanceLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

/**
 * @author Gzy
 * @version 1.0
 * @Description
 * @date create on 2023/4/20
 */
@Component
public interface UserBalanceLogMapper {

    List<UserBalanceLog> findAll();

    UserBalanceLog findById(@Param("id")BigInteger id);

    List<UserBalanceLog> findByOrderIdAndUserId(@Param("orderId")BigInteger orderId,@Param("userId")BigInteger userId);

    void add(@Param("userBalanceLog")UserBalanceLog userBalanceLog);

    void update(@Param("userBalanceLog")UserBalanceLog userBalanceLog);

}
