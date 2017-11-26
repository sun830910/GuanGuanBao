package com.enjoygreenlife.guanguanbao.model.DataModel;

/**
 * Created by luthertsai on 2017/11/23.
 */

public class ScanQRCodeResult {
    private String totalPoint = "0";
    private String totalNums = "0";
    private String totalWeight = "0";
    private String totalCoals = "0";
    private String userId = "0";
    private String orderNo = "";

    public void setTotalPoint(String totalPoint) {
        this.totalPoint = totalPoint;
    }

    public void setTotalNums(String totalNums) {
        this.totalNums = totalNums;
    }

    public void setTotalWeight(String totalWeight) {
        this.totalWeight = totalWeight;
    }

    public void setTotalCoals(String totalCoals) {
        this.totalCoals = totalCoals;
    }

    public String getTotalPoint() {
        int result = Integer.parseInt(totalPoint);
        return String.valueOf(result);
    }

    public String getTotalNums() {
        int result = Integer.parseInt(totalNums);
        return String.valueOf(result);
    }

    public String getTotalWeight() {
        double result = Double.parseDouble(totalWeight);
        return String.format("%.2f", totalWeight);
    }

    public String getTotalCoals() {
        double result = Double.parseDouble(totalCoals);
        return String.format("%.2f", totalCoals);
    }

    public String getUserId() {
        return userId;
    }

    public String getOrderNo() {
        return orderNo;
    }
}
