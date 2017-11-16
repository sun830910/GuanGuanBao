package com.enjoygreenlife.guanguanbao.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.MyLocationStyle;
import com.enjoygreenlife.guanguanbao.R;


public class HomeActivity extends AppCompatActivity implements AMap.OnMyLocationChangeListener {

    private static final int ZXING_CAMERA_PERMISSION = 1;
    private Class<?> mClss;

    private MapView _mapView = null;
    private AMap _aMap = null;
    private MyLocationStyle _myLocationStyle;

    private LinearLayout _homeView;
    private BottomNavigationView _navigation;

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
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        processViews(savedInstanceState);
    }

    private void processViews(Bundle savedInstanceState) {
        _homeView = (LinearLayout) findViewById(R.id.home_frame);

        _navigation = (BottomNavigationView) findViewById(R.id.navigation);
        _navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        _mapView = (MapView) findViewById(R.id.map);
        _mapView.onCreate(savedInstanceState);// 此方法必须重写

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
