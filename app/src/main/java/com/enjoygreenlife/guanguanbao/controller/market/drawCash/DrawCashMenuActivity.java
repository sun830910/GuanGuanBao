package com.enjoygreenlife.guanguanbao.controller.market.drawCash;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.enjoygreenlife.guanguanbao.R;
import com.enjoygreenlife.guanguanbao.model.ApiModel.ApiJsonFactory;
import com.enjoygreenlife.guanguanbao.model.ApiModel.SharedFileHandler;
import com.enjoygreenlife.guanguanbao.model.ApiModel.URLFactory;
import com.enjoygreenlife.guanguanbao.model.DataModel.Item;
import com.enjoygreenlife.guanguanbao.model.DataModel.ItemResponse;
import com.enjoygreenlife.guanguanbao.model.ViewModel.DrawCashList.DrawCashItem;
import com.enjoygreenlife.guanguanbao.model.ViewModel.DrawCashList.DrawCashListAdapter;
import com.enjoygreenlife.guanguanbao.tool.httpConnectionTool.HttpConnectionTool;
import com.enjoygreenlife.guanguanbao.tool.httpConnectionTool.HttpConnectionToolCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DrawCashMenuActivity extends AppCompatActivity {

    private final ApiJsonFactory _apiJsonFactory = new ApiJsonFactory();
    private SharedFileHandler _sharedFileHandler = new SharedFileHandler();
    private List<DrawCashItem> _drawCashItemList = new ArrayList<DrawCashItem>();
    private ListView _drawCashListView;
    private DrawCashListAdapter _drawCashListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_cash_menu);
        processView();
        getCashItem();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void processView() {
        _drawCashListView = (ListView) findViewById(R.id.cash_selection_list);
    }

    private void getCashItem() {
        String session = _sharedFileHandler.retreiveUserSession(DrawCashMenuActivity.this);
        String userId = _sharedFileHandler.retreiveUserID(DrawCashMenuActivity.this);
        String json = new ApiJsonFactory().getItemJson(session, userId, 1);
        System.out.println(json);

        // Call Connection Tool to process login
        HttpConnectionTool httpConnectionTool = new HttpConnectionTool();
        httpConnectionTool.postMethod(new URLFactory().getItemURL(), json, new HttpConnectionToolCallback() {
            @Override
            public void onSuccess(String result) {
                System.out.println(result);
                final ItemResponse itemResponse = _apiJsonFactory.parseItemResponse(result);
                if (itemResponse.getCode() == 1) {
                    // Update UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshListView(itemResponse.getData());
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    });
                }
            }
        });
    }

    private void refreshListView(ArrayList<Item> itemArrayList) {
        _drawCashItemList.clear();
        for (Item item : itemArrayList) {
            String cost = String.format(Locale.getDefault(), "%.0f", item.getPrice());
            String realMoney = String.format(Locale.getDefault(), "%.2f", item.getRealMoney());
            _drawCashItemList.add(new DrawCashItem(cost, realMoney));
            System.out.println(cost + "---" + realMoney);
        }
        _drawCashListAdapter = new DrawCashListAdapter(getApplicationContext(), _drawCashItemList);
        _drawCashListView.setAdapter(_drawCashListAdapter);
    }
}
