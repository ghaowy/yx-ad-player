package com.imprexion.aiscreen.service;

import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;

import com.imprexion.aiscreen.bean.MessageForAIScreenPB;

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

    //10.2.26.181 23333

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case RECEIVE_DATA:
                    if (mConnectListener != null) {
                        mConnectListener.onReceiveData((MessageForAIScreenPB.MessageForAIScreen) msg.obj);
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
        Log.d(TAG, "createConnect");
        if (mConnectThread == null) {
            Log.d(TAG, "new mConnectThread");
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
                        System.out.println(line);
                        MessageForAIScreenPB.MessageForAIScreen mfas = MessageForAIScreenPB.MessageForAIScreen.parseFrom(
                                Base64.decode(line.getBytes(), Base64.DEFAULT));
                        Message message = mHandler.obtainMessage();
                        message.what = RECEIVE_DATA;
                        message.obj = mfas;
                        mHandler.sendMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }

                }
            }
        }
    }

    public interface ConnectListener {
        void onReceiveData(MessageForAIScreenPB.MessageForAIScreen messageForAIScreen);

    }

    public void setOnConnectListener(ConnectListener connectListener) {
        mConnectListener = connectListener;
    }

    public void disconnect() {
        Log.d(TAG, "disconnect()");
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
