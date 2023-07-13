package com.zireaell1.weatherapp.domain.entities;

public class Config {
    private final int refreshTime;
    private final String temperatureUnit;

    public Config(int refreshTime, String temperatureUnit) {
        this.refreshTime = refreshTime;
        this.temperatureUnit = temperatureUnit;
    }

    public int getRefreshTime() {
        return refreshTime;
    }

    public String getTemperatureUnit() {
        return temperatureUnit;
    }

    public boolean equals(Config config) {
        return refreshTime == config.getRefreshTime() && temperatureUnit.equals(config.temperatureUnit);
    }
}
