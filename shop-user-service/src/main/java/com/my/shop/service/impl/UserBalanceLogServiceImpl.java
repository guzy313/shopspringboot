package com.my.shop.service.impl;

import com.my.shop.common.constant.ShopCode;
import com.my.shop.common.exception.CastException;
import com.my.shop.common.util.IdWorker;
import com.my.shop.mapper.UserBalanceLogMapper;
import com.my.shop.pojo.User;
import com.my.shop.pojo.UserBalanceLog;
import com.my.shop.service.UserBalanceLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @author Gzy
 * @version 1.0
 * @Description
 * @date create on 2023/4/24
 */
@Service
public class UserBalanceLogServiceImpl implements UserBalanceLogService {
    @Autowired
    private UserBalanceLogMapper userBalanceLogMapper;

    @Override
    public UserBalanceLog getUserBalanceLogById(BigInteger id) {
        return userBalanceLogMapper.findById(id);
    }

    @Override
    public void createUserBalanceLog(UserBalanceLog userBalanceLog) {
        if(userBalanceLog == null){
            CastException.cast(ShopCode.SHOP_USER_UPDATE_SUCCESS);
        }
        userBalanceLog.setCreate_time(Timestamp.valueOf(LocalDateTime.now()));
        userBalanceLogMapper.add(userBalanceLog);
    }

    @Override
    public void updateUserBalanceLog(UserBalanceLog userBalanceLog) {
        //TODO
    }
}
