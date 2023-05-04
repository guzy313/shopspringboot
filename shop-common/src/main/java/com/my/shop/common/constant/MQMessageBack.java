package com.my.shop.common.constant;

/**
 * @author Gzy
 * @version 1.0
 * @Description MQ消息返回枚举类
 * @date create on 2023/4/27
 */
public enum MQMessageBack {
    SUCCESS(0000,"消息发送成功"),
    UN_CONFIRM_ORDER(0001,"确认订单失败回退(删除订单) 发送消息 失败");

    private Integer code;

    private String message;

    MQMessageBack(Integer code, String message) {
        this.code = code;
        this.message = message;
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

    @Override
    public String toString() {
        return "MQMessageBack{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
