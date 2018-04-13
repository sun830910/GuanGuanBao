package com.enjoygreenlife.guanguanbao.model.ApiModel;

import android.location.Location;

import com.enjoygreenlife.guanguanbao.model.DataModel.Item;
import com.enjoygreenlife.guanguanbao.model.DataModel.ItemResponse;
import com.enjoygreenlife.guanguanbao.model.DataModel.RecycleMachineResponse;
import com.enjoygreenlife.guanguanbao.model.DataModel.ScanQRCodeResponse;
import com.enjoygreenlife.guanguanbao.model.DataModel.SimpleHttpResponse;
import com.enjoygreenlife.guanguanbao.model.DataModel.UserLoginResponse;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * Created by luthertsai on 2017/11/18.
 */

public class ApiJsonFactory {

    public String getLoginJson(String username, String userPassword) {
        return "{\"route\":\"login\",\"member\":{\"userName\":\"" + username + "\",\"userPassword\":\"" + userPassword + "\"}}";
    }

    public RequestBody getLoginFormBody(String username, String userPassword) {
        RequestBody body = new FormBody.Builder()
                .add("username", username)
                .add("password", userPassword)
                .build();
        return body;
    }

    public String getUserInfoJson(String session, String userID) {
        return "{\"route\":\"getUserInfo\",\"session\":\"" + session + "\", \"userId\":" + userID + "}";
    }

    public RequestBody getUserInfoFormBody(String session) {
        RequestBody body = new FormBody.Builder()
                .add("session", session)
                .build();
        return body;
    }

    public String getLogoutJson(String session, String userID) {
        return "{\"route\":\"logout\",\"session\":\"" + session + "\", \"userId\":" + userID + "}";
    }

    public RequestBody scanQRcodeFormBody(String session, String userID, String QRCode) {
        RequestBody body = new FormBody.Builder()
                .add("session", session)
                .add("username", userID)
                .add("hashCode", QRCode)
                .build();
        return body;
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

    public RequestBody getStationFormBody(String session, String userID, Location location) {
        RequestBody body = new FormBody.Builder()
                .add("latitide", "" + location.getLatitude())
                .add("longitude", "" + location.getLongitude())
                .add("session", session)
                .add("userID", userID)
                .build();
        return body;
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

    public String getItemJson(String session, String userID, int type) {
        return "{" +
                "\"route\":\"getItem\"," +
                "\"session\":\"" + session + "\"," +
                "\"userId\":" + userID + "," +
                "\"commodityTypeId\":" + type +
                "}";
    }

    public String buyItemsJson(String session, String userID, HashMap<Item, Integer> shoppingCart) {
        return "{" +
                "\"route\":\"buyItem\"," +
                "\"session\":\"" + session + "\"," +
                "\"userId\":" + userID + "," +
                "\"commodity\":" + generateJsonFromShoppingCart(shoppingCart) +
                "}";
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

    public ItemResponse parseItemResponse(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, ItemResponse.class);
    }

    private String generateJsonFromShoppingCart(HashMap<Item, Integer> shoppingCart) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        int i = 0;
        for (Map.Entry<Item, Integer> entry : shoppingCart.entrySet()) {
            Item item = entry.getKey();
            Integer number = entry.getValue();
            String itemJson = "{\"commodityId\":" + item.getId() + ", \"number\":" + number + "}";
            stringBuilder.append(itemJson);
            if (i != shoppingCart.size() - 1) {
                stringBuilder.append(",");
            }
            i++;
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
