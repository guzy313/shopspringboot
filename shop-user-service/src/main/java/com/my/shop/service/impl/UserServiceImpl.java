package com.my.shop.service.impl;

import com.my.shop.common.constant.ShopCode;
import com.my.shop.common.exception.CastException;
import com.my.shop.common.util.IdWorker;
import com.my.shop.mapper.UserBalanceLogMapper;
import com.my.shop.mapper.UserMapper;
import com.my.shop.pojo.Order;
import com.my.shop.pojo.User;
import com.my.shop.pojo.UserBalanceLog;
import com.my.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Gzy
 * @version 1.0
 * @Description
 * @date create on 2023/4/20
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserBalanceLogMapper userBalanceLogMapper;

    @Override
    public User getUserById(BigInteger id) {
        return userMapper.findById(id);
    }

    @Override
    public void createUser(User user) {
        if(user == null
        || user.getUser_name() == null
        || user.getUser_password() == null){
            CastException.cast(ShopCode.SHOP_USER_CREATE_FAIL);
        }

        //雪花算法自动生成下一个ID
        IdWorker idWorker = new IdWorker(userMapper.findAll().size());
        user.setId(BigInteger.valueOf(idWorker.nextId()));
        user.setUser_reg_time(Timestamp.valueOf(LocalDateTime.now()));
        userMapper.add(user);
    }

    @Override
    public Integer updateUser(User user) {
        if(user == null){
            CastException.cast(ShopCode.SHOP_USER_IS_NULL);
        }
        if(user.getId() == null){
            CastException.cast(ShopCode.SHOP_USER_NO_EXIST);
        }
        return userMapper.updateByPrimaryKey(user);
    }

    @Override
    public Integer useUserBalance(Order order) {
        UserBalanceLog userBalanceLog = new UserBalanceLog();
        System.out.println("run to here ..");
        userBalanceLog.setOrder_id(order.getId());
        //设置日志类型为付款
        userBalanceLog.setMoney_log_type(ShopCode.SHOP_USER_MONEY_PAID.getCode());
        userBalanceLog.setUse_money(order.getMoney_paid());
        userBalanceLog.setUser_id(order.getUser_id());
        userBalanceLog.setCreate_time(Timestamp.valueOf(LocalDateTime.now()));

        //更新操作
        return this.updateUserBalance(userBalanceLog);
    }

    public Integer unUseUserBalance(UserBalanceLog userBalanceLog) {
        //更新操作
        return this.updateUserBalance(userBalanceLog);
    }

    @Override
    public Integer updateUserBalance(UserBalanceLog userBalanceLog) {
        //获取使用的用户余额
        BigDecimal use_money = userBalanceLog.getUse_money();
        //获取用户
        User user = userMapper.findById(userBalanceLog.getUser_id());
        //判断用户是否存在
        if(user == null){
            CastException.cast(ShopCode.SHOP_USER_NO_EXIST);
        }
        Integer effectUserRows = 0;

        //1.校验参数是否合法
        if(userBalanceLog == null || userBalanceLog.getUser_id() == null
                || userBalanceLog.getOrder_id() == null
                || userBalanceLog.getUse_money() == null
                || userBalanceLog.getUser_id() == null
                || userBalanceLog.getUse_money().compareTo(BigDecimal.ZERO) <= 0 ){
            CastException.cast(ShopCode.SHOP_ORDER_INVALID);
        }
        //2.查询余额使用日志(通过订单id和用户id)
        List<UserBalanceLog> userBalanceLogList = userBalanceLogMapper.findByOrderIdAndUserId(userBalanceLog.getOrder_id(), userBalanceLog.getUser_id());
        //3.扣减余额情况
        if(userBalanceLog.getMoney_log_type().intValue() == ShopCode.SHOP_USER_MONEY_PAID.getCode().intValue()){
            //当前是付款情况
            if(userBalanceLogList.size() > 0){
                //已经付款,不需要再付款
                CastException.cast(ShopCode.SHOP_ORDER_PAY_STATUS_IS_PAY);
            }

            //未付款,准备开始付款操作
            //判断用户余额是否足够支付
            if(user.getUser_money().compareTo(use_money) < 0){
                CastException.cast(ShopCode.SHOP_MONEY_PAID_LESS_ZERO);
            }
            //进行余额扣减
            user.setUser_money(user.getUser_money().subtract(use_money));
            //更新用户信息
            effectUserRows = userMapper.updateByPrimaryKey(user);
            System.out.println(ShopCode.SHOP_USER_MONEY_PAID);
        }

        //4.回退余额情况
        if(userBalanceLog.getMoney_log_type().intValue() == ShopCode.SHOP_USER_MONEY_REFUND.getCode().intValue()){
            //当前是回退余额情况
            if(userBalanceLogList.size() <= 0){
                //未付过款不需要回退
                CastException.cast(ShopCode.SHOP_ORDER_PAY_STATUS_NO_PAY);
            }
            //判断是否进行过退款,如果进行过则不允许再退款
            for (UserBalanceLog u:userBalanceLogList) {
                if(u.getMoney_log_type().intValue() == ShopCode.SHOP_USER_MONEY_REFUND.getCode().intValue()){
                    CastException.cast(ShopCode.SHOP_USER_MONEY_REFUND_ALREADY);
                }
            }
            //进行余额增加
            user.setUser_money(user.getUser_money().add(use_money));
            //更新用户信息
            effectUserRows = userMapper.updateByPrimaryKey(user);
            System.out.println(ShopCode.SHOP_USER_MONEY_REFUND);
            return effectUserRows;
        }

        //5.记录用户订单余额变动日志
        userBalanceLogMapper.add(userBalanceLog);
        return effectUserRows;
    }
}
