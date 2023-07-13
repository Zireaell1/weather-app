package com.zireaell1.weatherapp.domain.repositories;

import com.zireaell1.weatherapp.domain.entities.City;

import java.util.Date;

public interface ConfigRepository {
    String getApiKey();

    int getRefreshTime();

    String getTemperatureUnit();

    Date getCityUpdateDate(City city);

    void saveRefreshTime(int refreshTime);

    void saveTemperatureUnit(String temperatureUnit);

    void saveCityUpdateDate(City city);
}
