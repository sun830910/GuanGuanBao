package com.enjoygreenlife.guanguanbao.view.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.enjoygreenlife.guanguanbao.R;
import com.enjoygreenlife.guanguanbao.model.ApiModel.ApiJsonFactory;
import com.enjoygreenlife.guanguanbao.model.ApiModel.URLFactory;
import com.enjoygreenlife.guanguanbao.model.DataModel.UserLoginResponse;
import com.enjoygreenlife.guanguanbao.tool.HttpConnectionTool;
import com.enjoygreenlife.guanguanbao.tool.HttpConnectionToolCallback;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity {


    private TextView _userName;
    private TextView _userPassword;
    private Button _loginButton;
    private TextView _register;
    private TextView _forgetPassword;
    private ProgressBar _progress;

    private void login() {
        _progress.setVisibility(View.VISIBLE);
        String json = new ApiJsonFactory().getLoginJson(_userName.getText().toString(), _userPassword.getText().toString());
        HttpConnectionTool httpConnectionTool = new HttpConnectionTool();
        httpConnectionTool.postMethod(new URLFactory().getLoginURL(), json, new HttpConnectionToolCallback() {
            @Override
            public void onSuccess(String result) {
                UserLoginResponse userLoginResponse = parseResponse(result);
                if (userLoginResponse.getCode() == 1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            _progress.setVisibility(View.INVISIBLE);
                            Toast.makeText(LoginActivity.this, "登入成功", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            _progress.setVisibility(View.INVISIBLE);
                            Toast.makeText(LoginActivity.this, "登入失敗...請檢查帳號密碼", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    private UserLoginResponse parseResponse(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, UserLoginResponse.class);
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
        _progress = (ProgressBar) findViewById(R.id.progressBar);
        _progress.setVisibility(View.INVISIBLE);
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
