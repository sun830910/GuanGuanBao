package com.enjoygreenlife.guanguanbao.tool.httpConnectionTool;

import java.io.IOException;

import okhttp3.Call;

/**
 * Created by luthertsai on 2017/11/18.
 */

public interface HttpConnectionToolCallback {
    void onSuccess(String result);
    void onFailure(Call call, IOException e);
}
