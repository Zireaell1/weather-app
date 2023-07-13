package com.zireaell1.weatherapp.domain.usecases.implementations;

import android.util.Log;

import com.zireaell1.weatherapp.domain.entities.City;
import com.zireaell1.weatherapp.domain.exceptions.AddCityException;
import com.zireaell1.weatherapp.domain.repositories.WeatherDataLocalRepository;
import com.zireaell1.weatherapp.domain.usecases.interfaces.AddCity;

import java.io.IOException;

public class AddCityUseCase implements AddCity {
    private static final String ADD_CITY_ERROR_MESSAGE = "Failed to add new city";
    private final WeatherDataLocalRepository weatherDataLocalRepository;

    public AddCityUseCase(WeatherDataLocalRepository weatherDataLocalRepository) {
        this.weatherDataLocalRepository = weatherDataLocalRepository;
    }

    @Override
    public void execute(City city) throws AddCityException {
        try {
            weatherDataLocalRepository.saveCity(city);
        }
        catch (IOException e) {
            Log.e("AddCityUseCase", "Error trying to add new city", e);
            throw new AddCityException(ADD_CITY_ERROR_MESSAGE);
        }
    }
}
