package com.my.shop.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author Gzy
 * @version 1.0
 * @Description MQ消息消费者 日志表
 * @date create on 2023/4/19
 */
public class MqMessageConsumerLog implements Serializable {
    //'主键'
    private String id;
    //'消费者组名'
    private String group_name;
    //'消息主题'
    private String msg_topic;
    //'消息标签'
    private String msg_tag;
    //'消息关键字'
    private String msg_key;
    //'消息内容'
    private String msg_body;
    //'消息状态 0正在处理 1处理成功 2处理失败'
    private Integer consumer_status;
    //'消费次数'
    private Integer consumer_times;
    //'消费时间'
    private Timestamp consumer_time;
    //'备注'
    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getMsg_topic() {
        return msg_topic;
    }

    public void setMsg_topic(String msg_topic) {
        this.msg_topic = msg_topic;
    }

    public String getMsg_tag() {
        return msg_tag;
    }

    public void setMsg_tag(String msg_tag) {
        this.msg_tag = msg_tag;
    }

    public String getMsg_key() {
        return msg_key;
    }

    public void setMsg_key(String msg_key) {
        this.msg_key = msg_key;
    }

    public String getMsg_body() {
        return msg_body;
    }

    public void setMsg_body(String msg_body) {
        this.msg_body = msg_body;
    }

    public Integer getConsumer_status() {
        return consumer_status;
    }

    public void setConsumer_status(Integer consumer_status) {
        this.consumer_status = consumer_status;
    }

    public Integer getConsumer_times() {
        return consumer_times;
    }

    public void setConsumer_times(Integer consumer_times) {
        this.consumer_times = consumer_times;
    }

    public Timestamp getConsumer_time() {
        return consumer_time;
    }

    public void setConsumer_time(Timestamp consumer_time) {
        this.consumer_time = consumer_time;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
