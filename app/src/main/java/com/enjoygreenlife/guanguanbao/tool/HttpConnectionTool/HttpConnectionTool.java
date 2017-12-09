package com.enjoygreenlife.guanguanbao.tool.HttpConnectionTool;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


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

    public HttpConnectionTool() {
        _client = new OkHttpClient.Builder()
                .connectTimeout(DEF_TIMEOUT_SOCKET, TimeUnit.SECONDS)
                .writeTimeout(DEF_TIMEOUT_SOCKET, TimeUnit.SECONDS)
                .readTimeout(DEF_TIMEOUT_SOCKET * 3, TimeUnit.SECONDS)
                .build();
    }

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

    public void cancel() {

    }

    public void postMethod(String url, String inputString, final HttpConnectionToolCallback callback) {
        final MediaType MEDIA_TYPE_JSON
                = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, inputString);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        _client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(final Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try (ResponseBody responseBody = response.body()) {
                            if (!response.isSuccessful())
                                throw new IOException("Unexpected code " + response);

                            Headers responseHeaders = response.headers();
                            for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                                System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                            }
                            callback.onSuccess(responseBody.string());
                        }
                    }
                });
    }

}
