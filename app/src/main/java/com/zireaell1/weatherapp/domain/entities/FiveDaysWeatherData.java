package com.zireaell1.weatherapp.domain.entities;

import java.util.List;

public class FiveDaysWeatherData {
    private final City city;
    private final List<WeatherData> weatherDataList;

    public FiveDaysWeatherData(City city, List<WeatherData> weatherDataList) {
        this.city = city;
        this.weatherDataList = weatherDataList;
    }

    public City getCity() {
        return city;
    }

    public List<WeatherData> getWeatherDataList() {
        return weatherDataList;
    }
}
