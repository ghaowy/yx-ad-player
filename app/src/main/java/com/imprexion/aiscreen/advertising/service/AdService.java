package com.imprexion.aiscreen.advertising.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AdService extends Service {
    public AdService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
