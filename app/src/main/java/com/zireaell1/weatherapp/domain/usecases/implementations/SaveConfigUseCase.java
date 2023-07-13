package com.zireaell1.weatherapp.domain.usecases.implementations;

import com.zireaell1.weatherapp.domain.entities.Config;
import com.zireaell1.weatherapp.domain.repositories.ConfigRepository;
import com.zireaell1.weatherapp.domain.usecases.interfaces.SaveConfig;

public class SaveConfigUseCase implements SaveConfig {
    private final ConfigRepository configRepository;

    public SaveConfigUseCase(ConfigRepository configRepository) {
        this.configRepository = configRepository;
    }

    @Override
    public void execute(Config config) {
        configRepository.saveRefreshTime(config.getRefreshTime());
        configRepository.saveTemperatureUnit(config.getTemperatureUnit());
    }
}
