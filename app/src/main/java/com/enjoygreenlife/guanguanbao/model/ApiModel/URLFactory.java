package com.enjoygreenlife.guanguanbao.model.ApiModel;

/**
 * Created by luthertsai on 2017/11/17.
 */

public class URLFactory {
    private static final String DOMAIN_NAME = "http://39.106.38.0:8080/GuanGuanBaoServerside/";

    public String getLoginURL() {
        return new String(DOMAIN_NAME + "login");
    }

    public String getLogoutURL() {
        return new String(DOMAIN_NAME + "logout");
    }

    public String getRegisterURL() {
        return new String(DOMAIN_NAME + "register");
    }

    public String getUerInfoURL() {
        return new String(DOMAIN_NAME + "user/");
    }

    public String scanQRcodeURL() {
        return new String(DOMAIN_NAME + "scanQRcode");
    }

    public String getStationURL() {
        return new String(DOMAIN_NAME + "stations");
    }

    public String getItemURL() {
        return new String(DOMAIN_NAME + "getItem");
    }

    public String buyItemURL() {
        return new String(DOMAIN_NAME + "buyItem");
    }
}
