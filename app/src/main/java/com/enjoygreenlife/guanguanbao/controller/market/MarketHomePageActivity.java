package com.enjoygreenlife.guanguanbao.controller.market;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.enjoygreenlife.guanguanbao.R;
import com.enjoygreenlife.guanguanbao.model.ViewModel.References.ActivityManager;

public class MarketHomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_home_page);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        setResult(ActivityManager.MARKET_HOME_ACTIVITY.getValue(), intent);
        finish();
    }
}
