package com.enjoygreenlife.guanguanbao.model.DataModel;

/**
 * Created by luthertsai on 2017/11/26.
 */

public class RecycleMachine {
    private String id = "";
    private String name = "";
    private String recycleStationType = "";
    private String longitude = "";
    private String latitude = "";
    private String address = "";
    private String regionId = "";

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRecycleStationType() {
        return recycleStationType;
    }

    public double getLongitude() {
        return Double.parseDouble(longitude);
    }

    public double getLatitude() {
        return Double.parseDouble(latitude);
    }

    public String getAddress() {
        return address;
    }

    public String getRegionId() {
        return regionId;
    }
}

