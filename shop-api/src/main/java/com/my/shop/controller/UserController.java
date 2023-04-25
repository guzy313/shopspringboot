package com.my.shop.controller;

import com.my.shop.common.CommonResult;
import com.my.shop.common.constant.ShopCode;
import com.my.shop.pojo.Order;
import com.my.shop.pojo.User;
import com.my.shop.service.UserService;
import feign.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigInteger;

/**
 * @author Gzy
 * @version 1.0
 * @Description 用户控制器
 * @date create on 2023/4/20
 */
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 通过用户ID查询用户
     * @param id
     * @return
     */
    @GetMapping("/user/getUserById")
    @ResponseBody
    public User getUserById(@RequestParam("id") BigInteger id){
        return userService.getUserById(id);
    }

    /**
     * 创建用户
     * @param user
     */
    @PostMapping("/user/createUser")
    @ResponseBody
    public CommonResult createUser(@RequestParam User user){
        userService.createUser(user);
        return new CommonResult(200,"创建用户成功", ShopCode.SHOP_USER_CREATE_SUCCESS);
    }

    /**
     * 更新用户
     * @param user
     */
    @PostMapping("user/updateUser")
    @ResponseBody
    public CommonResult updateUser(User user){
        userService.updateUser(user);
        return new CommonResult(ShopCode.SHOP_USER_UPDATE_SUCCESS.getCode(),ShopCode.SHOP_USER_UPDATE_SUCCESS.getMessage());
    }

    /**
     * 使用用户余额
     * @param order
     */
    @PostMapping("user/useUserBalance")
    @ResponseBody
    public void useUserBalance(@RequestParam("order") Order order){
        userService.useUserBalance(order);
    }

}
