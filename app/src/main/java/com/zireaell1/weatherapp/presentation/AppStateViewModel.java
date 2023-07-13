package com.zireaell1.weatherapp.presentation;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.zireaell1.weatherapp.R;
import com.zireaell1.weatherapp.data.api.CityApiDataSource;
import com.zireaell1.weatherapp.data.api.WeatherDataApiDataSource;
import com.zireaell1.weatherapp.data.config.ConfigDataSource;
import com.zireaell1.weatherapp.data.local.WeatherDataLocalDataSource;
import com.zireaell1.weatherapp.domain.entities.City;
import com.zireaell1.weatherapp.domain.repositories.CityApiRepository;
import com.zireaell1.weatherapp.domain.repositories.ConfigRepository;
import com.zireaell1.weatherapp.domain.repositories.WeatherDataApiRepository;
import com.zireaell1.weatherapp.domain.repositories.WeatherDataLocalRepository;
import com.zireaell1.weatherapp.domain.usecases.implementations.AddCityUseCase;
import com.zireaell1.weatherapp.domain.usecases.implementations.DeleteWeatherDataUseCase;
import com.zireaell1.weatherapp.domain.usecases.implementations.GetCitiesByNameUseCase;
import com.zireaell1.weatherapp.domain.usecases.implementations.GetConfigUseCase;
import com.zireaell1.weatherapp.domain.usecases.implementations.GetCurrentWeatherDataUseCase;
import com.zireaell1.weatherapp.domain.usecases.implementations.GetFiveDaysWeatherDataUseCase;
import com.zireaell1.weatherapp.domain.usecases.implementations.GetSavedCitiesUseCase;
import com.zireaell1.weatherapp.domain.usecases.implementations.SaveConfigUseCase;
import com.zireaell1.weatherapp.domain.usecases.implementations.UpdateCurrentWeatherDataUseCase;
import com.zireaell1.weatherapp.domain.usecases.implementations.UpdateFiveDaysWeatherDataUseCase;
import com.zireaell1.weatherapp.domain.usecases.interfaces.AddCity;
import com.zireaell1.weatherapp.domain.usecases.interfaces.DeleteWeatherData;
import com.zireaell1.weatherapp.domain.usecases.interfaces.GetCitiesByName;
import com.zireaell1.weatherapp.domain.usecases.interfaces.GetConfig;
import com.zireaell1.weatherapp.domain.usecases.interfaces.GetCurrentWeatherData;
import com.zireaell1.weatherapp.domain.usecases.interfaces.GetFiveDaysWeatherData;
import com.zireaell1.weatherapp.domain.usecases.interfaces.GetSavedCities;
import com.zireaell1.weatherapp.domain.usecases.interfaces.SaveConfig;
import com.zireaell1.weatherapp.domain.usecases.interfaces.UpdateCurrentWeatherData;
import com.zireaell1.weatherapp.domain.usecases.interfaces.UpdateFiveDaysWeatherData;

public class AppStateViewModel extends ViewModel {
    private final ConfigRepository configRepository;
    private final CityApiRepository cityApiRepository;
    private final WeatherDataApiRepository weatherDataApiRepository;
    private final WeatherDataLocalRepository weatherDataLocalRepository;

    private final AddCity addCity;
    private final DeleteWeatherData deleteWeatherData;
    private final GetCitiesByName getCitiesByName;
    private final GetConfig getConfig;
    private final GetCurrentWeatherData getCurrentWeatherData;
    private final GetFiveDaysWeatherData getFiveDaysWeatherData;
    private final GetSavedCities getSavedCities;
    private final SaveConfig saveConfig;
    private final UpdateCurrentWeatherData updateCurrentWeatherData;
    private final UpdateFiveDaysWeatherData updateFiveDaysWeatherData;
    private City currentCity;
    private String currentFragment;

    public AppStateViewModel(Context context) {
        configRepository = new ConfigDataSource(context, context.getString(R.string.config_shared_preferences_name));
        cityApiRepository = new CityApiDataSource(configRepository.getApiKey());
        weatherDataApiRepository = new WeatherDataApiDataSource(configRepository.getApiKey());
        weatherDataLocalRepository = new WeatherDataLocalDataSource(context);

        addCity = new AddCityUseCase(weatherDataLocalRepository);
        deleteWeatherData = new DeleteWeatherDataUseCase(weatherDataLocalRepository);
        getCitiesByName = new GetCitiesByNameUseCase(cityApiRepository);
        getConfig = new GetConfigUseCase(configRepository);
        getCurrentWeatherData = new GetCurrentWeatherDataUseCase(weatherDataApiRepository, weatherDataLocalRepository, configRepository);
        getFiveDaysWeatherData = new GetFiveDaysWeatherDataUseCase(weatherDataApiRepository, weatherDataLocalRepository, configRepository);
        getSavedCities = new GetSavedCitiesUseCase(weatherDataLocalRepository);
        saveConfig = new SaveConfigUseCase(configRepository);
        updateCurrentWeatherData = new UpdateCurrentWeatherDataUseCase(weatherDataApiRepository, weatherDataLocalRepository, configRepository);
        updateFiveDaysWeatherData = new UpdateFiveDaysWeatherDataUseCase(weatherDataApiRepository, weatherDataLocalRepository, configRepository);

        currentFragment = "weather";
    }

    public AddCity getAddCity() {
        return addCity;
    }

    public DeleteWeatherData getDeleteWeatherData() {
        return deleteWeatherData;
    }

    public GetCitiesByName getGetCitiesByName() {
        return getCitiesByName;
    }

    public GetConfig getGetConfig() {
        return getConfig;
    }

    public GetCurrentWeatherData getGetCurrentWeatherData() {
        return getCurrentWeatherData;
    }

    public GetFiveDaysWeatherData getGetFiveDaysWeatherData() {
        return getFiveDaysWeatherData;
    }

    public GetSavedCities getGetSavedCities() {
        return getSavedCities;
    }

    public SaveConfig getSaveConfig() {
        return saveConfig;
    }

    public UpdateCurrentWeatherData getUpdateCurrentWeatherData() {
        return updateCurrentWeatherData;
    }

    public UpdateFiveDaysWeatherData getUpdateFiveDaysWeatherData() {
        return updateFiveDaysWeatherData;
    }

    public ConfigRepository getConfigRepository() {
        return configRepository;
    }

    public City getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(City currentCity) {
        this.currentCity = currentCity;
    }

    public String getCurrentFragment() {
        return currentFragment;
    }

    public void setCurrentFragment(String currentFragment) {
        this.currentFragment = currentFragment;
    }
}
