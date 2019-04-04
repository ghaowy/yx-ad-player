package com.imprexion.aiscreen.status;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.imprexion.aiscreen.R;
import com.imprexion.aiscreen.bean.EventBusMessage;
import com.imprexion.aiscreen.bean.WeatherInfo;
import com.imprexion.aiscreen.component.BaiDuLocation;
import com.imprexion.aiscreen.net.NetContract;
import com.imprexion.aiscreen.net.NetPresenter;
import com.imprexion.aiscreen.tools.IconFontTextView;
import com.imprexion.aiscreen.tools.Tools;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class StatusFragment extends Fragment implements NetContract.StatusView {

    @BindView(R.id.tv_current_time)
    TextView tvCurrentTime;
    @BindView(R.id.tv_weather)
    TextView tvWeather;
    @BindView(R.id.tv_weather_icon)
    IconFontTextView tvWeatherIcon;
    @BindView(R.id.tv_destination_text)
    TextView tvDestinationText;
    private View mView;
    private Unbinder mUnbinder;
    public final static int UPDATE_WEATHER = 1;
    private final static String TAG = "StatusFragment";
    private String tempretures[];
    private String weatherIcon[];
    private NetPresenter mNetPresenter;

    private Thread mTimeThread;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    if (tvCurrentTime != null) {
                        Date date = new Date();
                        tvCurrentTime.setText(simpleDateFormat.format(date));
                        if (date.getMinutes() == 0 && date.getSeconds() == 0) {
                            EventBus.getDefault().post(new EventBusMessage(UPDATE_WEATHER, null));
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };
    private BaiDuLocation mBdLocation;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_status, container, false);
        mUnbinder = ButterKnife.bind(this, mView);
        tempretures = getResources().getStringArray(R.array.tempretures);
        weatherIcon = getContext().getResources().getStringArray(R.array.weather_icon);
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setcurrentTime();
        setWeatherInfo();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
//        if (mTimeThread.isAlive()) {
//            mTimeThread.destroy();
//        }
    }

    private void setcurrentTime() {
        mTimeThread = new Thread() {
            @Override
            public void run() {
                do {
                    try {
                        Thread.sleep(1000);
                        Message message = Message.obtain();
                        message.what = 1;
                        mHandler.sendMessage(message);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } while (true);
            }
        };
        mTimeThread.start();
    }

    private void setWeatherInfo() {
        if (mNetPresenter == null) {
            mNetPresenter = new NetPresenter(this);
        }

        requestWeather();

    }

    private void requestWeather() {
        mBdLocation = new BaiDuLocation();
        mBdLocation.setLocationUpdateListener(new BaiDuLocation.LocationUpdateListener() {
            @Override
            public void updateLoaction(String city, String address) {
                Log.d(TAG, "address =" + address);
                if (Tools.isNetworkConnected(getContext())) {
                    mNetPresenter.updateWeather(city);
                }
                if (!address.equals("nullnull")) {
                    tvDestinationText.setText(address);
                }
            }
        });
        mBdLocation.initLocationOption();
    }

    @Override
    public void updateWeather(WeatherInfo weatherInfo) {
        WeatherInfo.HeWeather6 heWeather6 = weatherInfo.getHeWeather6().get(0);
        Log.d(TAG, "heWeather6=" + new Gson().toJson(heWeather6));
        String status = heWeather6.getStatus();
        if (!status.equals("ok")) {
            return;
        }
        String weather = heWeather6.getNow().getCond_txt();
        tvWeather.setText(weather);

        if (weather.contains("晴")) {
            tvWeatherIcon.setText(weatherIcon[0]);
        } else if (weather.contains("雨")) {
            tvWeatherIcon.setText(weatherIcon[1]);
        } else if (weather.contains("阴")) {
            tvWeatherIcon.setText(weatherIcon[2]);
        } else if (weather.contains("雪")) {
            tvWeatherIcon.setText(weatherIcon[3]);
        } else if (weather.contains("多云")) {
            tvWeatherIcon.setText(weatherIcon[4]);
        }
    }
}
