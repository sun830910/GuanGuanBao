package com.enjoygreenlife.guanguanbao.model.ApiModel;

import com.enjoygreenlife.guanguanbao.model.DataModel.UserLoginResponse;
import com.google.gson.Gson;

/**
 * Created by luthertsai on 2017/11/18.
 */

public class ApiJsonFactory {

    public String getLoginJson(String username, String userPassword) {
        return new String("{\"route\":\"login\",\"member\":{\"userName\":\"" + username + "\",\"userPassword\":\"" + userPassword + "\"}}");
    }

    public String getUserInfoJson(String session, String userID) {
        return new String("{\"route\":\"getUserInfo\",\"session\":\"" + session + "\", \"userId\":" + userID + "}");
    }

    public UserLoginResponse parseUserLoginResponse(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, UserLoginResponse.class);
    }
}
