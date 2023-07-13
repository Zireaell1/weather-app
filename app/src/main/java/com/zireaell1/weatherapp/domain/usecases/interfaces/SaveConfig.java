package com.zireaell1.weatherapp.domain.usecases.interfaces;

import com.zireaell1.weatherapp.domain.entities.Config;

public interface SaveConfig {
    void execute(Config config);
}
