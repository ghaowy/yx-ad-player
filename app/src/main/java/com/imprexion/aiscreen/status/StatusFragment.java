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

import com.imprexion.aiscreen.R;
import com.imprexion.aiscreen.bean.EventBusMessage;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class StatusFragment extends Fragment {

    @BindView(R.id.tv_current_time)
    TextView tvCurrentTime;
    private View mView;
    private Unbinder mUnbinder;
    public final static int UPDATE_WEATHER = 1;
    private final static String TAG = "StatusFragment";

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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_status, container, false);
        mUnbinder = ButterKnife.bind(this, mView);
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setcurrentTime();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        if (mTimeThread.isAlive()) {
            mTimeThread.destroy();
        }
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
}
