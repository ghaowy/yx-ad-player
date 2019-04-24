package com.imprexion.adplayer.service;

import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import com.imprexion.adplayer.tools.ALog;
import com.imprexion.service.tracking.bean.aiscreen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class TcpClientConnector {

    private static TcpClientConnector mTcpClientConnector;
    private Thread mConnectThread;
    private Socket mSocket;
    private ConnectListener mConnectListener;
    private final static int RECEIVE_DATA = 100;
    private final static String TAG = "TcpClientConnector";

    //10.2.26.181 20002

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case RECEIVE_DATA:
                    if (mConnectListener != null) {
                        mConnectListener.onReceiveData((aiscreen) msg.obj);
                    }
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    public static TcpClientConnector getInstance() {
        if (mTcpClientConnector == null) {
            mTcpClientConnector = new TcpClientConnector();
        }
        return mTcpClientConnector;
    }

    public void createConnect(final String ip, final int port) {
        ALog.d(TAG, "createConnect");
        if (mConnectThread == null) {
            ALog.d(TAG, "new mConnectThread");
            mConnectThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        connect(ip, port);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            mConnectThread.start();
        }
    }

    public void connect(String ip, int port) {

        if (mSocket == null) {
            while (true) {
                try {
                    mSocket = new Socket(ip, port);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        aiscreen mfas = aiscreen.parseFrom(
                                Base64.decode(line.getBytes(), Base64.DEFAULT));
                        Message message = mHandler.obtainMessage();
                        message.what = RECEIVE_DATA;
                        message.obj = mfas;
                        mHandler.sendMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }

                }
            }
        }
    }

    public interface ConnectListener {
        void onReceiveData(aiscreen messageForAIScreen);

    }

    public void setOnConnectListener(ConnectListener connectListener) {
        mConnectListener = connectListener;
    }

    public void disconnect() {
        ALog.d(TAG, "disconnect()");
        if (mSocket != null) {
            try {
                mSocket.close();
                mSocket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
