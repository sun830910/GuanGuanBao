package com.enjoygreenlife.guanguanbao.model.DataModel;

/**
 * Created by luthertsai on 2017/11/23.
 */

public class ScanQRCodeResponse {
    private int code = 0;
    private ScanQRCodeResult data = new ScanQRCodeResult();

    public ScanQRCodeResult getData() {
        return data;
    }

    public int getCode() {
        return code;
    }
}
