package com.psdutta.hmi.photogalaxy;

import android.app.Application;
import android.content.Intent;

public class ApplicationClass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        startService();
    }

    void startService() {
        Intent it = new Intent(this, GalaxyServerService.class);
        startService(it);
    }
}
