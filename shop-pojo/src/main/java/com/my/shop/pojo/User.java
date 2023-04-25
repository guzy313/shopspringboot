package com.my.shop.pojo;


import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;

/**
 * @author Gzy
 * @version 1.0
 * @Description 用户类
 * @date create on 2023/4/19
 */
public class User implements Serializable {
    //主键
    private BigInteger id;
    //用户名称
    private String user_name;
    //用户密码
    private String user_password;
    //手机号
    private String user_phone;
    //积分
    private Integer user_score;
    //注册时间
    private Timestamp user_reg_time;
    //用户余额
    private BigDecimal user_money;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public Integer getUser_score() {
        return user_score;
    }

    public void setUser_score(Integer user_score) {
        this.user_score = user_score;
    }

    public Timestamp getUser_reg_time() {
        return user_reg_time;
    }

    public void setUser_reg_time(Timestamp user_reg_time) {
        this.user_reg_time = user_reg_time;
    }

    public BigDecimal getUser_money() {
        return user_money;
    }

    public void setUser_money(BigDecimal user_money) {
        this.user_money = user_money;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", user_name='" + user_name + '\'' +
                ", user_password='" + user_password + '\'' +
                ", user_phone='" + user_phone + '\'' +
                ", user_score=" + user_score +
                ", user_reg_time=" + user_reg_time +
                ", user_money=" + user_money +
                '}';
    }
}
