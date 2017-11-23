package com.enjoygreenlife.guanguanbao.view.scanner;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.enjoygreenlife.guanguanbao.model.ApiModel.ApiJsonFactory;
import com.enjoygreenlife.guanguanbao.model.ApiModel.SharedFileHandler;
import com.enjoygreenlife.guanguanbao.model.ApiModel.URLFactory;
import com.enjoygreenlife.guanguanbao.model.DataModel.QRCodeStructure;
import com.enjoygreenlife.guanguanbao.model.DataModel.UserLoginResponse;
import com.enjoygreenlife.guanguanbao.tool.HttpConnectionTool;
import com.enjoygreenlife.guanguanbao.tool.HttpConnectionToolCallback;
import com.enjoygreenlife.guanguanbao.view.login.LoginActivity;
import com.google.gson.Gson;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BaseScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private final SharedFileHandler _sharedFileHandler = new SharedFileHandler();
    private final ApiJsonFactory _apiJsonFactory = new ApiJsonFactory();
    private ZXingScannerView mScannerView;
    private Class<?> mClss;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent();
            intent.putExtra("SUCCESS", false);
            setResult(999, intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 999) {
            if (data.getBooleanExtra("SUCCESS", false)) {
                String message = data.getStringExtra("MESSAGE");
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            } else {
                System.out.println("CLOSE");
            }
        }
    }

    public void launchActivity(Class<?> className) {
        mClss = className;
        Intent intent = new Intent(this, className);
        intent.putExtra("NUMBER_OF_BOTTLES", true);
        intent.putExtra("WEIGHT_OF_BOTTLES", true);
        intent.putExtra("WEIGHT_OF_COALS", true);
        intent.putExtra("POINTS", true);
        if (mClss.equals(ScannerResultActivity.class)) {
            startActivityForResult(intent, 999);
        }
    }

    @Override
    public void handleResult(Result rawResult) {
//        Intent intent = new Intent();
//        intent.putExtra("SUCCESS", true);
//        intent.putExtra("MESSAGE", "Contents = " + rawResult.getText() +
//                ", Format = " + rawResult.getBarcodeFormat().toString());
//        setResult(999, intent);
//        finish();

        System.out.print("RESULT " + rawResult.getText());
        QRCodeStructure qrCodeStructure = new Gson().fromJson(rawResult.getText(), QRCodeStructure.class);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(BaseScannerActivity.this);
            }
        }, 2000);
    }

    private void getScannerResult(String session, String userID, String QRCode, String QRCodeImg, String orderNumber) {

//        String json = _apiJsonFactory.scanQRcodeJson(String session, String userID, String QRCode, String QRCodeImg, String orderNumber);
        String json = "";
        // Call Connection Tool to process login
        HttpConnectionTool httpConnectionTool = new HttpConnectionTool();
        httpConnectionTool.postMethod(new URLFactory().scanQRcodeURL(), json, new HttpConnectionToolCallback() {
            @Override
            public void onSuccess(String result) {
                final UserLoginResponse userLoginResponse = _apiJsonFactory.parseUserLoginResponse(result);
                System.out.println(result);
                if (userLoginResponse.getCode() == 1) {

//                    //Store session to SharedPreferences
//                    SharedFileHandler sharedFileHandler = new SharedFileHandler();
//                    sharedFileHandler.saveUserSession(BaseScannerActivity.this, userLoginResponse);

                    // Update UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(BaseScannerActivity.this, "SCANNER SUCCESS", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    launchActivity(LoginActivity.class);
                }
            }
        });
    }
}
