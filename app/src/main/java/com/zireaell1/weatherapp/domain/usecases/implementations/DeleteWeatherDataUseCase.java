package com.zireaell1.weatherapp.domain.usecases.implementations;

import android.util.Log;

import com.zireaell1.weatherapp.domain.entities.City;
import com.zireaell1.weatherapp.domain.exceptions.DataDeleteException;
import com.zireaell1.weatherapp.domain.repositories.WeatherDataLocalRepository;
import com.zireaell1.weatherapp.domain.usecases.interfaces.DeleteWeatherData;

import java.io.IOException;

public class DeleteWeatherDataUseCase implements DeleteWeatherData {
    private static final String DATA_DELETE_ERROR_MESSAGE = "Failed to delete data from local data source";
    private final WeatherDataLocalRepository weatherDataLocalRepository;

    public DeleteWeatherDataUseCase(WeatherDataLocalRepository weatherDataLocalRepository) {
        this.weatherDataLocalRepository = weatherDataLocalRepository;
    }

    @Override
    public void execute(City city) throws DataDeleteException {
        try {
            weatherDataLocalRepository.deleteCity(city);
            Log.d("DeleteWeatherDataUseCase", "Successfully deleted city file");
            weatherDataLocalRepository.deleteCurrentWeatherData(city);
            Log.d("DeleteWeatherDataUseCase", "Successfully deleted current weather data");
            weatherDataLocalRepository.deleteFiveDaysWeatherData(city);
            Log.d("DeleteWeatherDataUseCase", "Successfully deleted five days weather data");
        } catch (IOException e) {
            Log.e("DeleteWeatherDataUseCase", "Error trying to delete weather data", e);
            throw new DataDeleteException(DATA_DELETE_ERROR_MESSAGE);
        }
    }
}
