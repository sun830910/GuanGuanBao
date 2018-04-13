package com.enjoygreenlife.guanguanbao.model.DataModel;

/**
 * Created by luthertsai on 2017/11/23.
 */

public class ScanQRCodeResponse {
    private int code = 0;
    private RecycleInvoice returnObject = new RecycleInvoice();

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public RecycleInvoice getReturnObject() {
        return returnObject;
    }

    public void setReturnObject(RecycleInvoice returnObject) {
        this.returnObject = returnObject;
    }
}
