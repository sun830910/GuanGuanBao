package com.enjoygreenlife.guanguanbao.controller.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.enjoygreenlife.guanguanbao.R;
import com.enjoygreenlife.guanguanbao.model.ApiModel.ApiJsonFactory;
import com.enjoygreenlife.guanguanbao.model.ApiModel.SharedFileHandler;
import com.enjoygreenlife.guanguanbao.model.ApiModel.URLFactory;
import com.enjoygreenlife.guanguanbao.model.DataModel.UserLoginResponse;
import com.enjoygreenlife.guanguanbao.tool.httpConnectionTool.HttpConnectionTool;
import com.enjoygreenlife.guanguanbao.tool.httpConnectionTool.HttpConnectionToolCallback;

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

        // Call Connection Tool to process login
        HttpConnectionTool httpConnectionTool = new HttpConnectionTool();
        httpConnectionTool.postMethod(new URLFactory().getLoginURL(), json, new HttpConnectionToolCallback() {
            @Override
            public void onSuccess(String result) {
                System.out.println(result);
                UserLoginResponse userLoginResponse = new ApiJsonFactory().parseUserLoginResponse(result);
                String session = userLoginResponse.getSession();
//                System.out.println("PARSE+SESSION => " + session);

                if (userLoginResponse.getCode() == 1) {
                    //Store session to SharedPreferences
                    SharedFileHandler sharedFileHandler = new SharedFileHandler();
                    sharedFileHandler.saveUserSession(LoginActivity.this, session, userLoginResponse.getUser().getId());

                    // Update UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            _progress.setVisibility(View.INVISIBLE);
//                            Toast.makeText(LoginActivity.this, "登入成功", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent();
                            intent.putExtra("LOGIN_SUCCESS", true);
                            setResult(999, intent);
                            finish();
                        }
                    });
                } else {
                    // Update UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            _progress.setVisibility(View.INVISIBLE);
                            Toast.makeText(LoginActivity.this, getText(R.string.error_login_fail), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        processView();
        processController();
    }

    @Override
    public void onBackPressed() {
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
                } else {
                    Toast.makeText(LoginActivity.this, getText(R.string.hint_username_password_input_null), Toast.LENGTH_LONG).show();
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

    /*
    * Check Input Data
    */
    private boolean checkInputValue() {
        if (_userName.getText().length() != 0 & _userPassword.getText().length() != 0) {
            return true;
        } else {
            return false;
        }
    }

}
