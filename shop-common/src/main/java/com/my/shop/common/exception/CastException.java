package com.my.shop.common.exception;

import com.my.shop.common.constant.ShopCode;

import java.io.Serializable;

/**
 * @author Gzy
 * @version 1.0
 * @Description 异常处理类
 * @date create on 2023/4/19
 */
public class CastException implements Serializable {

    public static void cast(ShopCode shopCode){
        throw new CustomerException(shopCode);
    }

}
