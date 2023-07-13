package com.zireaell1.weatherapp.data.api;

import android.annotation.SuppressLint;
import android.util.JsonReader;

import com.zireaell1.weatherapp.domain.entities.City;
import com.zireaell1.weatherapp.domain.repositories.CityApiRepository;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CityApiDataSource implements CityApiRepository {
    private static final int LIMIT = 5;
    private static final String API_URL_PATTERN = "https://api.openweathermap.org/geo/1.0/direct?q=%s&limit=%d&appid=%s";
    private final String apiKey;

    public CityApiDataSource(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public List<City> getCitiesByName(String cityName) throws IOException {
        @SuppressLint("DefaultLocale") URL url = new URL(String.format(API_URL_PATTERN, cityName, LIMIT, apiKey));
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            return readJsonStreamCity(new BufferedInputStream(urlConnection.getInputStream()));
        } finally {
            urlConnection.disconnect();
        }
    }

    private List<City> readJsonStreamCity(InputStream in) throws IOException {
        try (JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)))) {
            return readCitiesArray(reader);
        }
    }

    private List<City> readCitiesArray(JsonReader reader) throws IOException {
        List<City> cities = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            cities.add(readCity(reader));
        }
        reader.endArray();
        return cities;
    }

    private City readCity(JsonReader reader) throws IOException {
        String name = "";
        Map<String, String> local_names = null;
        double lat = 0.0;
        double lon = 0.0;
        String country = "";
        String state = "";

        reader.beginObject();
        while (reader.hasNext()) {
            String jsonName = reader.nextName();
            switch (jsonName) {
                case "name":
                    name = reader.nextString();
                    break;
                case "local_names":
                    local_names = readLocalNames(reader);
                    break;
                case "lat":
                    lat = reader.nextDouble();
                    break;
                case "lon":
                    lon = reader.nextDouble();
                    break;
                case "country":
                    country = reader.nextString();
                    break;
                case "state":
                    state = reader.nextString();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return new City(name, local_names, lat, lon, country, state);
    }

    private Map<String, String> readLocalNames(JsonReader reader) throws IOException {
        Map<String, String> localNames = new HashMap<>();

        reader.beginObject();
        while (reader.hasNext()) {
            localNames.put(reader.nextName(), reader.nextString());
        }
        reader.endObject();
        return localNames;
    }
}
