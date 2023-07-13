package com.zireaell1.weatherapp.domain.usecases.interfaces;

import com.zireaell1.weatherapp.domain.entities.City;
import com.zireaell1.weatherapp.domain.exceptions.AddCityException;

public interface AddCity {
    void execute(City city) throws AddCityException;
}
