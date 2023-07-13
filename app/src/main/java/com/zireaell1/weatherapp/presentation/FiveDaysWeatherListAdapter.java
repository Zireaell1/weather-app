package com.zireaell1.weatherapp.presentation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zireaell1.weatherapp.R;
import com.zireaell1.weatherapp.domain.entities.WeatherData;
import com.zireaell1.weatherapp.domain.usecases.interfaces.GetConfig;

import java.util.Collections;
import java.util.List;

public class FiveDaysWeatherListAdapter extends RecyclerView.Adapter<FiveDaysWeatherViewHolder> {
    List<WeatherData> list = Collections.emptyList();
    Context context;
    GetConfig getConfig;

    public FiveDaysWeatherListAdapter(List<WeatherData> list, Context context, GetConfig getConfig) {
        this.list = list;
        this.context = context;
        this.getConfig = getConfig;
    }

    @NonNull
    @Override
    public FiveDaysWeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View weatherView = inflater.inflate(R.layout.five_days_weather_list_item, parent, false);
        return new FiveDaysWeatherViewHolder(weatherView);
    }

    @Override
    public void onBindViewHolder(@NonNull FiveDaysWeatherViewHolder holder, int position) {
        holder.time.setText(list.get(position).getDt_txt().split(" ")[1]);
        switch (list.get(position).getWeathers().get(0).getIcon()) {
            case "01d":
            case "01n":
                holder.weatherIcon.setImageResource(R.drawable.sunny_24px);
                break;
            case "02d":
            case "03d":
            case "04d":
            case "02n":
            case "03n":
            case "04n":
                holder.weatherIcon.setImageResource(R.drawable.cloudy_24px);
                break;
            case "09d":
            case "10d":
            case "09n":
            case "10n":
                holder.weatherIcon.setImageResource(R.drawable.rainy_24px);
                break;
            case "11d":
            case "11n":
                holder.weatherIcon.setImageResource(R.drawable.thunderstorm_24px);
                break;
            case "13d":
            case "13n":
                holder.weatherIcon.setImageResource(R.drawable.weather_snowy_24px);
                break;
            case "50d":
            case "50n":
                holder.weatherIcon.setImageResource(R.drawable.mist_24px);
                break;
        }

        switch (getConfig.execute().getTemperatureUnit()) {
            case "standard":
                holder.temperature.setText(context.getString(R.string.standard_temperature, list.get(position).getMain().getTemp()));
                break;
            case "metric":
                holder.temperature.setText(context.getString(R.string.metric_temperature, list.get(position).getMain().getTemp()));
                break;
            case "imperial":
                holder.temperature.setText(context.getString(R.string.imperial_temperature, list.get(position).getMain().getTemp()));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
