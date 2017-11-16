package com.enjoygreenlife.guanguanbao.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.enjoygreenlife.guanguanbao.R;
import com.enjoygreenlife.guanguanbao.model.SettingItem;
import com.enjoygreenlife.guanguanbao.model.SettingListAdapter;

import java.util.ArrayList;
import java.util.List;

public class SettingsMenuActivity extends AppCompatActivity {

    List<SettingItem> settingItemList = new ArrayList<SettingItem>();
    private ListView listV;
    private SettingListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_menu);

        // Set up on Top Toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        listV = (ListView) findViewById(R.id.setting_list);

        settingItemList.add(new SettingItem("帳號管理"));
        settingItemList.add(new SettingItem("通用管理"));
        settingItemList.add(new SettingItem("隱私設定"));
        settingItemList.add(new SettingItem("系統求助"));
        settingItemList.add(new SettingItem("關於軟件"));

        adapter = new SettingListAdapter(getApplicationContext(), settingItemList);

        listV.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
