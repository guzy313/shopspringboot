package com.my.shop.service;

import com.my.shop.common.CommonResult;
import com.my.shop.pojo.Goods;
import com.my.shop.pojo.GoodsLog;
import feign.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigInteger;

/**
 * @author Gzy
 * @version 1.0
 * @Description
 * @date create on 2023/4/20
 */
@FeignClient(value = "shopspringboot-api-service",contextId = "goods")
@Service
public interface GoodsService {

    @GetMapping("/goods/findById")
    Goods findById(@RequestParam("id") BigInteger id);

    @PostMapping("/goods/reduceNum")
    CommonResult reduceNum(@RequestBody GoodsLog goodsLog);


}
