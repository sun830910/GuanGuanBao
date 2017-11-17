package com.enjoygreenlife.guanguanbao.view.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.enjoygreenlife.guanguanbao.R;
import com.enjoygreenlife.guanguanbao.model.DataModel.UserLoginResponse;
import com.enjoygreenlife.guanguanbao.model.URLFactory;
import com.enjoygreenlife.guanguanbao.tool.HttpConnectionTool;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity {


    private TextView _userName;
    private TextView _userPassword;
    private Button _loginButton;
    private TextView _register;
    private TextView _forgetPassword;

    private void login() {
        String json = "{\"route\":\"login\",\"member\":{\"userName\":\"cliff6\",\"userPassword\":\"cliffk123\"}}";
        HttpConnectionTool httpConnectionTool = new HttpConnectionTool();
        httpConnectionTool.postMethod(new URLFactory().getLoginURL(), json);
    }


    private void parseResponse(){
        String json = "{\"code\":1,\"data\":{\"id\":\"1\",\"userName\":\"cliff6\",\"profilePhoto\":null,\"wallet\":\"421.00\",\"lockingWallet\":\"0.00\",\"address\":null,\"phoneNumber\":\"15061844637\",\"birthday\":null,\"gender\":null,\"email\":\"luckynomber72003@gmail.com\",\"ali\":null,\"qq\":null,\"wechat\":null,\"rankId\":\"1\"},\"session\":\"$2y$10$LR94UcUZu0IMgVTRfSPWo.rXGiO0u8ac530NUeldTQ9iL3519EWlO\"}";
        Gson gson = new Gson();
        UserLoginResponse obj2 = gson.fromJson(json, UserLoginResponse.class);
        System.out.println(obj2.getUser().getGender());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        processView();
        processController();
    }

    private void processView() {
        _userName = (TextView) findViewById(R.id.userNameTextField);
        _userPassword = (TextView) findViewById(R.id.userPasswordTextField);
        _loginButton = (Button) findViewById(R.id.loginButton);
        _register = (TextView) findViewById(R.id.registerButton);
        _forgetPassword = (TextView) findViewById(R.id.forgetPassword);
    }

    private void processController() {
        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInputValue()) {
                    login();
//                   parseResponse();
//                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "不可為空！！", Toast.LENGTH_LONG).show();
                }
            }
        });

        _register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "CLICK ON Register", Toast.LENGTH_LONG).show();
            }
        });

        _forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "CLICK ON FORGET PASSWORD", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean checkInputValue() {
        if (_userName.getText().length() != 0 & _userPassword.getText().length() != 0) {
            return true;
        } else {
            return false;
        }
    }
}
