package com.my.shop.common.exception;

import com.my.shop.common.constant.ShopCode;

/**
 * @author Gzy
 * @version 1.0
 * @Description 自定义异常
 * @date create on 2023/4/20
 * */
public class CustomerException extends RuntimeException{

    private ShopCode shopCode;

    public CustomerException(ShopCode shopCode) {
        this.shopCode = shopCode;
    }

}

