package com.psdutta.hmi.photogalaxy;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

public class ApplicationClass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        startService();
        initFreesco();
    }

    void startService() {
        Intent it = new Intent(this, GalaxyServerService.class);
        startService(it);
    }

    private void initFreesco() {
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setNetworkFetcher(new CustomNetworkFetcher())
                .build();
        Fresco.initialize(this, config);
        FLog.setMinimumLoggingLevel(FLog.VERBOSE);
    }
}
