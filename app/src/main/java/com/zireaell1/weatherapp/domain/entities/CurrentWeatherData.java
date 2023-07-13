package com.zireaell1.weatherapp.domain.entities;

public class CurrentWeatherData {
    private final City city;
    private final WeatherData weatherData;

    public CurrentWeatherData(City city, WeatherData weatherData) {
        this.city = city;
        this.weatherData = weatherData;
    }

    public City getCity() {
        return city;
    }

    public WeatherData getWeatherData() {
        return weatherData;
    }
}
