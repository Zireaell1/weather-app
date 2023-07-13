package com.zireaell1.weatherapp.domain.usecases.implementations;

import com.zireaell1.weatherapp.domain.entities.Config;
import com.zireaell1.weatherapp.domain.repositories.ConfigRepository;
import com.zireaell1.weatherapp.domain.usecases.interfaces.GetConfig;

public class GetConfigUseCase implements GetConfig {
    private final ConfigRepository configRepository;

    public GetConfigUseCase(ConfigRepository configRepository) {
        this.configRepository = configRepository;
    }

    @Override
    public Config execute() {
        return new Config(configRepository.getRefreshTime(), configRepository.getTemperatureUnit());
    }
}
