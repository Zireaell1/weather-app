package com.zireaell1.weatherapp.domain.repositories;

import com.zireaell1.weatherapp.domain.entities.City;

import java.io.IOException;
import java.util.List;

public interface CityApiRepository {
    List<City> getCitiesByName(String cityName) throws IOException;
}
