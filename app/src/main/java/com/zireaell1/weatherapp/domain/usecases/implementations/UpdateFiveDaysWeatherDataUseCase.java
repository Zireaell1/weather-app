package com.zireaell1.weatherapp.domain.usecases.implementations;

import android.util.Log;

import com.zireaell1.weatherapp.domain.entities.City;
import com.zireaell1.weatherapp.domain.entities.FiveDaysWeatherData;
import com.zireaell1.weatherapp.domain.exceptions.DataUpdateException;
import com.zireaell1.weatherapp.domain.repositories.ConfigRepository;
import com.zireaell1.weatherapp.domain.repositories.WeatherDataApiRepository;
import com.zireaell1.weatherapp.domain.repositories.WeatherDataLocalRepository;
import com.zireaell1.weatherapp.domain.usecases.interfaces.UpdateFiveDaysWeatherData;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class UpdateFiveDaysWeatherDataUseCase implements UpdateFiveDaysWeatherData {
    private static final String DATA_UPDATE_ERROR_MESSAGE = "Failed to update local data";
    private final WeatherDataApiRepository weatherDataApiRepository;
    private final WeatherDataLocalRepository weatherDataLocalRepository;
    private final ConfigRepository configRepository;

    public UpdateFiveDaysWeatherDataUseCase(WeatherDataApiRepository weatherDataApiRepository, WeatherDataLocalRepository weatherDataLocalRepository, ConfigRepository configRepository) {
        this.weatherDataApiRepository = weatherDataApiRepository;
        this.weatherDataLocalRepository = weatherDataLocalRepository;
        this.configRepository = configRepository;
    }

    @Override
    public CompletableFuture<FiveDaysWeatherData> execute(City city, String units, String lang) {
        CompletableFuture<FiveDaysWeatherData> future = new CompletableFuture<>();

        new Thread(() -> {
            try {
                Log.d("UpdateFiveDaysWeatherDataUseCase", "Trying to update local data");
                FiveDaysWeatherData fiveDaysWeatherData = weatherDataApiRepository.getFiveDaysWeatherData(city, units, lang);
                Log.d("UpdateFiveDaysWeatherDataUseCase", "Successfully retrieved data from remote data source");
                weatherDataLocalRepository.saveFiveDaysWeatherData(fiveDaysWeatherData);
                configRepository.saveCityUpdateDate(city);
                Log.d("UpdateFiveDaysWeatherDataUseCase", "Successfully updated local data");
                future.complete(fiveDaysWeatherData);
            } catch (IOException e) {
                Log.d("UpdateFiveDaysWeatherDataUseCase", "Failed to update local data");
                future.completeExceptionally(new DataUpdateException(DATA_UPDATE_ERROR_MESSAGE));
            }
        }).start();

        return future;
    }
}
