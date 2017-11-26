package com.enjoygreenlife.guanguanbao.model.DataModel;

import java.util.ArrayList;

/**
 * Created by luthertsai on 2017/11/26.
 */

public class RecycleMachineResponse {
    private int code = 0;
    private ArrayList<RecycleMachine> data = new ArrayList<RecycleMachine>();

    public int getCode() {
        return code;
    }

    public ArrayList<RecycleMachine> getData() {
        return data;
    }
}
