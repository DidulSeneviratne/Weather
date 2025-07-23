package com.izone.mausam.Ads;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.izone.mausam.R;

public class AppOpenAdManager {
    private AppOpenAd appOpenAd;
    private final Application application;

    public AppOpenAdManager(Application app) {
        this.application = app;
        loadAd();
    }

    private void loadAd() {
        AdRequest request = new AdRequest.Builder().build();
        AppOpenAd.load(application, application.getString(R.string.ads_open), request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                new AppOpenAd.AppOpenAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull AppOpenAd ad) {
                        appOpenAd = ad;
                    }
                });
    }

    public void showAdIfAvailable(Activity activity) {
        if (appOpenAd != null) {
            appOpenAd.show(activity);
        } else {
            loadAd(); // Load for next time
        }
    }
}
