package com.zireaell1.weatherapp.domain.repositories;

import com.zireaell1.weatherapp.domain.entities.City;
import com.zireaell1.weatherapp.domain.entities.CurrentWeatherData;
import com.zireaell1.weatherapp.domain.entities.FiveDaysWeatherData;

import java.io.IOException;

public interface WeatherDataApiRepository {
    CurrentWeatherData getCurrentWeatherData(City city, String units, String lang) throws IOException;

    FiveDaysWeatherData getFiveDaysWeatherData(City city, String units, String lang) throws IOException;
}
