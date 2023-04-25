package com.my.shop.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author Gzy
 * @version 1.0
 * @Description MQ消息生产者 日志表
 * @date create on 2023/4/19
 */
public class MqMessageProducerLog implements Serializable {
    //'主键'
    private String id;
    //'生产者组名'
    private String group_name;
    //'消息主题'
    private String msg_topic;
    //'消息标签'
    private String msg_tag;
    //'消息关键字'
    private String msg_key;
    //'消息内容'
    private String msg_body;
    //'消息状态 0未处理 1已处理'
    private Integer msg_status;
    //'记录时间'
    private Timestamp create_time;


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

    public Integer getMsg_status() {
        return msg_status;
    }

    public void setMsg_status(Integer msg_status) {
        this.msg_status = msg_status;
    }

    public Timestamp getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Timestamp create_time) {
        this.create_time = create_time;
    }
}
