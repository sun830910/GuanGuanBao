package com.enjoygreenlife.guanguanbao.controller.market.drawCash;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.enjoygreenlife.guanguanbao.R;
import com.enjoygreenlife.guanguanbao.model.ApiModel.ApiJsonFactory;
import com.enjoygreenlife.guanguanbao.model.ApiModel.SharedFileHandler;
import com.enjoygreenlife.guanguanbao.model.ApiModel.URLFactory;
import com.enjoygreenlife.guanguanbao.model.DataModel.Item;
import com.enjoygreenlife.guanguanbao.model.DataModel.ItemResponse;
import com.enjoygreenlife.guanguanbao.model.DataModel.SimpleHttpResponse;
import com.enjoygreenlife.guanguanbao.model.DataModel.UserLoginResponse;
import com.enjoygreenlife.guanguanbao.model.ViewModel.DrawCashList.DrawCashItem;
import com.enjoygreenlife.guanguanbao.model.ViewModel.DrawCashList.DrawCashListAdapter;
import com.enjoygreenlife.guanguanbao.model.ViewModel.References.ActivityManager;
import com.enjoygreenlife.guanguanbao.tool.httpConnectionTool.HttpConnectionTool;
import com.enjoygreenlife.guanguanbao.tool.httpConnectionTool.HttpConnectionToolCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class DrawCashMenuActivity extends AppCompatActivity {

    private final ApiJsonFactory _apiJsonFactory = new ApiJsonFactory();
    private SharedFileHandler _sharedFileHandler = new SharedFileHandler();
    private List<DrawCashItem> _drawCashItemList = new ArrayList<DrawCashItem>();
    private ArrayList<Item> _mItemArrayList = new ArrayList<Item>();
    private HashMap<Item, Integer> _shoppingCart = new HashMap<Item, Integer>();
    private ListView _drawCashListView;
    private DrawCashListAdapter _drawCashListAdapter;
    private AlertDialog.Builder builder;
    private Class<?> mClss;
    private String _selectedOptionCost;
    private String _selectedOptionRealmoney;
    private double _userPoints = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_cash_menu);
        processView();
        getCashItem();
        getUserData();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void processView() {
        _drawCashListView = (ListView) findViewById(R.id.cash_selection_list);
        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                selectOnItem(position);
            }
        };
        _drawCashListView.setOnItemClickListener(itemListener);
    }

    public void launchActivity(Class<?> className) {
        mClss = className;
        Intent intent = new Intent(this, className);
        if (mClss.equals(DrawCashSuccessActivity.class)) {
            intent.putExtra("COST", _selectedOptionCost);
            intent.putExtra("MONEY", _selectedOptionRealmoney);
            startActivityForResult(intent, ActivityManager.MARKET_DRAW_CASH_SUCCESS_ACTIVITY.getValue());
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == ActivityManager.MARKET_DRAW_CASH_SUCCESS_ACTIVITY.getValue()) {
            getUserData();
        }
    }

    private void selectOnItem(int position) {
        if (_userPoints >= _mItemArrayList.get(position).getPrice()) {
            showConfirmBuyingItem(position);
        } else {
            showNoEnoughMoneyWarning();
        }
    }

    private void showConfirmBuyingItem(final int position) {
        builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle(R.string.alert_confirm_buy_product_title);
        String message = getResources().getString(R.string.alert_confirm_buy_product_msg) + _drawCashItemList.get(position).getRealMoney() + getResources().getString(R.string.unit_draw_cash_money);
        builder.setMessage(message);

        //监听下方button点击事件
        builder.setPositiveButton(R.string.alert_confirm_buy_title, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                buyItem(_mItemArrayList.get(position), position);
//                Toast.makeText(getApplicationContext(), R.string.toast_postive, Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton(R.string.alert_cancel_buy_title, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                Toast.makeText(getApplicationContext(), R.string.toast_negative, Toast.LENGTH_SHORT).show();
            }
        });

        //设置对话框是可取消的
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showNoEnoughMoneyWarning() {
        builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle(R.string.alert_not_enough_point_title);
        builder.setMessage(R.string.alert_not_enough_points_msg);

        //监听下方button点击事件
        builder.setPositiveButton(R.string.alert_ok_title, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                Toast.makeText(getApplicationContext(), R.string.toast_postive, Toast.LENGTH_SHORT).show();
            }
        });

        //设置对话框是可取消的
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
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

    private void getUserData() {
        String session = _sharedFileHandler.retreiveUserSession(DrawCashMenuActivity.this);
        String userID = _sharedFileHandler.retreiveUserID(DrawCashMenuActivity.this);
        String json = _apiJsonFactory.getUserInfoJson(session, userID);
        // Call Connection Tool to process login
        HttpConnectionTool httpConnectionTool = new HttpConnectionTool();
        httpConnectionTool.postMethod(new URLFactory().getUerInfoURL(), json, new HttpConnectionToolCallback() {
            @Override
            public void onSuccess(String result) {
                final UserLoginResponse userLoginResponse = _apiJsonFactory.parseUserLoginResponse(result);
                System.out.println(result);
                if (userLoginResponse.getCode() == 1) {
                    // Update UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            _userPoints = userLoginResponse.getUser().getWallet();
                        }
                    });
                } else {
                    finish();
                }
            }
        });
    }

    private void buyItem(final Item item, final int position) {
        if (_shoppingCart.containsKey(item)) {
            _shoppingCart.put(item, _shoppingCart.get(item) + 1);
        } else {
            _shoppingCart.put(item, 1);
        }
        String session = _sharedFileHandler.retreiveUserSession(DrawCashMenuActivity.this);
        String userID = _sharedFileHandler.retreiveUserID(DrawCashMenuActivity.this);
        String json = _apiJsonFactory.buyItemsJson(session, userID, _shoppingCart);
        System.out.println(json);
        HttpConnectionTool httpConnectionTool = new HttpConnectionTool();
        httpConnectionTool.postMethod(new URLFactory().buyItemURL(), json, new HttpConnectionToolCallback() {
            @Override
            public void onSuccess(String result) {
                final SimpleHttpResponse simpleHttpResponse = _apiJsonFactory.parseSimpleHttpResponse(result);
                System.out.println(result);
                if (simpleHttpResponse.getCode() == 1) {
                    // Update UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            _shoppingCart.clear();
                            _selectedOptionCost = _drawCashItemList.get(position).getCost();
                            _selectedOptionRealmoney = _drawCashItemList.get(position).getRealMoney();
                            launchActivity(DrawCashSuccessActivity.class);
                        }
                    });
                } else if (simpleHttpResponse.getCode() == -202){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getUserData();
                            showNoEnoughMoneyWarning();
                        }
                    });
                }
            }
        });
    }

    private void refreshListView(ArrayList<Item> itemArrayList) {
        _drawCashItemList.clear();
        _mItemArrayList.clear();
        for (Item item : itemArrayList) {
            _mItemArrayList.add(item);
            String cost = String.format(Locale.getDefault(), "%.0f", item.getPrice());
            String realMoney = String.format(Locale.getDefault(), "%.2f", item.getRealMoney());
            _drawCashItemList.add(new DrawCashItem(cost, realMoney));
            System.out.println(cost + "---" + realMoney);
        }
        _drawCashListAdapter = new DrawCashListAdapter(getApplicationContext(), _drawCashItemList);
        _drawCashListView.setAdapter(_drawCashListAdapter);
    }
}
