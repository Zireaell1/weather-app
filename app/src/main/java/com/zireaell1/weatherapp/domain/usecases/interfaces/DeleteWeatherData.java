package com.zireaell1.weatherapp.domain.usecases.interfaces;

import com.zireaell1.weatherapp.domain.entities.City;
import com.zireaell1.weatherapp.domain.exceptions.DataDeleteException;

public interface DeleteWeatherData {
    void execute(City city) throws DataDeleteException;
}
