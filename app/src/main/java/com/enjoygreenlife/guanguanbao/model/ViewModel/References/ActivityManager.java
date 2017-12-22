package com.enjoygreenlife.guanguanbao.model.ViewModel.References;

/**
 * Created by luthertsai on 2017/12/21.
 */

public enum ActivityManager {
    HOME_ACTIVITY (9999),
    LOGIN_ACTIVITY (9998),
    REGISTER_ACTIVITY (9997),
    BASESCANNER_ACTIVITY (9996),
    SCANNER_RESULT_ACTIVITY (9995),
    SETTINGS_MENU_ACTIVITY (9994),
    MARKET_HOME_ACTIVITY (9993);

    private final int value;

    private ActivityManager(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
