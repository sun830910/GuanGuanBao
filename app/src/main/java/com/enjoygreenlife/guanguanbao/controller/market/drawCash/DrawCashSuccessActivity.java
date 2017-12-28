package com.enjoygreenlife.guanguanbao.controller.market.drawCash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.enjoygreenlife.guanguanbao.R;
import com.enjoygreenlife.guanguanbao.model.ViewModel.References.ActivityManager;

public class DrawCashSuccessActivity extends AppCompatActivity {

    private Button _backButton;
    private TextView _successMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_cash_success);
        _backButton = (Button) findViewById(R.id.exchage_cash_success_ok_btn);
        _successMsg = (TextView) findViewById(R.id.exchange_cash_success_msg);
        String msg = String.format("恭喜您以%s积分\n成功提现%s元", getIntent().getStringExtra("COST"), getIntent().getStringExtra("MONEY"));
        _successMsg.setText(msg);
        _backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToParent();
            }
        });
    }

    @Override
    public void onBackPressed() {
        backToParent();
    }

    private void backToParent() {
        Intent intent = new Intent();
        setResult(ActivityManager.MARKET_DRAW_CASH_SUCCESS_ACTIVITY.getValue(), intent);
        finish();
    }

}
