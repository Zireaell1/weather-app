package com.zireaell1.weatherapp.domain.usecases.interfaces;

import com.zireaell1.weatherapp.domain.entities.City;
import com.zireaell1.weatherapp.domain.entities.CurrentWeatherData;

import java.util.concurrent.CompletableFuture;

public interface UpdateCurrentWeatherData {
    CompletableFuture<CurrentWeatherData> execute(City city, String units, String lang);
}
