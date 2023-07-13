package com.zireaell1.weatherapp.presentation;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.zireaell1.weatherapp.R;
import com.zireaell1.weatherapp.domain.entities.CurrentWeatherData;

import java.util.concurrent.CompletableFuture;

public class CurrentWeatherFragment extends Fragment {
    private AppStateViewModel viewModel;

    public CurrentWeatherFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = ((MainActivity) requireActivity()).getViewModel();

        View view = inflater.inflate(R.layout.fragment_current_weather, container, false);

        CompletableFuture<CurrentWeatherData> futureCurrentWeatherData = viewModel.getGetCurrentWeatherData().execute(viewModel.getCurrentCity(), viewModel.getGetConfig().execute().getTemperatureUnit(), "en");
        futureCurrentWeatherData.exceptionally(e -> {
            Log.e("CurrentWeatherFragment", "Failed to get current weather data", e);
            return null;
        });
        futureCurrentWeatherData.thenAccept(currentWeatherData -> getActivity().runOnUiThread(() -> {
            ImageView currentWeatherIcon = view.findViewById(R.id.current_weather_icon);
            switch (currentWeatherData.getWeatherData().getWeathers().get(0).getIcon()) {
                case "01d":
                case "01n":
                    currentWeatherIcon.setImageResource(R.drawable.sunny_24px);
                    break;
                case "02d":
                case "03d":
                case "04d":
                case "02n":
                case "03n":
                case "04n":
                    currentWeatherIcon.setImageResource(R.drawable.cloudy_24px);
                    break;
                case "09d":
                case "10d":
                case "09n":
                case "10n":
                    currentWeatherIcon.setImageResource(R.drawable.rainy_24px);
                    break;
                case "11d":
                case "11n":
                    currentWeatherIcon.setImageResource(R.drawable.thunderstorm_24px);
                    break;
                case "13d":
                case "13n":
                    currentWeatherIcon.setImageResource(R.drawable.weather_snowy_24px);
                    break;
                case "50d":
                case "50n":
                    currentWeatherIcon.setImageResource(R.drawable.mist_24px);
                    break;
            }
            TextView currentTemperature = view.findViewById(R.id.current_temperature);
            TextView feelsLike = view.findViewById(R.id.feels_like);
            TextView wind = view.findViewById(R.id.wind);
            TextView humidity = view.findViewById(R.id.humidity);
            TextView pressure = view.findViewById(R.id.pressure);
            TextView visibility = view.findViewById(R.id.visibility);
            TextView windDirection = view.findViewById(R.id.wind_direction);

            humidity.setText(view.getContext().getString(R.string.percent, currentWeatherData.getWeatherData().getMain().getHumidity()));
            pressure.setText(view.getContext().getString(R.string.hpa, currentWeatherData.getWeatherData().getMain().getPressure()));
            visibility.setText(view.getContext().getString(R.string.meter, currentWeatherData.getWeatherData().getVisibility()));
            windDirection.setText(String.format("%f", currentWeatherData.getWeatherData().getWind().getDeg()));

            switch (viewModel.getGetConfig().execute().getTemperatureUnit()) {
                case "standard":
                    currentTemperature.setText(view.getContext().getString(R.string.standard_temperature, currentWeatherData.getWeatherData().getMain().getTemp()));
                    wind.setText(view.getContext().getString(R.string.standard_speed, currentWeatherData.getWeatherData().getWind().getSpeed()));
                    feelsLike.setText(view.getContext().getString(R.string.standard_temperature, currentWeatherData.getWeatherData().getMain().getFeelsLike()));
                    break;
                case "metric":
                    currentTemperature.setText(view.getContext().getString(R.string.metric_temperature, currentWeatherData.getWeatherData().getMain().getTemp()));
                    wind.setText(view.getContext().getString(R.string.metric_speed, currentWeatherData.getWeatherData().getWind().getSpeed()));
                    feelsLike.setText(view.getContext().getString(R.string.metric_temperature, currentWeatherData.getWeatherData().getMain().getFeelsLike()));
                    break;
                case "imperial":
                    currentTemperature.setText(view.getContext().getString(R.string.imperial_temperature, currentWeatherData.getWeatherData().getMain().getTemp()));
                    wind.setText(view.getContext().getString(R.string.imperial_speed, currentWeatherData.getWeatherData().getWind().getSpeed()));
                    feelsLike.setText(view.getContext().getString(R.string.imperial_temperature, currentWeatherData.getWeatherData().getMain().getFeelsLike()));
                    break;
            }
        }));
        return view;
    }
}