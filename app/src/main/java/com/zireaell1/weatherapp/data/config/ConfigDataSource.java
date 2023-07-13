package com.zireaell1.weatherapp.data.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.zireaell1.weatherapp.R;
import com.zireaell1.weatherapp.domain.entities.City;
import com.zireaell1.weatherapp.domain.repositories.ConfigRepository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ConfigDataSource implements ConfigRepository {
    private static final String REFRESH_TIME_KEY = "refreshTime";
    private static final String TEMPERATURE_UNIT_KEY = "temperatureUnit";
    private final SharedPreferences sharedPreferences;
    private final Context context;

    public ConfigDataSource(Context context, String configName) {
        sharedPreferences = context.getSharedPreferences(configName, Context.MODE_PRIVATE);
        this.context = context;
    }

    @Override
    public String getApiKey() {
        return "";
    }

    @Override
    public int getRefreshTime() {
        int defaultValue = context.getResources().getInteger(R.integer.refresh_time_default);
        return sharedPreferences.getInt(REFRESH_TIME_KEY, defaultValue);
    }

    @Override
    public String getTemperatureUnit() {
        String defaultValue = context.getResources().getString(R.string.temperature_unit_default);
        return sharedPreferences.getString(TEMPERATURE_UNIT_KEY, defaultValue);
    }

    @Override
    public Date getCityUpdateDate(City city) {
        try {
            String defaultValue = "00:00:00 - 00.00.0000";
            String dateStr = sharedPreferences.getString(String.format("city-%s-%s.json", city.getName(), city.getCountry()), defaultValue);
            DateFormat format = new SimpleDateFormat("HH:mm:ss - dd.MM.yyyy", Locale.ENGLISH);
            return format.parse(dateStr);
        } catch (ParseException e) {
            Log.e("ConfigDataSource", "Failed to read date from config", e);
            return new Date();
        }
    }

    @Override
    public void saveRefreshTime(int refreshTime) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(REFRESH_TIME_KEY, refreshTime);
        editor.apply();
    }

    @Override
    public void saveTemperatureUnit(String temperatureUnit) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TEMPERATURE_UNIT_KEY, temperatureUnit);
        editor.apply();
    }

    @Override
    public void saveCityUpdateDate(City city) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        DateFormat format = new SimpleDateFormat("HH:mm:ss - dd.MM.yyyy", Locale.ENGLISH);
        editor.putString(String.format("city-%s-%s.json", city.getName(), city.getCountry()), format.format(city.getDate()));
        editor.apply();
    }
}
