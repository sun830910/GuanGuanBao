package com.enjoygreenlife.guanguanbao.controller.market;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.enjoygreenlife.guanguanbao.R;
import com.enjoygreenlife.guanguanbao.controller.market.drawCash.DrawCashMenuActivity;
import com.enjoygreenlife.guanguanbao.model.ApiModel.ApiJsonFactory;
import com.enjoygreenlife.guanguanbao.model.ApiModel.SharedFileHandler;
import com.enjoygreenlife.guanguanbao.model.ApiModel.URLFactory;
import com.enjoygreenlife.guanguanbao.model.DataModel.UserLoginResponse;
import com.enjoygreenlife.guanguanbao.model.ViewModel.References.ActivityManager;
import com.enjoygreenlife.guanguanbao.tool.httpConnectionTool.HttpConnectionTool;
import com.enjoygreenlife.guanguanbao.tool.httpConnectionTool.HttpConnectionToolCallback;

import java.util.Locale;

public class MarketHomePageActivity extends AppCompatActivity {

    private LinearLayout _mDrawMoneyLayout;
    private TextView _mUserName;
    private TextView _mUserPoints;
    private final ApiJsonFactory _apiJsonFactory = new ApiJsonFactory();
    private SharedFileHandler _sharedFileHandler = new SharedFileHandler();
    private Class<?> mClss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_home_page);
        processView();
        getUserData(_sharedFileHandler.retreiveUserSession(MarketHomePageActivity.this), _sharedFileHandler.retreiveUserID(MarketHomePageActivity.this));
    }

    private void processView() {
        _mUserName = (TextView) findViewById(R.id.market_user_name);
        _mUserPoints = (TextView) findViewById(R.id.market_user_points);

        _mDrawMoneyLayout = (LinearLayout) findViewById(R.id.layout_draw_money);
        _mDrawMoneyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity(DrawCashMenuActivity.class);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        setResult(ActivityManager.MARKET_HOME_ACTIVITY.getValue(), intent);
        finish();
    }

    public void launchActivity(Class<?> className) {
        mClss = className;
        Intent intent = new Intent(this, className);
        if (mClss.equals(DrawCashMenuActivity.class)) {
            startActivityForResult(intent, ActivityManager.MARKET_DRAW_CASH_ACTIVITY.getValue());
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == ActivityManager.MARKET_DRAW_CASH_ACTIVITY.getValue()) {
            getUserData(_sharedFileHandler.retreiveUserSession(MarketHomePageActivity.this), _sharedFileHandler.retreiveUserID(MarketHomePageActivity.this));
        }
    }

    private void getUserData(String session, String userID) {
        String json = _apiJsonFactory.getUserInfoJson(session, userID);
        // Call Connection Tool to process login
        HttpConnectionTool httpConnectionTool = new HttpConnectionTool();
        httpConnectionTool.postMethod(new URLFactory().getUerInfoURL(), json, new HttpConnectionToolCallback() {
            @Override
            public void onSuccess(String result) {
                final UserLoginResponse userLoginResponse = _apiJsonFactory.parseUserLoginResponse(result);
                System.out.println(result);
                if (userLoginResponse.getCode() == 1) {

                    // Update UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                          _progress.setVisibility(View.INVISIBLE);
                            _mUserName.setText(userLoginResponse.getUser().getUserName());

                            String totalPointsStr = String.format(Locale.getDefault(), "%.0f", userLoginResponse.getUser().getWallet()) + getResources().getString(R.string.unit_draw_cash_points);
                            _mUserPoints.setText(totalPointsStr);
                        }
                    });
                } else {
                    finish();
                }
            }
        });
    }
}
