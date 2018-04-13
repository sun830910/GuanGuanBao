package com.enjoygreenlife.guanguanbao.controller.scanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.enjoygreenlife.guanguanbao.R;
import com.enjoygreenlife.guanguanbao.model.DataModel.RecycleInvoice;
import com.enjoygreenlife.guanguanbao.model.DataModel.ScanQRCodeResult;
import com.enjoygreenlife.guanguanbao.model.ViewModel.References.ActivityManager;

public class ScannerResultActivity extends AppCompatActivity {
    private TextView _numCountTextView;
    private TextView _weightCountTextView;
    private TextView _coalCountTextView;
    private TextView _pointsCountTextView;
    private Button _okButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_result);

        processView();
        processController();
        processData();
    }

    private void processController() {
        _okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity();
            }
        });
    }

    private void processView() {
        _numCountTextView = (TextView) findViewById(R.id.content_numCount_scanner_success);
        _weightCountTextView = (TextView) findViewById(R.id.content_weightCount_scanner_success);
        _coalCountTextView = (TextView) findViewById(R.id.content_coalCount_scanner_success);
        _pointsCountTextView = (TextView) findViewById(R.id.content_point_scanner_success);
        _okButton = (Button) findViewById(R.id.scanner_ok_button);
    }

    private void processData() {

        String resultStrNumber = getText(R.string.content_numCount_scanner_success_prefix) + getIntent().getStringExtra("NUMBER_OF_BOTTLES") + getText(R.string.content_numCount_scanner_success_suffix);
        String resultStrWeightCount = getText(R.string.content_weightCount_scanner_success_prefix) + getIntent().getStringExtra("WEIGHT_OF_BOTTLES") + "g";
        String resultStrCoalCount = getText(R.string.content_coalCount_scanner_success_prefix) + getIntent().getStringExtra("WEIGHT_OF_COALS") + getText(R.string.content_coalCount_scanner_success_suffix);
        String resultStrPointCount = getText(R.string.content_point_scanner_success_prefix) + getIntent().getStringExtra("POINTS") + getText(R.string.content_point_scanner_success_suffix);

        _numCountTextView.setText(resultStrNumber);
        _weightCountTextView.setText(resultStrWeightCount);
        _coalCountTextView.setText(resultStrCoalCount);
        _pointsCountTextView.setText(resultStrPointCount);
    }

    @Override
    public void onBackPressed() {
        closeActivity();
    }

    private void closeActivity() {
        Intent intent = new Intent();
        intent.putExtra("SUCCESS", true);
        setResult(ActivityManager.SCANNER_RESULT_ACTIVITY.getValue(), intent);
        finish();
    }
}
