package com.my.shop.mapper;

import com.my.shop.pojo.User;
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
public interface UserMapper {

    List<User> findAll();

    User findById(@Param("id")BigInteger id);

    Integer add(@Param("user")User user);

    Integer updateByPrimaryKey(@Param("user")User user);

    BigInteger findMaxId();

}
