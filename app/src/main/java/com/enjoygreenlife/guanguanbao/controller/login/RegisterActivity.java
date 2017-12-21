package com.enjoygreenlife.guanguanbao.controller.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.enjoygreenlife.guanguanbao.R;
import com.enjoygreenlife.guanguanbao.model.ApiModel.ApiJsonFactory;
import com.enjoygreenlife.guanguanbao.model.ApiModel.URLFactory;
import com.enjoygreenlife.guanguanbao.model.DataModel.SimpleHttpResponse;
import com.enjoygreenlife.guanguanbao.tool.httpConnectionTool.HttpConnectionTool;
import com.enjoygreenlife.guanguanbao.tool.httpConnectionTool.HttpConnectionToolCallback;

public class RegisterActivity extends AppCompatActivity {

    private EditText _editTextAccount;
    private EditText _editTextPhone;
    private EditText _editTextMail;
    private EditText _editTextPassword;
    private EditText _editTextConfirmPassword;
    private CheckBox _agreeCheck;
    private Button _submitButton;
    private final ApiJsonFactory _apiJsonFactory = new ApiJsonFactory();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        processView();
        processController();
    }

    private void processView() {
        setContentView(R.layout.activity_register);
        _editTextAccount = (EditText) findViewById(R.id.input_register_account);
        _editTextPhone = (EditText) findViewById(R.id.input_register_phoneNumber);
        _editTextMail = (EditText) findViewById(R.id.input_register_mail);
        _editTextPassword = (EditText) findViewById(R.id.input_register_password);
        _editTextConfirmPassword = (EditText) findViewById(R.id.input_register_confirmPassword);
        _agreeCheck = (CheckBox) findViewById(R.id.checkBox_agreement);
        _submitButton = (Button) findViewById(R.id.register_submit);
        _submitButton.setEnabled(checkSubmitEnabled());
    }

    private void processController() {
        _editTextAccount.addTextChangedListener(getInputTextWatcher());
        _editTextPhone.addTextChangedListener(getInputTextWatcher());
        _editTextMail.addTextChangedListener(getInputTextWatcher());
        _editTextPassword.addTextChangedListener(getInputTextWatcher());
        _editTextConfirmPassword.addTextChangedListener(getInputTextWatcher());
        _agreeCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                _submitButton.setEnabled(checkSubmitEnabled());
            }
        });
        _submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RegisterActivity.this, "點擊註冊", Toast.LENGTH_LONG).show();
            }
        });
    }

    private TextWatcher getInputTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                _submitButton.setEnabled(checkSubmitEnabled());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }

    private boolean checkSubmitEnabled() {
        if (_editTextAccount.getText().length() == 0) {
            return false;
        } else if (_editTextPhone.getText().length() == 0) {
            return false;
        } else if (_editTextMail.getText().length() == 0) {
            return false;
        } else if (_editTextPassword.getText().length() == 0) {
            return false;
        } else if (_editTextConfirmPassword.getText().length() == 0) {
            return false;
        } else if (!_agreeCheck.isChecked()) {
            return false;
        } else {
            return true;
        }
    }

    private void checkInputData() {

    }

    private void submit() {
        String account = _editTextAccount.getText().toString();
        String password = _editTextPassword.getText().toString();
        String email = _editTextMail.getText().toString();
        String phone = _editTextPhone.getText().toString();
        String json = new ApiJsonFactory().getRegisterJson(account, password, phone, email);

        // Call Connection Tool to process login
        HttpConnectionTool httpConnectionTool = new HttpConnectionTool();
        httpConnectionTool.postMethod(new URLFactory().getRegisterURL(), json, new HttpConnectionToolCallback() {
            @Override
            public void onSuccess(String result) {
                final SimpleHttpResponse simpleHttpResponse = _apiJsonFactory.parseSimpleHttpResponse(result);
                System.out.println(result);
                if (simpleHttpResponse.getCode() == 1) {
                    // Update UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent();
                            intent.putExtra("REGISTER_SUCCESS", true);
                            setResult(9999, intent);
                            finish();
                        }
                    });
                } else {
                    Toast.makeText(RegisterActivity.this, "註冊失敗", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

