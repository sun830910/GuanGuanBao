package com.enjoygreenlife.guanguanbao.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;

import com.enjoygreenlife.guanguanbao.R;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BaseScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            Intent intent = new Intent();
            intent.putExtra("SUCCESS",false);
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
    public void handleResult(Result rawResult) {
        Intent intent = new Intent();
        intent.putExtra("SUCCESS",true);
        intent.putExtra("MESSAGE", "Contents = " + rawResult.getText() +
                ", Format = " + rawResult.getBarcodeFormat().toString());
        setResult(999, intent);
        finish();

        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mScannerView.resumeCameraPreview(BaseScannerActivity.this);
//            }
//        }, 2000);
    }
}
