package com.zireaell1.weatherapp.domain.entities;

public class Sys {
    private final int sunrise;
    private final int sunset;
    private final String pod;

    public Sys(int sunrise, int sunset, String pod) {
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.pod = pod;
    }

    public int getSunrise() {
        return sunrise;
    }

    public int getSunset() {
        return sunset;
    }

    public String getPod() {
        return pod;
    }
}
