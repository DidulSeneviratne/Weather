package com.izone.mausam.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.izone.mausam.R;
import com.izone.mausam.model.ForecastResponse;

import java.util.List;

public class HourlyForecastAdapter extends RecyclerView.Adapter<HourlyForecastAdapter.ViewHolder> {

    private List<ForecastResponse.ForecastItem> forecastList;

    public HourlyForecastAdapter(List<ForecastResponse.ForecastItem> forecastList) {
        this.forecastList = forecastList;
    }

    @NonNull
    @Override
    public HourlyForecastAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hourly_forecast, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyForecastAdapter.ViewHolder holder, int position) {
        ForecastResponse.ForecastItem item = forecastList.get(position);

        String time = item.dt_txt.substring(11, 16);
        holder.textTime.setText(time);
        holder.textTemp.setText(Math.round(item.main.temp) + "°");
        holder.textCondition.setText(item.weather.get(0).main);
    }

    @Override
    public int getItemCount() {
        return forecastList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textTime, textTemp, textCondition;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTime = itemView.findViewById(R.id.textTime);
            textTemp = itemView.findViewById(R.id.textTemp);
            textCondition = itemView.findViewById(R.id.textCondition);
        }
    }
}
