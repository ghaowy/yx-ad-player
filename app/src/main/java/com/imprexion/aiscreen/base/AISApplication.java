package com.imprexion.aiscreen.base;

import android.app.Application;

import com.tencent.smtt.sdk.QbSdk;

public class AISApplication extends Application {


    private static AISApplication aisApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        aisApplication = this;

        //��wifi����£���������x5�ں�
        QbSdk.setDownloadWithoutWifi(true);
        //�Ѽ�����tbs�ں���Ϣ���ϱ������������������ؽ������ʹ���ĸ��ںˡ�
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5�Ⱥ˳�ʼ����ɵĻص���Ϊtrue��ʾx5�ں˼��سɹ��������ʾx5�ں˼���ʧ�ܣ����Զ��л���ϵͳ�ںˡ�
            }
            @Override
            public void onCoreInitFinished() {

            }
        };
        //x5�ں˳�ʼ���ӿ�
        QbSdk.initX5Environment(getApplicationContext(), cb);

    }

    public static AISApplication getInstance(){
        return aisApplication;
    }
}
