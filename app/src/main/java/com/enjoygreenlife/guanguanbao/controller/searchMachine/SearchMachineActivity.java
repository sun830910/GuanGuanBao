package com.enjoygreenlife.guanguanbao.controller.searchMachine;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

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
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;

import com.amap.api.services.weather.WeatherSearchQuery;
import com.enjoygreenlife.guanguanbao.R;
import com.enjoygreenlife.guanguanbao.model.ApiModel.ApiJsonFactory;
import com.enjoygreenlife.guanguanbao.model.ApiModel.SharedFileHandler;
import com.enjoygreenlife.guanguanbao.model.ApiModel.URLFactory;
import com.enjoygreenlife.guanguanbao.model.DataModel.RecycleMachine;
import com.enjoygreenlife.guanguanbao.model.DataModel.RecycleMachineResponse;
import com.enjoygreenlife.guanguanbao.tool.amap.AMapUtil;
import com.enjoygreenlife.guanguanbao.tool.httpConnectionTool.HttpConnectionTool;
import com.enjoygreenlife.guanguanbao.tool.httpConnectionTool.HttpConnectionToolCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


/**
 * AMapV2地图中介绍如何显示一个基本地图
 */
public class SearchMachineActivity extends Activity implements OnClickListener, AMap.OnMyLocationChangeListener,
		GeocodeSearch.OnGeocodeSearchListener {

	private final ApiJsonFactory _apiJsonFactory = new ApiJsonFactory();
	private SharedFileHandler _sharedFileHandler = new SharedFileHandler();
	private MapView mapView;
	private AMap aMap;
	private Button basicmap;
	private Button rsmap;
	private Button nightmap;
	private Button navimap;
	private final ThreadLocal<MyLocationStyle> _myLocationStyle = new ThreadLocal<>();
	private LatLonPoint latLonPoint = new LatLonPoint(39.90865, 116.39751);
	private GeocodeSearch geocoderSearch;
	private ArrayList<Marker> _markerList = new ArrayList<Marker>();
	private WeatherSearchQuery mquery;

	private CheckBox mStyleCheckbox;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_machine);
	    /*
         * 设置离线地图存储目录，在下载离线地图或初始化地图设置;
         * 使用过程中可自行设置, 若自行设置了离线地图存储的路径，
         * 则需要在离线地图下载和使用地图页面都进行路径设置
         * */
		//Demo中为了其他界面可以使用下载的离线地图，使用默认位置存储，屏蔽了自定义设置
		//  MapsInitializer.sdcardDir =OffLineMapUtils.getSdCacheDir(this);

		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		init();

	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			_myLocationStyle.set(new MyLocationStyle());
			_myLocationStyle.get().myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);//定位一次，且将视角移动到地图中心点。
			aMap.setMyLocationStyle(_myLocationStyle.get());

			aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
			aMap.setMyLocationEnabled(true);
		}
//		setMapCustomStyleFile(this);
		basicmap = (Button)findViewById(R.id.basicmap);
		basicmap.setOnClickListener(this);
		rsmap = (Button)findViewById(R.id.rsmap);
		rsmap.setOnClickListener(this);
		nightmap = (Button)findViewById(R.id.nightmap);
		nightmap.setOnClickListener(this);
		navimap = (Button)findViewById(R.id.navimap);
		navimap.setOnClickListener(this);

		mStyleCheckbox = (CheckBox) findViewById(R.id.check_style);

		mStyleCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				aMap.setMapCustomEnable(b);
			}
		});

	}

	private void setMapCustomStyleFile(Context context) {
		String styleName = "style_json.json";
		FileOutputStream outputStream = null;
		InputStream inputStream = null;
		String filePath = null;
		try {
			inputStream = context.getAssets().open(styleName);
			byte[] b = new byte[inputStream.available()];
			inputStream.read(b);

			filePath = context.getFilesDir().getAbsolutePath();
			File file = new File(filePath + "/" + styleName);
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			outputStream = new FileOutputStream(file);
			outputStream.write(b);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();

				if (outputStream != null)
					outputStream.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		aMap.setCustomMapStylePath(filePath + "/" + styleName);

		aMap.showMapText(false);

		mapInit();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.basicmap:
				aMap.setMapType(AMap.MAP_TYPE_NORMAL);// 矢量地图模式
				break;
			case R.id.rsmap:
				aMap.setMapType(AMap.MAP_TYPE_SATELLITE);// 卫星地图模式
				break;
			case R.id.nightmap:
				aMap.setMapType(AMap.MAP_TYPE_NIGHT);//夜景地图模式
				break;
			case R.id.navimap:
				aMap.setMapType(AMap.MAP_TYPE_NAVI);//导航地图模式
				break;
		}

		mStyleCheckbox.setChecked(false);

	}

	private void mapInit() {
		aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
		//设置SDK 自带定位消息监听
		aMap.setOnMyLocationChangeListener(this);


		geocoderSearch = new GeocodeSearch(this);
		geocoderSearch.setOnGeocodeSearchListener(this);
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

		getStation(_sharedFileHandler.retreiveUserSession(SearchMachineActivity.this), _sharedFileHandler.retreiveUserID(SearchMachineActivity.this), location);
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
					aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
							AMapUtil.convertToLatLng(latLonPoint), 15));
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
						final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title(machine.getName()).snippet(machine.getAddress()));
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
