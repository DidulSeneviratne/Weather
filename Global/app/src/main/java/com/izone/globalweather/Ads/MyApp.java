package com.izone.globalweather.Ads;

import android.app.Application;
import com.google.android.gms.ads.MobileAds;

public class MyApp extends Application {

    public AppOpenAdManager appOpenAdManager;

    @Override
    public void onCreate() {
        super.onCreate();
        MobileAds.initialize(this, initializationStatus -> {});
        appOpenAdManager = new AppOpenAdManager(this);
    }
}

