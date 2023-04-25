package com.my.shop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Gzy
 * @version 1.0
 * @Description 优惠券服务启动类
 * @date create on 2023/4/19
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan(basePackages = {"com.my.shop.mapper"})
public class CouponApplication {

    public static void main(String[] args) {
        SpringApplication.run(CouponApplication.class,args);
    }

}
