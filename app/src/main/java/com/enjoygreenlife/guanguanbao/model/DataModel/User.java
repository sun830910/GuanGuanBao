package com.enjoygreenlife.guanguanbao.model.DataModel;


/**
 * Created by luthertsai on 2017/11/17.
 */

public class User {

    private int id = 0;

    private String userName = "";

    private String profilePhoto = "";

    private double wallet = 0.0;

    private double lockingWallet = 0.0;

    private String address = "";

    private String phoneNumber = "";

    private String birthday = "";

    private int gender = 0;

    private String email = "";

    private String ali = "";

    private String qq = "";

    private String wechat = "";

    private int rankId = 0;

    public User() {

    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
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

    public String getPhoneNumber() {
        return phoneNumber;
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
}
