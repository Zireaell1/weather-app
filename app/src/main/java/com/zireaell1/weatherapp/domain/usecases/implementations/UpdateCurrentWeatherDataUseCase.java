package com.zireaell1.weatherapp.domain.usecases.implementations;

import android.util.Log;

import com.zireaell1.weatherapp.domain.entities.City;
import com.zireaell1.weatherapp.domain.entities.CurrentWeatherData;
import com.zireaell1.weatherapp.domain.exceptions.DataUpdateException;
import com.zireaell1.weatherapp.domain.repositories.ConfigRepository;
import com.zireaell1.weatherapp.domain.repositories.WeatherDataApiRepository;
import com.zireaell1.weatherapp.domain.repositories.WeatherDataLocalRepository;
import com.zireaell1.weatherapp.domain.usecases.interfaces.UpdateCurrentWeatherData;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class UpdateCurrentWeatherDataUseCase implements UpdateCurrentWeatherData {
    private static final String DATA_UPDATE_ERROR_MESSAGE = "Failed to update local data";
    private final WeatherDataApiRepository weatherDataApiRepository;
    private final WeatherDataLocalRepository weatherDataLocalRepository;
    private final ConfigRepository configRepository;

    public UpdateCurrentWeatherDataUseCase(WeatherDataApiRepository weatherDataApiRepository, WeatherDataLocalRepository weatherDataLocalRepository, ConfigRepository configRepository) {
        this.weatherDataApiRepository = weatherDataApiRepository;
        this.weatherDataLocalRepository = weatherDataLocalRepository;
        this.configRepository = configRepository;
    }

    @Override
    public CompletableFuture<CurrentWeatherData> execute(City city, String units, String lang) {
        CompletableFuture<CurrentWeatherData> future = new CompletableFuture<>();

        new Thread(() -> {
            try {
                Log.d("UpdateCurrentWeatherDataUseCase", "Trying to update local data");
                CurrentWeatherData currentWeatherData = weatherDataApiRepository.getCurrentWeatherData(city, units, lang);
                Log.d("UpdateCurrentWeatherDataUseCase", "Successfully retrieved data from remote data source");
                weatherDataLocalRepository.saveCurrentWeatherData(currentWeatherData);
                configRepository.saveCityUpdateDate(city);
                Log.d("UpdateCurrentWeatherDataUseCase", "Successfully updated local data");
                future.complete(currentWeatherData);
            } catch (IOException e) {
                Log.d("UpdateCurrentWeatherDataUseCase", "Failed to update local data");
                future.completeExceptionally(new DataUpdateException(DATA_UPDATE_ERROR_MESSAGE));
            }
        }).start();

        return future;
    }
}
