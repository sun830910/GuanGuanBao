package com.enjoygreenlife.guanguanbao.model.ApiModel;

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
}
