package com.zireaell1.weatherapp.domain.repositories;

import com.zireaell1.weatherapp.domain.entities.City;
import com.zireaell1.weatherapp.domain.entities.CurrentWeatherData;
import com.zireaell1.weatherapp.domain.entities.FiveDaysWeatherData;

import java.io.IOException;
import java.util.List;

public interface WeatherDataLocalRepository {
    List<City> getSavedCities() throws IOException;
    City getCityByName(String cityName, String countryName) throws IOException;
    CurrentWeatherData getCurrentWeatherData(City city) throws IOException;
    FiveDaysWeatherData getFiveDaysWeatherData(City city) throws IOException;
    void saveCity(City city) throws IOException;
    void saveCurrentWeatherData(CurrentWeatherData currentWeatherData) throws IOException;
    void saveFiveDaysWeatherData(FiveDaysWeatherData fiveDaysWeatherData) throws IOException;
    void deleteCity(City city) throws IOException;
    void deleteCurrentWeatherData(City city) throws IOException;
    void deleteFiveDaysWeatherData(City city) throws IOException;
}
