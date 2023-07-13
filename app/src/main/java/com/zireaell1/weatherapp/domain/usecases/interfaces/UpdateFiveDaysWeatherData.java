package com.zireaell1.weatherapp.domain.usecases.interfaces;

import com.zireaell1.weatherapp.domain.entities.City;
import com.zireaell1.weatherapp.domain.entities.FiveDaysWeatherData;

import java.util.concurrent.CompletableFuture;

public interface UpdateFiveDaysWeatherData {
    CompletableFuture<FiveDaysWeatherData> execute(City city, String units, String lang);
}
