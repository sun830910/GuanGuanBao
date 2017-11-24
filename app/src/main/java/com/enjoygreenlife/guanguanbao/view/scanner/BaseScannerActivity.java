package com.enjoygreenlife.guanguanbao.view.scanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.enjoygreenlife.guanguanbao.model.ApiModel.ApiJsonFactory;
import com.enjoygreenlife.guanguanbao.model.ApiModel.SharedFileHandler;
import com.enjoygreenlife.guanguanbao.model.ApiModel.URLFactory;
import com.enjoygreenlife.guanguanbao.model.DataModel.QRCodeStructure;
import com.enjoygreenlife.guanguanbao.model.DataModel.ScanQRCodeResponse;
import com.enjoygreenlife.guanguanbao.model.DataModel.ScanQRCodeResult;
import com.enjoygreenlife.guanguanbao.tool.HttpConnectionTool;
import com.enjoygreenlife.guanguanbao.tool.HttpConnectionToolCallback;
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

    public void launchActivity(Class<?> className) {
        mClss = className;
        Intent intent = new Intent(this, className);
        intent.putExtra("NUMBER_OF_BOTTLES", _scanQRCodeResult.getTotalNums());
        intent.putExtra("WEIGHT_OF_BOTTLES", _scanQRCodeResult.getTotalWeight());
        intent.putExtra("WEIGHT_OF_COALS", _scanQRCodeResult.getTotalCoals());
        intent.putExtra("POINTS", _scanQRCodeResult.getTotalPoint());
        if (mClss.equals(ScannerResultActivity.class)) {
            startActivityForResult(intent, 1001);
        }
    }

    // Call Back method  to get the Message form other Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            Intent intent = new Intent();
            intent.putExtra("SUCCESS", true);
//        intent.putExtra("MESSAGE", "Contents = " + rawResult.getText() +
//                ", Format = " + rawResult.getBarcodeFormat().toString());
            setResult(999, intent);
            finish();
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

        System.out.println("RESULT " + rawResult.getText());
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
                System.out.println(result);
                if (scanQRCodeResponse.getCode() == 1) {
                    _scanQRCodeResult = scanQRCodeResponse.getData();
                    launchActivity(ScannerResultActivity.class);
//                    // Update UI
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            Toast.makeText(BaseScannerActivity.this, "SCANNER SUCCESS", Toast.LENGTH_LONG).show();
//                        }
//                    });
                } else {
                    mScannerView.resumeCameraPreview(BaseScannerActivity.this);
                }
            }
        });
    }
}
