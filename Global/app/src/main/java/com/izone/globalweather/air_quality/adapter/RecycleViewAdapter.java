package com.izone.globalweather.air_quality.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.izone.globalweather.R;

import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {
    private List<City> cityArrayList;
    private Context context;
    private RecycleViewInterface recycleViewInterface;

    public RecycleViewAdapter(List<City> cityArrayList, Context context, RecycleViewInterface recycleViewInterface) {
        this.cityArrayList = cityArrayList;
        this.context = context;
        this.recycleViewInterface = recycleViewInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_search_item,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        City city = cityArrayList.get(position);
        holder.countryName.setText(city.getCountry());
    }

    @Override
    public int getItemCount() {
        return cityArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView countryName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            countryName = itemView.findViewById(R.id.countryName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recycleViewInterface != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            recycleViewInterface.onItemClick(cityArrayList.get(position));
                        }
                    }
                }
            });
        }
    }
}
