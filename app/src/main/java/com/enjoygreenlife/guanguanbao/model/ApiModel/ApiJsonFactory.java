package com.enjoygreenlife.guanguanbao.model.ApiModel;

import android.location.Location;

import com.enjoygreenlife.guanguanbao.model.DataModel.RecycleMachineResponse;
import com.enjoygreenlife.guanguanbao.model.DataModel.ScanQRCodeResponse;
import com.enjoygreenlife.guanguanbao.model.DataModel.SimpleHttpResponse;
import com.enjoygreenlife.guanguanbao.model.DataModel.UserLoginResponse;
import com.google.gson.Gson;

/**
 * Created by luthertsai on 2017/11/18.
 */

public class ApiJsonFactory {

    public String getLoginJson(String username, String userPassword) {
        return "{\"route\":\"login\",\"member\":{\"userName\":\"" + username + "\",\"userPassword\":\"" + userPassword + "\"}}";
    }

    public String getUserInfoJson(String session, String userID) {
        return "{\"route\":\"getUserInfo\",\"session\":\"" + session + "\", \"userId\":" + userID + "}";
    }

    public String getLogoutJson(String session, String userID) {
        return "{\"route\":\"logout\",\"session\":\"" + session + "\", \"userId\":" + userID + "}";
    }

    public String scanQRcodeJson(String session, String userID, String QRCode, String QRCodeImg, String orderNumber) {
        return "{" +
                "\"route\":\"scanQRcode\"," +
                "\"session\":\"" + session + "\"," +
                "\"userId\":" + userID + "," +
                "\"orderNo\":\"" + orderNumber + "\"," +
                "\"QRCode\":\"" + QRCode + "\"," +
                "\"QRcodeImg\":\"" + QRCodeImg + "\"" +
                "}";
    }

    public String getStationJson(String session, String userID, Location location) {
        return "{" +
                "\"route\":\"getStation\"," +
                "\"session\":\"" + session + "\"," +
                "\"userId\":" + userID + "," +
                "\"latitude\":" + location.getLatitude() + "," +
                "\"longitude\":" + location.getLongitude() + "" +
               "}";
    }

    public String getRegisterJson(String account, String password, String phone, String mail) {
        return "{" +
                "\"route\":\"register\"," +
                "\"register\":{" +
                "\"userName\":\"" + account + "\"," +
                "\"userPassword\":\"" + password + "\"," +
                "\"phoneNumber\":\"" + phone + "\"," +
                "\"email\":\"" + mail + "\"," +
                "\"repassword\":\"" + password + "\"" +
                "}}";
    }

    public SimpleHttpResponse parseSimpleHttpResponse(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, SimpleHttpResponse.class);
    }

    public UserLoginResponse parseUserLoginResponse(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, UserLoginResponse.class);
    }

    public ScanQRCodeResponse parseScanQRCodeResponse(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, ScanQRCodeResponse.class);
    }

    public RecycleMachineResponse parseRecycleMachineResponse(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, RecycleMachineResponse.class);
    }
}
