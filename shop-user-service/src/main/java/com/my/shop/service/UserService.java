package com.my.shop.service;

import com.my.shop.pojo.Order;
import com.my.shop.pojo.User;
import com.my.shop.pojo.UserBalanceLog;
import feign.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigInteger;

/**
 * @author Gzy
 * @version 1.0
 * @Description
 * @date create on 2023/4/20
 */
public interface UserService {

    /**
     * 通过用户ID查询用户
     * @param id
     * @return
     */
    User getUserById(BigInteger id);

    /**
     * 创建用户
     * @param user
     */
    void createUser(User user);

    /**
     * 更新用户
     * @param user
     */
    void updateUser(User user);

    /**
     * 使用用户余额
     * @param order
     */
    void useUserBalance(Order order);

    /**
     * 更新用户余额
     * @param userBalanceLog
     */
    void updateUserBalance(UserBalanceLog userBalanceLog);

}
