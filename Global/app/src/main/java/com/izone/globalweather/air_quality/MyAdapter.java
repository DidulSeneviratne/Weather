package com.izone.globalweather.air_quality;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.izone.globalweather.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {


    Context context;
    List<CityList> items;

    public MyAdapter(Context context, List<CityList> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull  MyViewHolder holder, int position) {

        String sco= String.valueOf(items.get(position).getMeterValue())  + " μg/m3";
        String sNO= String.valueOf(items.get(position).getNo())  + "μg/m3";
        String sNO2= String.valueOf(items.get(position).getNo())  + "μg/m3";
        String sO3= String.valueOf(items.get(position).getO3())  + "μg/m3";
        String sS02= String.valueOf(items.get(position).getS02())  + "μg/m3";
        String snh3= String.valueOf(items.get(position).getNh3())  + "μg/m3";


        holder.nameView.setText(items.get(position).getCity());
        holder.co.setText(sco);
        holder.NO.setText(sNO);
        holder.NO2.setText(sNO2);
        holder.O3.setText(sO3);
        holder.S02.setText(sS02);
        holder.nh3.setText(snh3);

        holder.imageView.setImageResource(items.get(position).getGetimage());

//        holder.emailView.setText(items.get(position).getCity());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
