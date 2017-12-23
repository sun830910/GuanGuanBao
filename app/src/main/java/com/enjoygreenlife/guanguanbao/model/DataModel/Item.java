package com.enjoygreenlife.guanguanbao.model.DataModel;

import java.util.ArrayList;

/**
 * Created by luthertsai on 2017/12/22.
 */

public class Item {
    private int id = 0;
    private String name = "";
    private String description = "";
    private String picture = "";
    private double price = 0;
    private int number = 0;
    private double realMoney = 0;
    private int commodityOrder = 0;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPicture() {
        return picture;
    }

    public double getPrice() {
        return price;
    }

    public int getNumber() {
        return number;
    }

    public double getRealMoney() {
        return realMoney;
    }

    public int getCommodityOrder() {
        return commodityOrder;
    }
}
