package com.enjoygreenlife.guanguanbao.controller.market.drawCash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.enjoygreenlife.guanguanbao.R;
import com.enjoygreenlife.guanguanbao.model.ViewModel.References.ActivityManager;

public class DrawCashSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_cash_success);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        setResult(ActivityManager.MARKET_DRAW_CASH_SUCCESS_ACTIVITY.getValue(), intent);
        finish();
    }
}
