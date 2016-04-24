package com.sponsor.android;

import android.app.Application;
import android.content.Context;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.ProgressiveJpegConfig;
import com.facebook.imagepipeline.image.ImmutableQualityInfo;
import com.facebook.imagepipeline.image.QualityInfo;
import com.orhanobut.logger.Logger;
import com.sponsor.android.manager.PhoneManager;
import com.sponsor.android.net.OkHttpManager;

import java.io.File;

public class SponsorApplication extends Application {
    private static Context mContext;
    private static SponsorApplication INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        mContext = getApplicationContext();

        /**
         * 初始化logger
         */
        Logger.init("SponsorFinal").hideThreadInfo();

        /**
         * 初始化fressco
         */
        initFressco();
    }

    private void initFressco(){
        /*******初始化Facebook Fresco********/
        DiskCacheConfig mainDiskCacheConfig = DiskCacheConfig.newBuilder()
                .setBaseDirectoryPath(new File(PhoneManager.getAppRootPath()))
                .setBaseDirectoryName("cache")
                .setMaxCacheSize(50 * 1024 * 1024)
                .setMaxCacheSizeOnLowDiskSpace(30 * 1024 * 1024)
                .setMaxCacheSizeOnVeryLowDiskSpace(15 * 1024 * 1024)
                .setVersion(1)
                .build();

        /*************渐进显示******************/
        ProgressiveJpegConfig pjpegConfig = new ProgressiveJpegConfig() {
            @Override
            public int getNextScanNumberToDecode(int scanNumber) {
                return scanNumber + 2;
            }

            public QualityInfo getQualityInfo(int scanNumber) {
                boolean isGoodEnough = (scanNumber >= 5);
                return ImmutableQualityInfo.of(scanNumber, isGoodEnough, false);
            }
        };

        ImagePipelineConfig config = OkHttpImagePipelineConfigFactory.newBuilder(INSTANCE, OkHttpManager.instance())
                .setProgressiveJpegConfig(pjpegConfig)
                .setMainDiskCacheConfig(mainDiskCacheConfig)
                .build();
        Fresco.initialize(INSTANCE, config);
    }

    public static synchronized SponsorApplication getInstance() {
        return INSTANCE;
    }

    public static synchronized Context getContext() {
        return mContext;
    }
}
