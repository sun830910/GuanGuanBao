package com.enjoygreenlife.guanguanbao.view.home;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.MyLocationStyle;
import com.enjoygreenlife.guanguanbao.R;
import com.enjoygreenlife.guanguanbao.model.ApiModel.ApiJsonFactory;
import com.enjoygreenlife.guanguanbao.model.ApiModel.SharedFileHandler;
import com.enjoygreenlife.guanguanbao.model.ApiModel.URLFactory;
import com.enjoygreenlife.guanguanbao.model.DataModel.UserLoginResponse;
import com.enjoygreenlife.guanguanbao.tool.HttpConnectionTool;
import com.enjoygreenlife.guanguanbao.tool.HttpConnectionToolCallback;
import com.enjoygreenlife.guanguanbao.view.login.LoginActivity;
import com.enjoygreenlife.guanguanbao.view.scanner.BaseScannerActivity;
import com.enjoygreenlife.guanguanbao.view.settings.SettingsMenuActivity;


public class HomeActivity extends AppCompatActivity implements AMap.OnMyLocationChangeListener {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final int ZXING_CAMERA_PERMISSION = 1;
    private final SharedFileHandler _sharedFileHandler = new SharedFileHandler();
    private final ApiJsonFactory _apiJsonFactory = new ApiJsonFactory();
    private Class<?> mClss;
    //AMAP
    private MapView _mapView = null;
    private AMap _aMap = null;
    private MyLocationStyle _myLocationStyle;
    //Custom Layouts
    private LinearLayout _homeView;
    private BottomNavigationView _navigation;
    private TextView _userNameText;
    private TextView _userPhoneText;
    private TextView _totalCO2Text;
    private TextView _totalBottlesText;
    private TextView _totalPointsText;
    private TextView _totalRewards;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    _homeView.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_qrcode:
                    _homeView.setVisibility(View.INVISIBLE);
                    launchActivity(BaseScannerActivity.class);
                    return true;
                case R.id.navigation_settings:
                    _homeView.setVisibility(View.INVISIBLE);
                    launchActivity(SettingsMenuActivity.class);
                    return true;
            }
            return false;
        }

    };

    public void launchActivity(Class<?> className) {
        mClss = className;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, ZXING_CAMERA_PERMISSION);
            this.launchActivity(className);
        } else {
            Intent intent = new Intent(this, className);
            if (mClss.equals(BaseScannerActivity.class)) {
                startActivityForResult(intent, 999);
            } else if (mClss.equals(SettingsMenuActivity.class)) {
                startActivityForResult(intent, 998);
            } else if (mClss.equals(LoginActivity.class)) {
                startActivityForResult(intent, 997);
            }

        }
    }

    // Call Back method  to get the Message form other Activity
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
            mOnNavigationItemSelectedListener.onNavigationItemSelected(_navigation.getMenu().getItem(0));
        } else if (requestCode == 998) {
            mOnNavigationItemSelectedListener.onNavigationItemSelected(_navigation.getMenu().getItem(0));
        } else if (requestCode == 997) {
            getUserData(_sharedFileHandler.retreiveUserSession(HomeActivity.this), _sharedFileHandler.retreiveUserID(HomeActivity.this));
            mOnNavigationItemSelectedListener.onNavigationItemSelected(_navigation.getMenu().getItem(0));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        if (checkLocationPermission()) {
//            if (ContextCompat.checkSelfPermission(this,
//                    Manifest.permission.ACCESS_FINE_LOCATION)
//                    == PackageManager.PERMISSION_GRANTED) {
//
//            }
//        }

        processViews(savedInstanceState);

        checkLoginStatus();
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(HomeActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }

    private void checkLoginStatus() {
        if (_sharedFileHandler.retreiveUserSession(HomeActivity.this) == null) {
            Toast.makeText(HomeActivity.this, "尚未登入", Toast.LENGTH_LONG).show();
            launchActivity(LoginActivity.class);
        } else {
            getUserData(_sharedFileHandler.retreiveUserSession(HomeActivity.this), _sharedFileHandler.retreiveUserID(HomeActivity.this));
        }
    }

    private void processViews(Bundle savedInstanceState) {
        _homeView = (LinearLayout) findViewById(R.id.home_frame);

        _navigation = (BottomNavigationView) findViewById(R.id.navigation);
        _navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        _mapView = (MapView) findViewById(R.id.map);
        _mapView.onCreate(savedInstanceState);// 此方法必须重写

        _userNameText = (TextView) findViewById(R.id.user_name);
        _userPhoneText = (TextView) findViewById(R.id.user_phone);
        _totalCO2Text = (TextView) findViewById(R.id.total_co2);
        _totalBottlesText = (TextView) findViewById(R.id.total_bottles);
        _totalPointsText = (TextView) findViewById(R.id.total_points);
        _totalRewards = (TextView) findViewById(R.id.total_rewards);

        mapInit();
    }

    private void mapInit() {
        if (_aMap == null) {
            _aMap = _mapView.getMap();
            setUpMap();
        }
        _aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        //设置SDK 自带定位消息监听
        _aMap.setOnMyLocationChangeListener(this);
    }

    private void getUserData(String session, String userID) {

        String json = _apiJsonFactory.getUserInfoJson(session, userID);

        // Call Connection Tool to process login
        HttpConnectionTool httpConnectionTool = new HttpConnectionTool();
        httpConnectionTool.postMethod(new URLFactory().getUerInfoURL(), json, new HttpConnectionToolCallback() {
            @Override
            public void onSuccess(String result) {
                final UserLoginResponse userLoginResponse = _apiJsonFactory.parseUserLoginResponse(result);
                System.out.println(result);
                if (userLoginResponse.getCode() == 1) {

                    //Store session to SharedPreferences
                    SharedFileHandler sharedFileHandler = new SharedFileHandler();
                    sharedFileHandler.saveUserSession(HomeActivity.this, userLoginResponse);

                    // Update UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            _progress.setVisibility(View.INVISIBLE);
                            _userNameText.setText(userLoginResponse.getUser().getUserName());
                            _userPhoneText.setText(userLoginResponse.getUser().getPhoneNumber());
                            _totalCO2Text.setText("" + userLoginResponse.getUser().getTotalCoals());
                            _totalBottlesText.setText("" + userLoginResponse.getUser().getTotalNums());
                            _totalPointsText.setText("" + userLoginResponse.getUser().getSumPoint());
                            _totalRewards.setText("" + userLoginResponse.getUser().getWallet());

                            Toast.makeText(HomeActivity.this, "已抓到登入", Toast.LENGTH_LONG).show();

                        }
                    });
                } else {
                    launchActivity(LoginActivity.class);
                }
            }
        });
    }


    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        // 如果要设置定位的默认状态，可以在此处进行设置
        _myLocationStyle = new MyLocationStyle();
        _aMap.setMyLocationStyle(_myLocationStyle);

        _aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        _aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        _mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        _mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        _mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        _mapView.onDestroy();
    }

    @Override
    public void onMyLocationChange(Location location) {
        // 定位回调监听
        if (location != null) {
//            Log.e("amap", "onMyLocationChange 定位成功， lat: " + location.getLatitude() + " lon: " + location.getLongitude());
            Bundle bundle = location.getExtras();
            if (bundle != null) {
                int errorCode = bundle.getInt(MyLocationStyle.ERROR_CODE);
                String errorInfo = bundle.getString(MyLocationStyle.ERROR_INFO);
                // 定位类型，可能为GPS WIFI等，具体可以参考官网的定位SDK介绍
                int locationType = bundle.getInt(MyLocationStyle.LOCATION_TYPE);
                /*
                errorCode
                errorInfo
                locationType
                */
//                Log.e("amap", "定位信息， code: " + errorCode + " errorInfo: " + errorInfo + " locationType: " + locationType);
            } else {
//                Log.e("amap", "定位信息， bundle is null ");
            }

        } else {
//            Log.e("amap", "定位失败");
        }
    }

}
