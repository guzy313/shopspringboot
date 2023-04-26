package com.my.shop.service;

import com.my.shop.pojo.Order;
import com.my.shop.pojo.User;
import feign.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

/**
 * @author Gzy
 * @version 1.0
 * @Description 用户接口
 * @date create on 2023/4/20
 */
@Service
@FeignClient(value = "shopspringboot-user-service")
public interface UserService {

    /**
     * 通过用户ID查询用户
     * @param id
     * @return
     */
    @GetMapping("/user/getUserById")
    User getUserById(@RequestParam("id")BigInteger id);

    /**
     * 创建用户
     * @param user
     */
    @PostMapping("user/createUser")
    void createUser(@RequestBody User user);

    /**
     * 更新用户
     * @param user
     */
    @PostMapping("user/updateUser")
    void updateUser(@RequestBody User user);

    /**
     * 使用用户余额
     * @param order
     */
    @PostMapping("user/useUserBalance")
    void useUserBalance(@RequestBody Order order);

}
