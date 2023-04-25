package com.my.shop.controller;

import com.my.shop.common.CommonResult;
import com.my.shop.pojo.Goods;
import com.my.shop.pojo.GoodsLog;
import com.my.shop.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigInteger;

/**
 * @author Gzy
 * @version 1.0
 * @Description
 * @date create on 2023/4/23
 */
@Controller
public class GoodsController {
    @Autowired
    private GoodsService goodsService;


    @GetMapping("/goods/findById")
    @ResponseBody
    public Goods findById(@RequestParam("id") BigInteger id){
        return goodsService.findById(id);
    }

    @PostMapping("/goods/reduceNum")
    @ResponseBody
    public CommonResult reduceNum(@RequestParam("goodsLog") GoodsLog goodsLog){
       return goodsService.reduceNum(goodsLog);
    }



}
