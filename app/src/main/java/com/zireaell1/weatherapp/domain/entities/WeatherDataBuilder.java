package com.zireaell1.weatherapp.domain.entities;

import java.util.List;

public class WeatherDataBuilder {
    private List<Weather> weathers;
    private String base;
    private Main main;
    private int visibility;
    private Wind wind;
    private Rain rain;
    private Snow snow;
    private Clouds clouds;
    private int dt;
    private Sys sys;
    private String dt_txt;
    private int timezone;

    public WeatherDataBuilder setWeathers(List<Weather> weathers) {
        this.weathers = weathers;
        return this;
    }

    public WeatherDataBuilder setBase(String base) {
        this.base = base;
        return this;
    }

    public WeatherDataBuilder setMain(Main main) {
        this.main = main;
        return this;
    }

    public WeatherDataBuilder setVisibility(int visibility) {
        this.visibility = visibility;
        return this;
    }

    public WeatherDataBuilder setWind(Wind wind) {
        this.wind = wind;
        return this;
    }

    public WeatherDataBuilder setRain(Rain rain) {
        this.rain = rain;
        return this;
    }

    public WeatherDataBuilder setSnow(Snow snow) {
        this.snow = snow;
        return this;
    }

    public WeatherDataBuilder setClouds(Clouds clouds) {
        this.clouds = clouds;
        return this;
    }

    public WeatherDataBuilder setDt(int dt) {
        this.dt = dt;
        return this;
    }

    public WeatherDataBuilder setSys(Sys sys) {
        this.sys = sys;
        return this;
    }

    public WeatherDataBuilder setDt_txt(String dt_txt) {
        this.dt_txt = dt_txt;
        return this;
    }

    public WeatherDataBuilder setTimezone(int timezone) {
        this.timezone = timezone;
        return this;
    }

    public WeatherData build() {
        return new WeatherData(weathers, base, main, visibility, wind, rain, snow, clouds, dt, sys, dt_txt, timezone);
    }
}
