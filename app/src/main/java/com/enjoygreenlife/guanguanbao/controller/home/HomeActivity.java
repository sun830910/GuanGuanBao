package com.enjoygreenlife.guanguanbao.controller.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearch.OnWeatherSearchListener;
import com.amap.api.services.weather.WeatherSearchQuery;
import com.enjoygreenlife.guanguanbao.R;
import com.enjoygreenlife.guanguanbao.controller.login.LoginActivity;
import com.enjoygreenlife.guanguanbao.controller.scanner.BaseScannerActivity;
import com.enjoygreenlife.guanguanbao.controller.settings.SettingsMenuActivity;
import com.enjoygreenlife.guanguanbao.model.ApiModel.ApiJsonFactory;
import com.enjoygreenlife.guanguanbao.model.ApiModel.SharedFileHandler;
import com.enjoygreenlife.guanguanbao.model.ApiModel.URLFactory;
import com.enjoygreenlife.guanguanbao.model.ApiModel.WeatherResultConverter;
import com.enjoygreenlife.guanguanbao.model.DataModel.RecycleMachine;
import com.enjoygreenlife.guanguanbao.model.DataModel.RecycleMachineResponse;
import com.enjoygreenlife.guanguanbao.model.DataModel.UserLoginResponse;
import com.enjoygreenlife.guanguanbao.model.ViewModel.References.ActivityManager;
import com.enjoygreenlife.guanguanbao.tool.amap.AMapUtil;
import com.enjoygreenlife.guanguanbao.tool.httpConnectionTool.HttpConnectionTool;
import com.enjoygreenlife.guanguanbao.tool.httpConnectionTool.HttpConnectionToolCallback;

import java.util.ArrayList;
import java.util.Locale;


