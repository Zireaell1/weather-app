package com.zireaell1.weatherapp.presentation;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zireaell1.weatherapp.R;

public class FiveDaysWeatherViewHolder extends RecyclerView.ViewHolder {
    TextView time;
    ImageView weatherIcon;
    TextView temperature;
    View view;

    public FiveDaysWeatherViewHolder(View itemView) {
        super(itemView);
        time = itemView.findViewById(R.id.time);
        weatherIcon = itemView.findViewById(R.id.weather_icon);
        temperature = itemView.findViewById(R.id.temperature);
        view = itemView;
    }
}
