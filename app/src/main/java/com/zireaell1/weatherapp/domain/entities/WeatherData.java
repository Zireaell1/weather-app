package com.zireaell1.weatherapp.domain.entities;

import java.util.List;

public class WeatherData {
    private final List<Weather> weathers;
    private final String base;
    private final Main main;
    private final int visibility;
    private final Wind wind;
    private final Rain rain;
    private final Snow snow;
    private final Clouds clouds;
    private final int dt;
    private final Sys sys;
    private final String dt_txt;
    private final int timezone;

    public WeatherData(List<Weather> weathers, String base, Main main, int visibility, Wind wind, Rain rain, Snow snow, Clouds clouds, int dt, Sys sys, String dt_txt, int timezone) {
        this.weathers = weathers;
        this.base = base;
        this.main = main;
        this.visibility = visibility;
        this.wind = wind;
        this.rain = rain;
        this.snow = snow;
        this.clouds = clouds;
        this.dt = dt;
        this.sys = sys;
        this.dt_txt = dt_txt;
        this.timezone = timezone;
    }

    public List<Weather> getWeathers() {
        return weathers;
    }

    public String getBase() {
        return base;
    }

    public Main getMain() {
        return main;
    }

    public int getVisibility() {
        return visibility;
    }

    public Wind getWind() {
        return wind;
    }

    public Rain getRain() {
        return rain;
    }

    public Snow getSnow() {
        return snow;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public int getDt() {
        return dt;
    }

    public Sys getSys() {
        return sys;
    }

    public String getDt_txt() {
        return dt_txt;
    }

    public int getTimezone() {
        return timezone;
    }
}
