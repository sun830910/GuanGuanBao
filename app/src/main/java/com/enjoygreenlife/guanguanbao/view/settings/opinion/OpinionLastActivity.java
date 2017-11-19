package com.enjoygreenlife.guanguanbao.view.settings.opinion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.enjoygreenlife.guanguanbao.R;
import com.enjoygreenlife.guanguanbao.view.home.HomeActivity;
import com.enjoygreenlife.guanguanbao.view.settings.SettingsMenuActivity;

public class OpinionLastActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opinion_last);
        processView();
        Button submit = (Button) findViewById(R.id.button_back);// 取得按鈕物件

        submit.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View arg0) {
                //判斷條件 有輸入值才執行

                    Intent intent = new Intent();
                    intent.setClass(OpinionLastActivity.this, HomeActivity.class);
                    Bundle bundle = new Bundle();

                    intent.putExtras(bundle);   // 記得put進去，不然資料不會帶過去哦

                    startActivity(intent);

            }
        });

    }

    private void processView() {
        // Set up on Top Toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(getString(R.string.opinion_title));
    }
}
