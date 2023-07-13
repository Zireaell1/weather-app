package com.zireaell1.weatherapp.domain.usecases.implementations;

import android.util.Log;

import com.zireaell1.weatherapp.domain.entities.City;
import com.zireaell1.weatherapp.domain.exceptions.DataRetrieveException;
import com.zireaell1.weatherapp.domain.repositories.WeatherDataLocalRepository;
import com.zireaell1.weatherapp.domain.usecases.interfaces.GetSavedCities;

import java.io.IOException;
import java.util.List;

public class GetSavedCitiesUseCase implements GetSavedCities {
    private static final String GET_CITIES_ERROR_MESSAGE = "Failed to get cities list";
    private final WeatherDataLocalRepository weatherDataLocalRepository;

    public GetSavedCitiesUseCase(WeatherDataLocalRepository weatherDataLocalRepository) {
        this.weatherDataLocalRepository = weatherDataLocalRepository;
    }

    @Override
    public List<City> execute() throws DataRetrieveException {
        try {
            return weatherDataLocalRepository.getSavedCities();
        }
        catch (IOException e) {
            Log.e("GetSavedCitiesUseCase", "Error trying to get cities list", e);
            throw new DataRetrieveException(GET_CITIES_ERROR_MESSAGE);
        }
    }
}
