package com.my.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Gzy
 * @version 1.0
 * @Description
 * @date create on 2023/5/9
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class PayWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayWebApplication.class,args);
    }

}
