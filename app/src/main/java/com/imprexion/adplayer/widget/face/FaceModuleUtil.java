package com.imprexion.adplayer.widget.face;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.imprexion.adplayer.widget.face.bean.SignInfoBean;
import com.imprexion.adplayer.widget.face.event.FaceIdDetectUserEvent;
import com.imprexion.adplayer.widget.face.event.FaceSignInEvent;
import com.imprexion.adplayer.widget.face.event.FaceSignOutEvent;
import com.imprexion.adplayer.widget.face.event.QuerySignEvent;
import com.imprexion.adplayer.widget.face.event.RetryBindEvent;
import com.imprexion.library.YxLog;
import com.imprexion.library.util.AppUtils;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;

/**
 * @author : yan
 * @date : 2019/10/31 14:06
 * @desc : TODO 人脸插件工具类
 */
public class FaceModuleUtil {
    private static final String TAG = "FaceModuleUtil";
    private static final int MSG_FACEID_Bind = 0x100;  // 256，绑定,绑定之后才能进行通信
    private static final int MSG_FACEID_Unbind = 0x101;  // 257，解绑，解绑之后不会再发送相关消息
    private static final int MSG_FACEID_QUERY = 0x102;  // 258，查询用户accountToken信息
    private static final int MSG_FACEID_SIGNIN = 0x103;  //259， 登录
    private static final int MSG_FACEID_SIGNOUT = 0x104;  // 260，登出
    private static final int MSG_FACEID_CLOSE = 0x105;  // 261，关闭
    private static final int MSG_FACEID_SGININTHENCLOSE = 0x106;  // 262，登录成功2秒后 关闭
    private static final int MSG_FACEID_LAUNCHED = 0x107;  // 263，启动应用成功

    private static final int MSG_FACEID_BACK_DETECT_USER_SUCCESS = 0x10A;  // 266，后台监控摄像头检测到用户

