package com.enjoygreenlife.guanguanbao.controller.market;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.enjoygreenlife.guanguanbao.R;
import com.enjoygreenlife.guanguanbao.controller.market.drawCash.DrawCashMenuActivity;
import com.enjoygreenlife.guanguanbao.model.ViewModel.References.ActivityManager;

public class MarketHomePageActivity extends AppCompatActivity {

    private LinearLayout _mDrawMoneyLayout;
    private Class<?> mClss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_home_page);
        processView();
    }

    private void processView() {
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
}
