package com.enjoygreenlife.guanguanbao.model.ApiModel;

/**
 * Created by luthertsai on 2017/11/17.
 */

public class URLFactory {
    private static final String DOMAIN_NAME = "http://112.74.32.77/apiServer/";

    public String getLoginURL() {
        return new String(DOMAIN_NAME + "login");
    }

    public String getUerInfoURL() {
        return new String(DOMAIN_NAME + "getUserInfo");
    }
}