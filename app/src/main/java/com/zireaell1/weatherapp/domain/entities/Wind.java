package com.zireaell1.weatherapp.domain.entities;

public class Wind {
    private final double speed;
    private final double deg;
    private final double gust;

    public Wind(double speed, double deg, double gust) {
        this.speed = speed;
        this.deg = deg;
        this.gust = gust;
    }

    public double getSpeed() {
        return speed;
    }

    public double getDeg() {
        return deg;
    }

    public double getGust() {
        return gust;
    }
}
