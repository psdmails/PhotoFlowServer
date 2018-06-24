package com.psdutta.hmi.photogalaxy;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.psdutta.hmi.photogalaxy.connection.PhotoGalaxyManager;

public class GalaxyServerService extends Service {
    public GalaxyServerService() {
        PhotoGalaxyManager.getInstance();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }
}
