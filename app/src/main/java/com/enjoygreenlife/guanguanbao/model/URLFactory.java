package com.enjoygreenlife.guanguanbao.model;

import com.google.api.client.http.GenericUrl;

/**
 * Created by luthertsai on 2017/11/17.
 */

public class URLFactory extends GenericUrl {
    private static final String DOMAIN_NAME = "http://112.74.32.77/apiServer/";

    public URLFactory(String encodedUrl) {
        super(encodedUrl);
    }

    public static URLFactory getLoginURL() {
        return new URLFactory(DOMAIN_NAME + "login");
    }
}
