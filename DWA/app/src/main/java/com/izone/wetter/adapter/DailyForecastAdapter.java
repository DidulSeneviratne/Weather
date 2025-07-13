package com.izone.wetter.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.izone.wetter.R;
import com.izone.wetter.model.ForecastResponse;

import java.util.List;

public class DailyForecastAdapter extends RecyclerView.Adapter<DailyForecastAdapter.ViewHolder> {

    private List<ForecastResponse.ForecastItem> forecastList;

    public DailyForecastAdapter(List<ForecastResponse.ForecastItem> forecastList) {
        this.forecastList = forecastList;
    }

    @NonNull
    @Override
    public DailyForecastAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_daily_forecast, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyForecastAdapter.ViewHolder holder, int position) {
        ForecastResponse.ForecastItem item = forecastList.get(position);
        holder.textDate.setText(item.dt_txt.substring(0, 10));
        holder.textDayTemp.setText(Math.round(item.main.temp) + "°");
        holder.textDayCondition.setText(item.weather.get(0).main);
    }

    @Override
    public int getItemCount() {
        return forecastList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textDate, textDayTemp, textDayCondition;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textDate = itemView.findViewById(R.id.textDate);
            textDayTemp = itemView.findViewById(R.id.textDayTemp);
            textDayCondition = itemView.findViewById(R.id.textDayCondition);
        }
    }
}

