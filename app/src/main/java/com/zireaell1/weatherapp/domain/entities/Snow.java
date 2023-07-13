package com.zireaell1.weatherapp.domain.entities;

public class Snow {
    private final double snow1h;
    private final double snow3h;

    public Snow(double snow1h, double snow3h) {
        this.snow1h = snow1h;
        this.snow3h = snow3h;
    }

    public double getSnow1h() {
        return snow1h;
    }

    public double getSnow3h() {
        return snow3h;
    }
}
