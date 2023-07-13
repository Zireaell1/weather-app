package com.zireaell1.weatherapp.domain.entities;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class City {
    private final String name;
    private final Map<String, String> local_names;
    private final double lat;
    private final double lon;
    private final String country;
    private final String state;
    private Date date;

    public City(String name, Map<String, String> local_names, double lat, double lon, String country, String state) {
        this.name = name;
        this.local_names = local_names;
        this.lat = lat;
        this.lon = lon;
        this.country = country;
        this.state = state;
        date = Calendar.getInstance().getTime();
    }

    public String getName() {
        return name;
    }

    public Map<String, String> getLocal_names() {
        return local_names;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getCountry() {
        return country;
    }

    public String getState() {
        return state;
    }

    public Date getDate() {
        return date;
    }

    public void updateDate() {
        date = Calendar.getInstance().getTime();
    }
}
