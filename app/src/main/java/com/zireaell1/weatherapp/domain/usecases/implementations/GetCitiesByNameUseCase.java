package com.zireaell1.weatherapp.domain.usecases.implementations;

import android.util.Log;

import com.zireaell1.weatherapp.domain.entities.City;
import com.zireaell1.weatherapp.domain.exceptions.DataRetrieveException;
import com.zireaell1.weatherapp.domain.repositories.CityApiRepository;
import com.zireaell1.weatherapp.domain.usecases.interfaces.GetCitiesByName;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GetCitiesByNameUseCase implements GetCitiesByName {
    private static final String DATA_RETRIEVE_ERROR_MESSAGE = "Failed to retrieve data from remote data source";
    private final CityApiRepository cityApiRepository;

    public GetCitiesByNameUseCase(CityApiRepository cityApiRepository) {
        this.cityApiRepository = cityApiRepository;
    }

    @Override
    public CompletableFuture<List<City>> execute(String cityName) {
        CompletableFuture<List<City>> future = new CompletableFuture<>();

        new Thread(() -> {
            try {
                future.complete(cityApiRepository.getCitiesByName(cityName));
            } catch (IOException e) {
                Log.e("GetCitiesByNameUseCase", "Error trying to retrieve data from remote data source.", e);
                future.completeExceptionally(new DataRetrieveException(DATA_RETRIEVE_ERROR_MESSAGE));
            }
        }).start();

        return future;
    }
}
