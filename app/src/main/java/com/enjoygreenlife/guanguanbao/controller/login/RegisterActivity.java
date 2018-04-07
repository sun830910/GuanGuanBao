package com.enjoygreenlife.guanguanbao.controller.login;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
import com.enjoygreenlife.guanguanbao.model.ViewModel.References.ActivityManager;
import com.enjoygreenlife.guanguanbao.tool.httpConnectionTool.HttpConnectionTool;
import com.enjoygreenlife.guanguanbao.tool.httpConnectionTool.HttpConnectionToolCallback;

import java.io.IOException;

import okhttp3.Call;

public class RegisterActivity extends AppCompatActivity {

    private final ApiJsonFactory _apiJsonFactory = new ApiJsonFactory();
    private EditText _editTextAccount;
    private EditText _editTextPhone;
    private EditText _editTextMail;
    private EditText _editTextPassword;
    private EditText _editTextConfirmPassword;
    private CheckBox _agreeCheck;
    private Button _submitButton;

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
                checkInputData();
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
                if (_editTextAccount.getText().hashCode() == charSequence.hashCode()) {
                    _editTextAccount.getBackground().clearColorFilter();
                } else if (_editTextPhone.getText().hashCode() == charSequence.hashCode()) {
                    _editTextPhone.getBackground().clearColorFilter();
                } else if (_editTextMail.getText().hashCode() == charSequence.hashCode()) {
                    _editTextMail.getBackground().clearColorFilter();
                } else if (_editTextPassword.getText().hashCode() == charSequence.hashCode()) {
                    _editTextPassword.getBackground().clearColorFilter();
                } else if (_editTextConfirmPassword.getText().hashCode() == charSequence.hashCode()) {
                    _editTextConfirmPassword.getBackground().clearColorFilter();
                }
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
        if (!_editTextAccount.getText().toString().matches("[a-zA-Z\\d]{6,20}$")) {
            _editTextAccount.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            setFocusToEditText(_editTextAccount);
            Toast.makeText(RegisterActivity.this, "帳號格式不相符", Toast.LENGTH_LONG).show();
            return;
        } else if (!_editTextPassword.getText().toString().matches("[a-z0-9A-Z\\d]{6,20}$")) {
            _editTextPassword.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            setFocusToEditText(_editTextPassword);
            Toast.makeText(RegisterActivity.this, "密碼格式不相符", Toast.LENGTH_LONG).show();
            return;
        } else if (!_editTextPhone.getText().toString().matches("13[\\d]{9}$|14[\\d]{9}$|15[\\d]{9}$|17[\\d]{9}$|18[\\d]{9}$")) {
            _editTextPhone.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            setFocusToEditText(_editTextPhone);
            Toast.makeText(RegisterActivity.this, "電話格式不相符", Toast.LENGTH_LONG).show();
            return;
        } else if (!_editTextMail.getText().toString().matches("\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$")) {
            _editTextMail.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            setFocusToEditText(_editTextMail);
            Toast.makeText(RegisterActivity.this, "郵箱格式不相符", Toast.LENGTH_LONG).show();
            return;
        } else if (!_editTextPassword.getText().toString().equals(_editTextConfirmPassword.getText().toString())) {
            Toast.makeText(RegisterActivity.this, "密碼不相符", Toast.LENGTH_LONG).show();
            _editTextPassword.setText("");
            _editTextPassword.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            _editTextConfirmPassword.setText("");
            _editTextConfirmPassword.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            setFocusToEditText(_editTextConfirmPassword);
            return;
        } else {
            submit();
        }
    }

    private void setFocusToEditText(EditText e) {
        if (e.equals(_editTextAccount)) {
            _editTextAccount.requestFocus();
            _editTextPhone.clearFocus();
            _editTextMail.clearFocus();
            _editTextPassword.clearFocus();
            _editTextConfirmPassword.clearFocus();
        } else if (e.equals(_editTextPhone)) {
            _editTextAccount.clearFocus();
            _editTextPhone.requestFocus();
            _editTextMail.clearFocus();
            _editTextPassword.clearFocus();
            _editTextConfirmPassword.clearFocus();
        } else if (e.equals(_editTextMail)) {
            _editTextAccount.clearFocus();
            _editTextPhone.clearFocus();
            _editTextMail.requestFocus();
            _editTextPassword.clearFocus();
            _editTextConfirmPassword.clearFocus();
        } else if (e.equals(_editTextPassword)) {
            _editTextAccount.clearFocus();
            _editTextPhone.clearFocus();
            _editTextMail.clearFocus();
            _editTextPassword.requestFocus();
            _editTextConfirmPassword.clearFocus();
        } else if (e.equals(_editTextConfirmPassword)) {
            _editTextAccount.clearFocus();
            _editTextPhone.clearFocus();
            _editTextMail.clearFocus();
            _editTextPassword.clearFocus();
            _editTextConfirmPassword.requestFocus();
        }
    }

    private void submit() {
        String account = _editTextAccount.getText().toString();
        String password = _editTextPassword.getText().toString();
        String email = _editTextMail.getText().toString();
        String phone = _editTextPhone.getText().toString();
        String json = new ApiJsonFactory().getRegisterJson(account, password, phone, email);
        System.out.println(json);

        // Call Connection Tool to process login
        HttpConnectionTool httpConnectionTool = new HttpConnectionTool();
        httpConnectionTool.jsonPostMethod(new URLFactory().getRegisterURL(), json, new HttpConnectionToolCallback() {
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
                            setResult(ActivityManager.REGISTER_ACTIVITY.getValue(), intent);
                            finish();
                        }
                    });
                } else {
                    Toast.makeText(RegisterActivity.this, "註冊失敗", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("REGISTER_SUCCESS", false);
        setResult(ActivityManager.REGISTER_ACTIVITY.getValue(), intent);
        finish();
    }
}

