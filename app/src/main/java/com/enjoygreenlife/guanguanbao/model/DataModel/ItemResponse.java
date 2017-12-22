package com.enjoygreenlife.guanguanbao.model.DataModel;

import java.util.ArrayList;

/**
 * Created by luthertsai on 2017/12/22.
 */

public class ItemResponse {
    private int code = 0;
    private ArrayList<Item> data = new ArrayList<Item>();

    public int getCode() {
        return code;
    }

    public ArrayList<Item> getData() {
        return data;
    }
}
