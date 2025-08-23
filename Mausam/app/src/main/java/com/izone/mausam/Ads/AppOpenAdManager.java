package com.izone.mausam.Ads;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.izone.mausam.R;

import java.util.Date;

public class AppOpenAdManager {
    private AppOpenAd appOpenAd;
    private long loadTime = 0;
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
                        loadTime = (new Date()).getTime();
                    }
                });
    }

    public void showAdIfAvailable(Activity activity) {
        if (isAdAvailable()) {
            appOpenAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    appOpenAd = null;
                    loadAd();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    appOpenAd = null;
                    loadAd();
                }
            });

            appOpenAd.show(activity);
        } else {
            loadAd();
        }
    }

    private boolean isAdAvailable() {
        return appOpenAd != null && (new Date()).getTime() - loadTime < 4 * 3600 * 1000;
    }
}
