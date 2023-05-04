package com.my.shop.common.util;

import java.util.UUID;

/**
 * @author Gzy
 * @version 1.0
 * @Description UUID生成
 * @date create on 2023/5/4
 */
public class UUIDWorker {

    /**
     * 随机生成无符号UUID
     * @return
     */
    public static String getUUIDFormat(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }



}
