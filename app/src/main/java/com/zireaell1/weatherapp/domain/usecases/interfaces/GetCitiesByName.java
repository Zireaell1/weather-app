package com.zireaell1.weatherapp.domain.usecases.interfaces;

import com.zireaell1.weatherapp.domain.entities.City;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface GetCitiesByName {
    CompletableFuture<List<City>> execute(String cityName);
}
