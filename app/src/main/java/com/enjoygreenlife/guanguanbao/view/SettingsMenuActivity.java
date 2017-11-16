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

    List<SettingItem> _settingItemList = new ArrayList<SettingItem>();
    private ListView _settingListView;
    private SettingListAdapter _settingListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_menu);

        // Set up on Top Toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(getString(R.string.bar_title_settings));

        _settingListView = (ListView) findViewById(R.id.setting_list);

        _settingItemList.add(new SettingItem(getString(R.string.title_edit_personal), "ic_edit_personal"));
        _settingItemList.add(new SettingItem(getString(R.string.title_edit_general), "ic_general_setting"));
        _settingItemList.add(new SettingItem(getString(R.string.title_edit_privacy), "ic_privacy"));
        _settingItemList.add(new SettingItem(getString(R.string.title_help), "ic_megaphone"));
        _settingItemList.add(new SettingItem(getString(R.string.title_about), "ic_app_info"));

        _settingListAdapter = new SettingListAdapter(getApplicationContext(), _settingItemList);

        _settingListView.setAdapter(_settingListAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
