package com.izone.globalweather.air_quality;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.izone.globalweather.R;

public class MyViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView nameView, emailView;

    TextView  nh3, NO2, S02, O3, NO, co;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageview);
        nameView = itemView.findViewById(R.id.name);

        co = itemView.findViewById(R.id.co);
        NO = itemView.findViewById(R.id.NO);
        NO2 = itemView.findViewById(R.id.NO2);
        O3 = itemView.findViewById(R.id.O3);
        S02 = itemView.findViewById(R.id.S02);
        nh3 = itemView.findViewById(R.id.nh3);
    }
}
