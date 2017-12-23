package com.enjoygreenlife.guanguanbao.model.ViewModel.DrawCashList;

/**
 * Created by luthertsai on 2017/12/23.
 */

public class DrawCashItem {
    private String _cost;

    private String _realMoney;

    public DrawCashItem(String cost, String realMoney) {
        this._cost = cost;
        this._realMoney = realMoney;
    }

    public String getCost() {
        return _cost;
    }

    public String getRealMoney() {
        return _realMoney;
    }
}
