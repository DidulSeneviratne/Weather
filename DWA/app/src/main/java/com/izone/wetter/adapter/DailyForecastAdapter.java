package com.izone.wetter.adapter;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.izone.wetter.R;
import com.izone.wetter.model.ForecastResponse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DailyForecastAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_FORECAST = 0;
    private static final int VIEW_TYPE_AD = 1;
    private static final int AD_POSITION = 3;

    private final List<ForecastResponse.ForecastItem> forecastList;
    private NativeAd nativeAd; // you can set this from outside
    private RecyclerView recyclerDaily;
    private boolean showAd = true;

    public void disableAd() {
        this.showAd = false;
        notifyDataSetChanged(); // Refresh list
    }

    public DailyForecastAdapter(List<ForecastResponse.ForecastItem> forecastList, RecyclerView recyclerDaily) {
        this.forecastList = forecastList;
        this.recyclerDaily = recyclerDaily;
    }

    public void setNativeAd(NativeAd ad) {
        this.nativeAd = ad;
        notifyItemChanged(AD_POSITION);
    }

    @Override
    public int getItemCount() {
        return forecastList.size() + (showAd && nativeAd != null ? 1 : 0); // one extra for ad
    }

    @Override
    public int getItemViewType(int position) {
        if (showAd && nativeAd != null && position == AD_POSITION) {
            return VIEW_TYPE_AD;
        }
        return VIEW_TYPE_FORECAST;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_AD) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_native_ad, parent, false);
            return new AdViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_daily_forecast, parent, false);
            return new ForecastViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_AD) {
            ((AdViewHolder) holder).bind(nativeAd, recyclerDaily);
        } else {
            int realPosition = position > AD_POSITION ? position - 1 : position;
            ForecastResponse.ForecastItem item = forecastList.get(realPosition);
            ((ForecastViewHolder) holder).bind(item);
        }
    }

    public static class ForecastViewHolder extends RecyclerView.ViewHolder {
        TextView textDate, textDayTemp, textDayCondition, textWind, textPressure, textPrecip;
        ImageView imageIcon;

        public ForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            textDate = itemView.findViewById(R.id.textDate);
            textDayTemp = itemView.findViewById(R.id.textDayTemp);
            textDayCondition = itemView.findViewById(R.id.textDayCondition);
            textWind = itemView.findViewById(R.id.textWind);
            textPressure = itemView.findViewById(R.id.textPressure);
            textPrecip = itemView.findViewById(R.id.textPrecip);
            imageIcon = itemView.findViewById(R.id.imageIcon);
        }

        public void bind(ForecastResponse.ForecastItem item) {
            textDate.setText(formatDate(item.dt_txt));
            textDayTemp.setText(Math.round(item.main.temp) + "°");
            textDayCondition.setText(item.weather.get(0).main);
            textWind.setText("Wind: " + item.wind.speed + " m/s");
            textPressure.setText("Pressure: " + item.main.pressure + " hPa");
            textPrecip.setText("Precipitation: " + Math.round(item.pop * 100) + "%");

            String iconUrl = "https://openweathermap.org/img/wn/" + item.weather.get(0).icon + "@2x.png";
            Glide.with(itemView.getContext())
                    .load(iconUrl)
                    .into(imageIcon);
        }

        private String formatDate(String dtTxt) {
            try {
                SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                SimpleDateFormat output = new SimpleDateFormat("EEE dd.MM.yyyy - HH:mm", Locale.getDefault());
                Date date = input.parse(dtTxt);
                return output.format(date);
            } catch (Exception e) {
                return dtTxt;
            }
        }
    }

    public static class AdViewHolder extends RecyclerView.ViewHolder {
        NativeAdView nativeAdView;


        public AdViewHolder(@NonNull View itemView) {
            super(itemView);
            nativeAdView = (NativeAdView) itemView;
        }

        public void bind(NativeAd ad, RecyclerView recyclerDaily) {
            if (ad == null) return;

            // Set up views based on your layout
            TextView headlineView = nativeAdView.findViewById(R.id.ad_headline);
            TextView bodyView = nativeAdView.findViewById(R.id.ad_body);
            MediaView mediaView = nativeAdView.findViewById(R.id.ad_media);

            headlineView.setText(ad.getHeadline());
            nativeAdView.setHeadlineView(headlineView);

            if (ad.getBody() != null) {
                bodyView.setText(ad.getBody());
                nativeAdView.setBodyView(bodyView);
                nativeAdView.setMediaView(mediaView);
                bodyView.setVisibility(View.VISIBLE);

            } else {
                bodyView.setVisibility(View.GONE);
            }

            nativeAdView.setNativeAd(ad);
        }
    }
}
