package com.zireaell1.weatherapp.domain.usecases.implementations;

import android.util.Log;

import com.zireaell1.weatherapp.domain.entities.City;
import com.zireaell1.weatherapp.domain.entities.FiveDaysWeatherData;
import com.zireaell1.weatherapp.domain.exceptions.DataRetrieveException;
import com.zireaell1.weatherapp.domain.repositories.ConfigRepository;
import com.zireaell1.weatherapp.domain.repositories.WeatherDataApiRepository;
import com.zireaell1.weatherapp.domain.repositories.WeatherDataLocalRepository;
import com.zireaell1.weatherapp.domain.usecases.interfaces.GetFiveDaysWeatherData;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class GetFiveDaysWeatherDataUseCase implements GetFiveDaysWeatherData {
    private static final String DATA_RETRIEVE_ERROR_MESSAGE = "Failed to retrieve data from remote and local data source";
    private final WeatherDataApiRepository weatherDataApiRepository;
    private final WeatherDataLocalRepository weatherDataLocalRepository;
    private final ConfigRepository configRepository;

    public GetFiveDaysWeatherDataUseCase(WeatherDataApiRepository weatherDataApiRepository, WeatherDataLocalRepository weatherDataLocalRepository, ConfigRepository configRepository) {
        this.weatherDataApiRepository = weatherDataApiRepository;
        this.weatherDataLocalRepository = weatherDataLocalRepository;
        this.configRepository = configRepository;
    }

    @Override
    public CompletableFuture<FiveDaysWeatherData> execute(City city, String units, String lang) {
        CompletableFuture<FiveDaysWeatherData> future = new CompletableFuture<>();

        new Thread(() -> {
            try {
                Log.d("GetFiveDaysWeatherDataUseCase", "Trying to get data from local data source");
                FiveDaysWeatherData fiveDaysWeatherData = weatherDataLocalRepository.getFiveDaysWeatherData(city);
                Log.d("GetFiveDaysWeatherDataUseCase", "Successfully retrieved data from local data source");
                future.complete(fiveDaysWeatherData);
            } catch (IOException e) {
                try {
                    FiveDaysWeatherData fiveDaysWeatherData = weatherDataApiRepository.getFiveDaysWeatherData(city, units, lang);
                    Log.d("GetFiveDaysWeatherDataUseCase", "Successfully retrieved data from remote data source.");
                    weatherDataLocalRepository.saveFiveDaysWeatherData(fiveDaysWeatherData);
                    configRepository.saveCityUpdateDate(city);
                    Log.d("GetFiveDaysWeatherDataUseCase", "Successfully saved data to local storage");
                    future.complete(fiveDaysWeatherData);
                    return;
                } catch (IOException ex) {
                    Log.e("GetFiveDaysWeatherDataUseCase", "Error trying to retrieve data from remove data source. Trying to get data from local data source", ex);
                }

                try {
                    future.complete(weatherDataLocalRepository.getFiveDaysWeatherData(city));
                } catch (IOException ex) {
                    Log.e("GetFiveDaysWeatherDataUseCase", "Error trying to retrieve data from local data source.", ex);
                    future.completeExceptionally(new DataRetrieveException(DATA_RETRIEVE_ERROR_MESSAGE));
                }
            }
        }).start();

        return future;
    }
}
