package com.enjoygreenlife.guanguanbao.model.DataModel;

/**
 * Created by luthertsai on 2017/11/17.
 */

public class UserLoginResponse {

    private int code = 0;

    private User returnObject = new User();

    private String session = "";

    public UserLoginResponse() {

    }

    public int getCode() {
        return code;
    }

    public User getReturnObject() {
        return returnObject;
    }

    public String getSession() {
        return session;
    }
}
