package com.my.shop.common;

import java.io.Serializable;

/**
 * @author Gzy
 * @version 1.0
 * @Description 前台返回对象
 * @date create on 2023/4/19
 */
public class CommonResult<T> implements Serializable {
    //代码 - 200成功
    private Integer code;
    //消息
    private String message;
    //返回数据
    private T data;

    public CommonResult() {
    }

    public CommonResult(Integer code, String message) {
        this.code = code;
        this.message = message;
    }


    public CommonResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


}
