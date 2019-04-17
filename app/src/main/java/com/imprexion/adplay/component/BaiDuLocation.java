package com.imprexion.adplay.component;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.imprexion.adplay.base.ADPlayApplication;

public class BaiDuLocation {
    LocationClient locationClient;
    private double mLatitude;
    private double mLongitude;

    private final static String TAG = "BaiDuLocation";


    public void setLocationUpdateListener(LocationUpdateListener locationUpdateListener) {
        mLocationUpdateListener = locationUpdateListener;
    }

    private LocationUpdateListener mLocationUpdateListener;

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void initLocationOption() {
        locationClient = new LocationClient(ADPlayApplication.getInstance().getApplicationContext());
        LocationClientOption locationOption = new LocationClientOption();
        MyLocationListener myLocationListener = new MyLocationListener();
        //注册监听函数
        locationClient.registerLocationListener(myLocationListener);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        locationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        locationOption.setCoorType("bd09ll");
        locationOption.setIsNeedAddress(true);
        locationClient.setLocOption(locationOption);
        //开始定位
        locationClient.start();

    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            String city = location.getCity();
            String address = location.getCity() + location.getDistrict();
            mLocationUpdateListener.updateLoaction(city, address);
        }
    }

    public interface LocationUpdateListener {
        void updateLoaction(String city, String address);
    }
}
