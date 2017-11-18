package com.enjoygreenlife.guanguanbao.model.ViewModel.SettingList;

import android.media.Image;


/**
 * Created by luthertsai on 2017/11/15.
 */

public class SettingItem {

    private String _title;

    private String _imgResName;

    public SettingItem(String title, String imgResName) {
        this._title = title;
        this._imgResName = imgResName;
    }

    public String getImgResName() {
        return _imgResName;
    }

    public void setImgResName(String imgResName) {
        this._imgResName = imgResName;
    }

    public String GetTitle() {
        return _title;
    }

    public void SetTitle(String _title) {
        this._title = _title;
    }

}
