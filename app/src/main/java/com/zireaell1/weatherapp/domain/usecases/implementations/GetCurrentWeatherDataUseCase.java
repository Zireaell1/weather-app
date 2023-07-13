package com.zireaell1.weatherapp.domain.usecases.implementations;

import android.util.Log;

import com.zireaell1.weatherapp.domain.entities.City;
import com.zireaell1.weatherapp.domain.entities.CurrentWeatherData;
import com.zireaell1.weatherapp.domain.exceptions.DataRetrieveException;
import com.zireaell1.weatherapp.domain.repositories.ConfigRepository;
import com.zireaell1.weatherapp.domain.repositories.WeatherDataApiRepository;
import com.zireaell1.weatherapp.domain.repositories.WeatherDataLocalRepository;
import com.zireaell1.weatherapp.domain.usecases.interfaces.GetCurrentWeatherData;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class GetCurrentWeatherDataUseCase implements GetCurrentWeatherData {
    private static final String DATA_RETRIEVE_ERROR_MESSAGE = "Failed to retrieve data from remote and local data source";
    private final WeatherDataApiRepository weatherDataApiRepository;
    private final WeatherDataLocalRepository weatherDataLocalRepository;
    private final ConfigRepository configRepository;

    public GetCurrentWeatherDataUseCase(WeatherDataApiRepository weatherDataApiRepository, WeatherDataLocalRepository weatherDataLocalRepository, ConfigRepository configRepository) {
        this.weatherDataApiRepository = weatherDataApiRepository;
        this.weatherDataLocalRepository = weatherDataLocalRepository;
        this.configRepository = configRepository;
    }

    @Override
    public CompletableFuture<CurrentWeatherData> execute(City city, String units, String lang) {
        CompletableFuture<CurrentWeatherData> future = new CompletableFuture<>();

        new Thread(() -> {
            try {
                Log.d("GetCurrentWeatherDataUseCase", "Trying to get data from local data source");
                CurrentWeatherData currentWeatherData = weatherDataLocalRepository.getCurrentWeatherData(city);
                Log.d("GetCurrentWeatherDataUseCase", "Successfully retrieved data from local data source");
                future.complete(currentWeatherData);
            } catch (IOException e) {
                Log.d("GetCurrentWeatherDataUseCase", "Local data not found. Trying to get data from remote data source");
                try {
                    CurrentWeatherData currentWeatherData = weatherDataApiRepository.getCurrentWeatherData(city, units, lang);
                    Log.d("GetCurrentWeatherDataUseCase", "Successfully retrieved data from remote data source.");
                    weatherDataLocalRepository.saveCurrentWeatherData(currentWeatherData);
                    configRepository.saveCityUpdateDate(city);
                    Log.d("GetCurrentWeatherDataUseCase", "Successfully saved data to local storage");
                    future.complete(currentWeatherData);
                    return;
                } catch (IOException ex) {
                    Log.e("GetCurrentWeatherDataUseCase", "Error trying to retrieve data from remove data source. Trying to get data from local data source", ex);
                }

                try {
                    future.complete(weatherDataLocalRepository.getCurrentWeatherData(city));
                } catch (IOException ex) {
                    Log.e("GetCurrentWeatherDataUseCase", "Error trying to retrieve data from local data source.", ex);
                    future.completeExceptionally(new DataRetrieveException(DATA_RETRIEVE_ERROR_MESSAGE));
                }
            }
        }).start();

        return future;
    }
}
