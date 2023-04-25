package com.my.shop.service;

import com.my.shop.common.CommonResult;
import com.my.shop.pojo.Goods;
import com.my.shop.pojo.GoodsLog;
import feign.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigInteger;

/**
 * @author Gzy
 * @version 1.0
 * @Description
 * @date create on 2023/4/20
 */
@FeignClient("shopspringboot-goods-service")
@Service
public interface GoodsService {

    @GetMapping("/goods/findById")
    Goods findById(@Param("id") BigInteger id);

    @PostMapping("/goods/reduceNum")
    CommonResult reduceNum(@Param("orderLog") GoodsLog goodsLog);


}
