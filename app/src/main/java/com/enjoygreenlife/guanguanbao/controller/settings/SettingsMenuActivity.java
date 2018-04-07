package com.enjoygreenlife.guanguanbao.controller.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.enjoygreenlife.guanguanbao.R;
import com.enjoygreenlife.guanguanbao.controller.settings.aboutApp.AboutAppActivity;
import com.enjoygreenlife.guanguanbao.controller.settings.generalSetting.GeneralSettingActivity;
import com.enjoygreenlife.guanguanbao.controller.settings.opinion.OpinionActivity;
import com.enjoygreenlife.guanguanbao.controller.settings.opinion.OpinionLastActivity;
import com.enjoygreenlife.guanguanbao.model.ApiModel.ApiJsonFactory;
import com.enjoygreenlife.guanguanbao.model.ApiModel.SharedFileHandler;
import com.enjoygreenlife.guanguanbao.model.ApiModel.URLFactory;
import com.enjoygreenlife.guanguanbao.model.DataModel.SimpleHttpResponse;
import com.enjoygreenlife.guanguanbao.model.ViewModel.References.ActivityManager;
import com.enjoygreenlife.guanguanbao.model.ViewModel.SettingList.SettingItem;
import com.enjoygreenlife.guanguanbao.model.ViewModel.SettingList.SettingListAdapter;
import com.enjoygreenlife.guanguanbao.tool.httpConnectionTool.HttpConnectionTool;
import com.enjoygreenlife.guanguanbao.tool.httpConnectionTool.HttpConnectionToolCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class SettingsMenuActivity extends AppCompatActivity {

    private final ApiJsonFactory _apiJsonFactory = new ApiJsonFactory();
    private SharedFileHandler _sharedFileHandler = new SharedFileHandler();
    private List<SettingItem> _settingItemList = new ArrayList<SettingItem>();
    private Button _logoutButton;
    private ListView _settingListView;
    private SettingListAdapter _settingListAdapter;


    /*** 根据Item的position位置来判断具体跳转至哪个Activity */

    /*** 根据Item的position位置来判断具体跳转至哪个Activity */
    private void toNewActivity(int position) {
        Intent i;
        switch (position) {
            case 0:
                i = new Intent(SettingsMenuActivity.this, OpinionLastActivity.class);
                break;
            case 1:
                i = new Intent(SettingsMenuActivity.this, GeneralSettingActivity.class);
                break;
            case 2:
                i = new Intent(SettingsMenuActivity.this, OpinionActivity.class);
                break;
            case 3:
                i = new Intent(SettingsMenuActivity.this, AboutAppActivity.class);
                break;

            default:
                i = new Intent(SettingsMenuActivity.this, SettingsMenuActivity.class);
                break;
        }
        startActivity(i);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_menu);

        processView();

        processController();

    }

    private void processController() {
        _logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        // ListView Item Click Listener
        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                toNewActivity(position);
            }
        };

        _settingListView.setOnItemClickListener(itemListener);
    }

    private void processView() {
        // Set up on Top Toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(getString(R.string.bar_title_settings));

        _logoutButton = (Button) findViewById(R.id.logout_button);
        _settingListView = (ListView) findViewById(R.id.setting_list);

        _settingItemList.add(new SettingItem(getString(R.string.title_edit_personal), "ic_edit_personal"));
        _settingItemList.add(new SettingItem(getString(R.string.title_edit_general), "ic_general_setting"));
//        _settingItemList.add(new SettingItem(getString(R.string.title_edit_privacy), "ic_privacy"));
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

    private void logout() {
        String json = _apiJsonFactory.getLogoutJson(_sharedFileHandler.retreiveUserSession(SettingsMenuActivity.this), _sharedFileHandler.retreiveUserID(SettingsMenuActivity.this));
        System.out.println(json);
        HttpConnectionTool httpConnectionTool = new HttpConnectionTool();
        httpConnectionTool.jsonPostMethod(new URLFactory().getLogoutURL(), json, new HttpConnectionToolCallback() {
            @Override
            public void onSuccess(String result) {
                final SimpleHttpResponse simpleHttpResponse = _apiJsonFactory.parseSimpleHttpResponse(result);
                System.out.println(result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        _sharedFileHandler.saveUserSession(SettingsMenuActivity.this, "", "");
//                            Toast.makeText(SettingsMenuActivity.this, "已登出", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent();
                        intent.putExtra("LOGOUT", true);
                        setResult(ActivityManager.SETTINGS_MENU_ACTIVITY.getValue(), intent);
                        finish();
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("LOGOUT", false);
        setResult(ActivityManager.SETTINGS_MENU_ACTIVITY.getValue(), intent);
        finish();
    }
}
