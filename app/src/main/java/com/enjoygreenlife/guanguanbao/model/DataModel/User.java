package com.enjoygreenlife.guanguanbao.model.DataModel;


/**
 * Created by luthertsai on 2017/11/17.
 */

public class User {

    private int user_id = 0;

    private String username = "";

    private String profilePhoto = "";

    private double wallet = 0.0;

    private double lockingWallet = 0.0;

    private String address = "";

    private String phone = "";

    private String birthday = "";

    private int gender = 0;

    private String email = "";

    private String ali = "";

    private String qq = "";

    private String wechat = "";

    private int rankId = 0;

    private double sumPoint = 0.0;

    private int totalNums = 0;

    private double totalCoals = 0;

    public User() {

    }

    public int getUser_id() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public double getWallet() {
        return wallet;
    }

    public double getLockingWallet() {
        return wallet;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public int getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public String getAli() {
        return ali;
    }

    public String getQq() {
        return qq;
    }

    public String getWechat() {
        return wechat;
    }

    public int getRankId() {
        return rankId;
    }

    public double getSumPoint() {
        return sumPoint;
    }

    public int getTotalNums() {
        return totalNums;
    }

    public double getTotalCoals() {
        return totalCoals;
    }
}
