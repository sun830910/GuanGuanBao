package com.enjoygreenlife.guanguanbao.model.ApiModel;

import com.enjoygreenlife.guanguanbao.model.DataModel.ScanQRCodeResponse;
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

    public UserLoginResponse parseUserLoginResponse(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, UserLoginResponse.class);
    }

    public ScanQRCodeResponse parseScanQRCodeResponse(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, ScanQRCodeResponse.class);
    }
}
