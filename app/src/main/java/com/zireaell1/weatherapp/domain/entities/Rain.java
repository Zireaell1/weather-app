package com.zireaell1.weatherapp.domain.entities;

public class Rain {
    private final double rain1h;
    private final double rain3h;

    public Rain(double rain1h, double rain3h) {
        this.rain1h = rain1h;
        this.rain3h = rain3h;
    }

    public double getRain1h() {
        return rain1h;
    }

    public double getRain3h() {
        return rain3h;
    }
}
