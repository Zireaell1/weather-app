package com.zireaell1.weatherapp.domain.entities;

public class Main {
    private final double temp;
    private final double feelsLike;
    private final double tempMin;
    private final double tempMax;
    private final int pressure;
    private final int humidity;
    private final int seaLevel;
    private final int grndLevel;

    public Main(double temp, double feelsLike, double tempMin, double tempMax, int pressure, int humidity, int seaLevel, int grndLevel) {
        this.temp = temp;
        this.feelsLike = feelsLike;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.pressure = pressure;
        this.humidity = humidity;
        this.seaLevel = seaLevel;
        this.grndLevel = grndLevel;
    }

    public double getTemp() {
        return temp;
    }

    public double getFeelsLike() {
        return feelsLike;
    }

    public double getTempMin() {
        return tempMin;
    }

    public double getTempMax() {
        return tempMax;
    }

    public int getPressure() {
        return pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getSeaLevel() {
        return seaLevel;
    }

    public int getGrndLevel() {
        return grndLevel;
    }
}
