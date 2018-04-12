package com.enjoygreenlife.guanguanbao.model.DataModel;


/**
 * Created by luthertsai on 2017/11/17.
 */

public class User {

    private Integer user_id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String avatar;
    private String session;
    private String wechat;
    private String alipay;
    private String name;
    private String birthday;
    private Integer wallet = 0;
    private Integer lock_wallet = 0;
    private String latest_ip;
    private Integer totalCount = 0;
    private Integer totalProfit = 0;
    private Double totalCoal = 0.0;
    private Double totalWeight = 0.0;


    public User() {

    }

    public Integer getUser_id() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getSession() {
        return session;
    }

    public String getWechat() {
        return wechat;
    }

    public String getAlipay() {
        return alipay;
    }

    public String getName() {
        return name;
    }

    public String getBirthday() {
        return birthday;
    }

    public Integer getWallet() {
        return wallet;
    }

    public Integer getLock_wallet() {
        return lock_wallet;
    }

    public String getLatest_ip() {
        return latest_ip;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public Integer getTotalProfit() {
        return totalProfit;
    }

    public Double getTotalCoal() {
        return totalCoal;
    }

    public Double getTotalWeight() {
        return totalWeight;
    }
}