    private static final String KEY_PACKAGE_NAME = "packageName";
    private static final String KEY_RESTART = "restart";
    private static FaceModuleUtil sFaceModuleUtil;
    private Messenger mService;
    private ServiceConnection mConn;
    private Messenger mMessenger;
    private Gson mGson = new Gson();
    private SignInfoBean mInfoBean;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (WidgetConstants.ACTION_SIGN.equals(intent.getAction())) {
                String messageType = intent.getStringExtra("messageType");
                if (messageType.equals("launchFinished")) {
                    dealRetry();
                }
            }
        }
    };

    private FaceModuleUtil() {
        initConnection();
        initMessenger();
    }

    public static FaceModuleUtil getInstance() {
        if (sFaceModuleUtil == null) {
            synchronized (FaceModuleUtil.class) {
                if (sFaceModuleUtil == null) {
                    sFaceModuleUtil = new FaceModuleUtil();
                }
            }
        }
        return sFaceModuleUtil;
    }

    private void initMessenger() {
        mMessenger = new Messenger(new FaceHandler(this));
    }

    public void launchFaceRecognizeActivity(Context context) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.imprexion.service.facerecognition");
        if (intent == null) {
            YxLog.e(TAG, "error , launch launchFaceRecognizeActivity failed , intent is null");
            return;
        }
        intent.putExtra(KEY_PACKAGE_NAME, context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //重新打开刷脸插件
        intent.putExtra(KEY_RESTART, "restart face recognition");
        context.startActivity(intent);
    }

    private void initConnection() {
        mConn = new ServiceConnection() { // 服务端连接通道
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mService = new Messenger(service);
                // 连接成功后发送绑定消息给服务端
                sendBindFaceIDSuccessMessage();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mService = null;
                dealRetry();
            }
        };
    }

    public void queryFaceIDMessage() {
        if (mService == null) {
            return;
        }
        try {
            Message msgFromClient = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("package", AppUtils.getPackageName());
            bundle.putInt("requestCode", 1000);
            msgFromClient.setData(bundle);
            msgFromClient.what = MSG_FACEID_QUERY;
            msgFromClient.replyTo = mMessenger;
            mService.send(msgFromClient);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void sendBindFaceIDSuccessMessage() {
        if (mService == null) {
            return;
        }
        Message msgFromClient = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("package", AppUtils.getPackageName());
        bundle.putInt("requestCode", 1000);
        msgFromClient.setData(bundle);
        msgFromClient.what = MSG_FACEID_Bind;
        msgFromClient.replyTo = mMessenger;
        try {
            mService.send(msgFromClient);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public void registerAndBindServices(Context context) {
        context.registerReceiver(mBroadcastReceiver, new IntentFilter(WidgetConstants.ACTION_SIGN));
        bindServices(context);
    }

    public void bindServices(Context context) {
        Intent intent = new Intent();
        intent.setClassName("com.imprexion.service.facerecognition", "com.imprexion.service.facerecognition.FaceRecService");
        intent.setAction("com.imprexion.service.facerecognition");
        String packageName = AppUtils.getPackageName();
        intent.putExtra("package", packageName);
        intent.setType(packageName);
        context.bindService(intent, mConn, Context.BIND_AUTO_CREATE);
    }

    public void unRegister(Context context) {
        // 取消注册 服务及接受者
        YxLog.i(TAG, "unRegister");
        try {
            sendUnBindFaceIDMessage();
            context.unregisterReceiver(mBroadcastReceiver);
            context.unbindService(mConn);
            sFaceModuleUtil = null;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void sendUnBindFaceIDMessage() {
        Message msgFromClient = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("package", AppUtils.getPackageName());
        bundle.putInt("requestCode", 1001);
        msgFromClient.setData(bundle);
        msgFromClient.what = MSG_FACEID_Unbind;
        msgFromClient.replyTo = mMessenger;
        try {
            mService.send(msgFromClient);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    static class FaceHandler extends Handler {

        private WeakReference<FaceModuleUtil> mWfModule;

        public FaceHandler(FaceModuleUtil faceModuleUtil) {
            mWfModule = new WeakReference<>(faceModuleUtil);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mWfModule == null || mWfModule.get() == null) {
                return;
            }
            YxLog.d(TAG, "handleMessage , msg.what = " + msg.what);

            switch (msg.what) {
                case MSG_FACEID_QUERY:
                    mWfModule.get().dealQuery(msg.getData().getString("data"));
                    break;
                case MSG_FACEID_SIGNIN:
                    mWfModule.get().dealSignIn(msg.getData().getString("data"));
                    break;
                case MSG_FACEID_SIGNOUT:
                    mWfModule.get().dealSignOut();
                    break;
                case MSG_FACEID_CLOSE:
                    break;
                case MSG_FACEID_SGININTHENCLOSE:
                    mWfModule.get().dealSignThenClose();
                    break;
                case MSG_FACEID_LAUNCHED:
                    mWfModule.get().dealRetry();
                    break;
                case MSG_FACEID_Bind:
                    break;
                case MSG_FACEID_Unbind:
                    break;
                case MSG_FACEID_BACK_DETECT_USER_SUCCESS:
                    mWfModule.get().sendMessageFaceIdDetectUser();
                    break;
                default:
                    break;
            }
        }
    }

    private void dealQuery(String jsonData) {
        if (TextUtils.isEmpty(jsonData) || mGson == null) {
            EventBus.getDefault().post(new QuerySignEvent(null));
            return;
        }
        mInfoBean = mGson.fromJson(jsonData, SignInfoBean.class);
        EventBus.getDefault().post(new QuerySignEvent(mInfoBean));
    }

    private void dealRetry() {
        EventBus.getDefault().post(new RetryBindEvent());
    }

    private void dealSignIn(String jsonData) {
        if (TextUtils.isEmpty(jsonData) || mGson == null) {
            return;
        }
        mInfoBean = mGson.fromJson(jsonData, SignInfoBean.class);
    }

    private void dealSignOut() {
        EventBus.getDefault().post(new FaceSignOutEvent());
    }

    private void dealSignThenClose() {
        EventBus.getDefault().post(new FaceSignInEvent(mInfoBean));
    }


    private void sendMessageFaceIdDetectUser() {
        EventBus.getDefault().post(new FaceIdDetectUserEvent());
    }


}
