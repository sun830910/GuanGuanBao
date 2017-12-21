package com.enjoygreenlife.guanguanbao.controller.scanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.enjoygreenlife.guanguanbao.model.ApiModel.ApiJsonFactory;
import com.enjoygreenlife.guanguanbao.model.ApiModel.SharedFileHandler;
import com.enjoygreenlife.guanguanbao.model.ApiModel.URLFactory;
import com.enjoygreenlife.guanguanbao.model.DataModel.QRCodeStructure;
import com.enjoygreenlife.guanguanbao.model.DataModel.ScanQRCodeResponse;
import com.enjoygreenlife.guanguanbao.model.DataModel.ScanQRCodeResult;
import com.enjoygreenlife.guanguanbao.model.ViewModel.References.ActivityManager;
import com.enjoygreenlife.guanguanbao.tool.httpConnectionTool.HttpConnectionTool;
import com.enjoygreenlife.guanguanbao.tool.httpConnectionTool.HttpConnectionToolCallback;
import com.google.gson.Gson;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BaseScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private final SharedFileHandler _sharedFileHandler = new SharedFileHandler();
    private final ApiJsonFactory _apiJsonFactory = new ApiJsonFactory();
    private ZXingScannerView mScannerView;
    private Class<?> mClss;
    private ScanQRCodeResult _scanQRCodeResult = new ScanQRCodeResult();

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            closeActivity(false);
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

    public void launchActivity(Class<?> className) {
        System.out.println("LAUNCH SCAN RESULT");
        mClss = className;
        if (mClss.equals(ScannerResultActivity.class)) {
            Intent intent = new Intent(BaseScannerActivity.this, className);
            intent.putExtra("NUMBER_OF_BOTTLES", _scanQRCodeResult.getTotalNums());
            intent.putExtra("WEIGHT_OF_BOTTLES", _scanQRCodeResult.getTotalWeight());
            intent.putExtra("WEIGHT_OF_COALS", _scanQRCodeResult.getTotalCoals());
            intent.putExtra("POINTS", _scanQRCodeResult.getTotalPoint());
            startActivityForResult(intent, ActivityManager.SCANNER_RESULT_ACTIVITY.getValue());
        }
    }

    // Call Back method  to get the Message form other Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityManager.SCANNER_RESULT_ACTIVITY.getValue()) {
            closeActivity(true);
        }
    }

    @Override
    public void handleResult(Result rawResult) {
//        System.out.println("RESULT " + rawResult.getText());
        QRCodeStructure qrCodeStructure = new Gson().fromJson(rawResult.getText(), QRCodeStructure.class);
        String session = _sharedFileHandler.retreiveUserSession(BaseScannerActivity.this);
        String userID = _sharedFileHandler.retreiveUserID(BaseScannerActivity.this);
        getScannerResult(session, userID, qrCodeStructure.getQRCode(), qrCodeStructure.getQRcodeImg(), qrCodeStructure.getOrderNo());

//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mScannerView.resumeCameraPreview(BaseScannerActivity.this);
//            }
//        }, 2000);
    }

    private void getScannerResult(String session, String userID, String QRCode, String QRCodeImg, String orderNumber) {

        String json = _apiJsonFactory.scanQRcodeJson(session, userID, QRCode, QRCodeImg, orderNumber);
        System.out.println(json);
        // Call Connection Tool to process login
        HttpConnectionTool httpConnectionTool = new HttpConnectionTool();
        httpConnectionTool.postMethod(new URLFactory().scanQRcodeURL(), json, new HttpConnectionToolCallback() {
            @Override
            public void onSuccess(String result) {
                final ScanQRCodeResponse scanQRCodeResponse = _apiJsonFactory.parseScanQRCodeResponse(result);
//                System.out.println("SCANNER++++++++" + result);
                if (scanQRCodeResponse.getCode() == 1) {
                    _scanQRCodeResult = scanQRCodeResponse.getData();
                    launchActivity(ScannerResultActivity.class);
                } else if (scanQRCodeResponse.getCode() == -5) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(BaseScannerActivity.this, "二維碼已失效", Toast.LENGTH_LONG).show();
                            mScannerView.resumeCameraPreview(BaseScannerActivity.this);
                        }
                    });
                } else {
                    closeActivity(false);
                }
            }
        });
    }

    private void closeActivity(boolean resultCheck) {
        Intent intent = new Intent();
        intent.putExtra("SUCCESS", resultCheck);
        setResult(ActivityManager.BASESCANNER_ACTIVITY.getValue(), intent);
        finish();
    }
}
