package com.enjoygreenlife.guanguanbao.view.settings.opinion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.enjoygreenlife.guanguanbao.R;

public class OpinionActivity extends AppCompatActivity {
    EditText ans;//宣告意見

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opinion);

        ans = (EditText) findViewById(R.id.et1);// 取得意見
        Button submit = (Button) findViewById(R.id.button);// 取得按鈕物件


        submit.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View arg0) {
                //判斷條件 有輸入值才執行
                if (!("".equals(ans.getText().toString()))) {
                    Intent intent = new Intent();
                    intent.setClass(OpinionActivity.this, OpinionLastActivity.class);
                    Bundle bundle = new Bundle();

                    intent.putExtras(bundle);   // 記得put進去，不然資料不會帶過去哦

                    startActivity(intent);
                }
            }
        });
    }
}
