package com.zireaell1.weatherapp.domain.usecases.interfaces;

import com.zireaell1.weatherapp.domain.entities.City;
import com.zireaell1.weatherapp.domain.exceptions.DataRetrieveException;

import java.util.List;

public interface GetSavedCities {
    List<City> execute() throws DataRetrieveException;
}
