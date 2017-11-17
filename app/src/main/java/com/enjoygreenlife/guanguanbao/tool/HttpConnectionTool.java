package com.enjoygreenlife.guanguanbao.tool;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;


/**
 * Created by luthertsai on 2017/11/18.
 */

public class HttpConnectionTool {


    /**
     * default socket timeout
     */
    public static final int DEF_TIMEOUT_SOCKET = 10;

    private final OkHttpClient _client;
    private Request _request;
    private boolean userStop = false;

    /**
     * detect network is available or no
     *
     * @param ctx is a Context
     * @return true if the network is fine or false if network don't work
     */
    public static boolean isAvailable(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null)
            return false;

        if (info.isAvailable())
            return true;
        else
            return false;
    }

    /**
     * detect network type is WIFI or not
     *
     * @param ctx is a Context
     * @return true with currently network type is WIFI or false if others
     */
    public static boolean isWIFI(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null)
            return false;

        int type = info.getType();
        if (type == ConnectivityManager.TYPE_WIFI || type == ConnectivityManager.TYPE_WIMAX)
            return true;
        else return false;
    }


    public HttpConnectionTool() {
        _client = new OkHttpClient.Builder()
                .connectTimeout(DEF_TIMEOUT_SOCKET, TimeUnit.SECONDS)
                .writeTimeout(DEF_TIMEOUT_SOCKET, TimeUnit.SECONDS)
                .readTimeout(DEF_TIMEOUT_SOCKET * 3, TimeUnit.SECONDS)
                .build();
    }


    public void cancel() {

    }

}