public class HomeActivity extends AppCompatActivity implements AMap.OnMyLocationChangeListener,
        OnGeocodeSearchListener, OnWeatherSearchListener {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final int ZXING_CAMERA_PERMISSION = 1;
    private final ApiJsonFactory _apiJsonFactory = new ApiJsonFactory();
    private SharedFileHandler _sharedFileHandler = new SharedFileHandler();
    private Class<?> mClss;
    //AMAP
    private MapView _mapView = null;
    private AMap _aMap = null;
    private MyLocationStyle _myLocationStyle;
    private LatLonPoint latLonPoint = new LatLonPoint(39.90865, 116.39751);
    private Marker geoMarker;
    private Marker regeoMarker;
    private GeocodeSearch geocoderSearch;
    private ArrayList<Marker> _markerList = new ArrayList<Marker>();
    private WeatherSearchQuery mquery;
    private WeatherSearch mweathersearch;
    private LocalWeatherLive weatherlive;
    private String cityName = "北京市";
    private String cityAdCode = "212000";
    //Custom Layouts
    private LinearLayout _homeView;
    private SwipeRefreshLayout _mSwipeRefreshLayout;
    private BottomNavigationView _navigation;
    private TextView _userNameText;
    private TextView _userPhoneText;
    private TextView _totalCO2Text;
    private TextView _totalBottlesText;
    private TextView _totalPointsText;
    private TextView _totalRewards;
    private TextView _locationTextView;
    private TextView _tempreatureTextView;
    private ImageView _weatherInfoImage;
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

    /**
     * method for luanchActivity
     */
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
                System.out.println("+++++++" + ActivityManager.BASESCANNER_ACTIVITY.getValue());
                startActivityForResult(intent, ActivityManager.BASESCANNER_ACTIVITY.getValue());
            } else if (mClss.equals(SettingsMenuActivity.class)) {
                startActivityForResult(intent, ActivityManager.SETTINGS_MENU_ACTIVITY.getValue());
            } else if (mClss.equals(LoginActivity.class)) {
                System.out.println("+++++++" + ActivityManager.LOGIN_ACTIVITY.getValue());
                startActivityForResult(intent, ActivityManager.LOGIN_ACTIVITY.getValue());
            }

        }
    }

    /**
     * Call Back method  to get the Message form other Activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == ActivityManager.BASESCANNER_ACTIVITY.getValue()) {
            getUserData(_sharedFileHandler.retreiveUserSession(HomeActivity.this), _sharedFileHandler.retreiveUserID(HomeActivity.this));
//            if (data.getBooleanExtra("SUCCESS", false)) {
//                getUserData(_sharedFileHandler.retreiveUserSession(HomeActivity.this), _sharedFileHandler.retreiveUserID(HomeActivity.this));
//            } else {
//                System.out.println("CLOSE");
//            }
            mOnNavigationItemSelectedListener.onNavigationItemSelected(_navigation.getMenu().getItem(0));
        } else if (requestCode == ActivityManager.SETTINGS_MENU_ACTIVITY.getValue()) {
            mOnNavigationItemSelectedListener.onNavigationItemSelected(_navigation.getMenu().getItem(0));
            if (data.getBooleanExtra("LOGOUT", false)) {
                launchActivity(LoginActivity.class);
            }
        } else if (requestCode == ActivityManager.LOGIN_ACTIVITY.getValue()) {
            getUserData(_sharedFileHandler.retreiveUserSession(HomeActivity.this), _sharedFileHandler.retreiveUserID(HomeActivity.this));
            mOnNavigationItemSelectedListener.onNavigationItemSelected(_navigation.getMenu().getItem(0));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        processViews(savedInstanceState);
        checkLoginStatus();
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


    private void processViews(Bundle savedInstanceState) {
        _homeView = (LinearLayout) findViewById(R.id.home_frame);
        _mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.home_swipe_refresh);
        _mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUserData(_sharedFileHandler.retreiveUserSession(HomeActivity.this), _sharedFileHandler.retreiveUserID(HomeActivity.this));
            }
        });

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
        _locationTextView = (TextView) findViewById(R.id.locationTextView);
        _tempreatureTextView = (TextView) findViewById(R.id.info_temperatureText);

        _weatherInfoImage = (ImageView) findViewById(R.id.icon_weather);
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


        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        // 如果要设置定位的默认状态，可以在此处进行设置
        _myLocationStyle = new MyLocationStyle();
        _myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);//定位一次，且将视角移动到地图中心点。
        _aMap.setMyLocationStyle(_myLocationStyle);

        _aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        _aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
    }

    /**
     * Function implement On Location Change
     */
    @Override
    public void onMyLocationChange(Location location) {

        //Get User Address by Location from AMAP
        latLonPoint = new LatLonPoint(location.getLatitude(), location.getLongitude());
        // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);
        // 设置异步逆地理编码请求
        geocoderSearch.getFromLocationAsyn(query);

        getStation(_sharedFileHandler.retreiveUserSession(HomeActivity.this), _sharedFileHandler.retreiveUserID(HomeActivity.this), location);
        /*
        // 定位回调监听
        if (location != null) {
//            Log.e("amap", "onMyLocationChange 定位成功， lat: " + location.getLatitude() + " lon: " + location.getLongitude());
            Bundle bundle = location.getExtras();
            if (bundle != null) {
                int errorCode = bundle.getInt(MyLocationStyle.ERROR_CODE);
                String errorInfo = bundle.getString(MyLocationStyle.ERROR_INFO);
                // 定位类型，可能为GPS WIFI等，具体可以参考官网的定位SDK介绍
                int locationType = bundle.getInt(MyLocationStyle.LOCATION_TYPE);

                Log.e("amap", "定位信息， code: " + errorCode + " errorInfo: " + errorInfo + " locationType: " + locationType);
            } else {
                Log.e("amap", "定位信息， bundle is null ");
            }

        } else {
            Log.e("amap", "定位失败");
        }*/
    }

    /**
     * Get Address from Latitude & Longitude with AMAP API
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (rCode == AMapException.CODE_AMAP_SUCCESS) {
                if (result != null && result.getRegeocodeAddress() != null
                        && result.getRegeocodeAddress().getFormatAddress() != null) {
                    String addressName = result.getRegeocodeAddress().getFormatAddress() + "附近";
                    cityName = result.getRegeocodeAddress().getCity().substring(0, 2);
                    cityAdCode = result.getRegeocodeAddress().getAdCode();
                    _aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            AMapUtil.convertToLatLng(latLonPoint), 15));
                    System.out.println("LOCATION+++++++" + addressName);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            _locationTextView.setText(cityName);
                            searchliveweather();
                        }
                    });

                } else {
                    System.out.println("LOCATION-----" + R.string.no_geo_result);
                }
            } else {
                System.out.println("LOCATION-----" + rCode);
            }
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    /**
     * 实时天气查询
     */
    private void searchliveweather() {
        mquery = new WeatherSearchQuery(cityAdCode, WeatherSearchQuery.WEATHER_TYPE_LIVE);//检索参数为城市和天气类型，实时天气为1、天气预报为2
        mweathersearch = new WeatherSearch(this);
        mweathersearch.setOnWeatherSearchListener(this);
        mweathersearch.setQuery(mquery);
        mweathersearch.searchWeatherAsyn(); //异步搜索
    }

    /**
     * 实时天气查询回调
     */
    @Override
    public void onWeatherLiveSearched(LocalWeatherLiveResult weatherLiveResult, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (weatherLiveResult != null && weatherLiveResult.getLiveResult() != null) {
                weatherlive = weatherLiveResult.getLiveResult();
                String temperature = weatherlive.getTemperature() + "°C";
                _tempreatureTextView.setText(temperature);
                WeatherResultConverter converter = new WeatherResultConverter();
                converter.SetWeatherIcon(_weatherInfoImage, weatherlive.getWeather());
                System.out.println("WEATHER++++" + weatherlive.getWeather() + "+++" + weatherlive.getTemperature() + "°C");
            } else {
                System.out.println("WEATHER--ERR--" + R.string.no_geo_result);
            }
        } else {
            System.out.println("WEATHER--ERR--" + rCode);
        }
    }

    /**
     * 天气预报查询结果回调
     */
    @Override
    public void onWeatherForecastSearched(LocalWeatherForecastResult weatherForecastResult, int rCode) {

    }

    /**
     * Call it for check Login Status
     */
    private void checkLoginStatus() {
        _sharedFileHandler = new SharedFileHandler();
        if (_sharedFileHandler.retreiveUserSession(HomeActivity.this) == null) {
            Toast.makeText(HomeActivity.this, "尚未登入", Toast.LENGTH_LONG).show();
            launchActivity(LoginActivity.class);
        } else {
            System.out.println("SESSION: " + _sharedFileHandler.retreiveUserSession(HomeActivity.this));
            getUserData(_sharedFileHandler.retreiveUserSession(HomeActivity.this), _sharedFileHandler.retreiveUserID(HomeActivity.this));
        }
    }

    /**
     * Call it for Getting User Data
     */
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

                    // Update UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                          _progress.setVisibility(View.INVISIBLE);
                            _userNameText.setText(userLoginResponse.getUser().getUserName());
                            _userPhoneText.setText(userLoginResponse.getUser().getPhoneNumber());

                            String coalStr = String.format(Locale.getDefault(), "%.0f", userLoginResponse.getUser().getTotalCoals()) + getResources().getString(R.string.unit_co2_gram);
                            SpannableString coalSpanString = new SpannableString(coalStr);
                            coalSpanString.setSpan(new RelativeSizeSpan(.4f), coalStr.length() - 1, coalStr.length(), 0); // set size
                            _totalCO2Text.setText(coalSpanString);

                            String totalNumStr = String.valueOf(userLoginResponse.getUser().getTotalNums()) + getResources().getString(R.string.unit_bottle);
                            SpannableString totalNumSpanStr = new SpannableString(totalNumStr);
                            totalNumSpanStr.setSpan(new RelativeSizeSpan(.5f), totalNumSpanStr.length() - 1, totalNumSpanStr.length(), 0); // set size
                            _totalBottlesText.setText(totalNumSpanStr);

                            String totalRewardStr = String.format(Locale.getDefault(), "%.0f", userLoginResponse.getUser().getSumPoint()) + getResources().getString(R.string.unit_money);
                            SpannableString totalRewardSpanStr = new SpannableString(totalRewardStr);
                            totalRewardSpanStr.setSpan(new RelativeSizeSpan(.5f), totalRewardSpanStr.length() - 1, totalRewardSpanStr.length(), 0); // set size
                            _totalRewards.setText(totalRewardSpanStr);

                            String totalPointsStr = String.format(Locale.getDefault(), "%.0f", userLoginResponse.getUser().getWallet()) + getResources().getString(R.string.unit_point);
                            SpannableString totalPointsSpanStr = new SpannableString(totalPointsStr);
                            totalPointsSpanStr.setSpan(new RelativeSizeSpan(.5f), totalPointsSpanStr.length() - 1, totalPointsSpanStr.length(), 0); // set size
                            _totalPointsText.setText(totalPointsSpanStr);

                            _mSwipeRefreshLayout.setRefreshing(false);
                            Toast.makeText(HomeActivity.this, "資料已更新", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    launchActivity(LoginActivity.class);
                }
            }
        });
    }

    /**
     * Call it for Getting Stations Around
     */
    private void getStation(String session, String userID, Location location) {
        cleanMarkerOnMap();
        String json = _apiJsonFactory.getStationJson(session, userID, location);
        System.out.println(json);
        // Call Connection Tool to process login
        HttpConnectionTool httpConnectionTool = new HttpConnectionTool();
        httpConnectionTool.postMethod(new URLFactory().getStationURL(), json, new HttpConnectionToolCallback() {
            @Override
            public void onSuccess(String result) {
                final RecycleMachineResponse recycleMachineResponse = _apiJsonFactory.parseRecycleMachineResponse(result);
                System.out.println(result);
                if (recycleMachineResponse.getCode() == 1) {
                    for (RecycleMachine machine : recycleMachineResponse.getData()) {
                        LatLng latLng = new LatLng(machine.getLatitude(), machine.getLongitude());
                        final Marker marker = _aMap.addMarker(new MarkerOptions().position(latLng).title(machine.getName()).snippet(machine.getAddress()));
                        System.out.println(machine.getId() + "--" + machine.getName() + "--" + machine.getAddress());
                        _markerList.add(marker);
                    }
                }
            }
        });
    }

    private void cleanMarkerOnMap() {
        for (Marker marker : _markerList) {
            marker.destroy();
        }
        _markerList.clear();
    }
}
