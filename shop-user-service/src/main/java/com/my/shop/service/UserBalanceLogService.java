package com.my.shop.service;

import com.my.shop.pojo.User;
import com.my.shop.pojo.UserBalanceLog;

import java.math.BigInteger;

/**
 * @author Gzy
 * @version 1.0
 * @Description
 * @date create on 2023/4/24
 */
public interface UserBalanceLogService {

    /**
     * 通过用户ID查询用户余额操作日志
     * @param id
     * @return
     */
    UserBalanceLog getUserBalanceLogById(BigInteger id);

    /**
     * 创建用户余额操作日志
     * @param userBalanceLog
     */
    void createUserBalanceLog(UserBalanceLog userBalanceLog);

    /**
     * 更新用户余额操作日志
     * @param userBalanceLog
     */
    void updateUserBalanceLog(UserBalanceLog userBalanceLog);

}
