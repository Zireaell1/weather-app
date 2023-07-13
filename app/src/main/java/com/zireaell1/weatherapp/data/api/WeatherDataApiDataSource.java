package com.zireaell1.weatherapp.data.api;

import android.util.JsonReader;

import com.zireaell1.weatherapp.domain.entities.City;
import com.zireaell1.weatherapp.domain.entities.Clouds;
import com.zireaell1.weatherapp.domain.entities.CurrentWeatherData;
import com.zireaell1.weatherapp.domain.entities.FiveDaysWeatherData;
import com.zireaell1.weatherapp.domain.entities.Main;
import com.zireaell1.weatherapp.domain.entities.Rain;
import com.zireaell1.weatherapp.domain.entities.Snow;
import com.zireaell1.weatherapp.domain.entities.Sys;
import com.zireaell1.weatherapp.domain.entities.Weather;
import com.zireaell1.weatherapp.domain.entities.WeatherData;
import com.zireaell1.weatherapp.domain.entities.WeatherDataBuilder;
import com.zireaell1.weatherapp.domain.entities.Wind;
import com.zireaell1.weatherapp.domain.repositories.WeatherDataApiRepository;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WeatherDataApiDataSource implements WeatherDataApiRepository {
    private static final String API_URL_PATTERN_CURRENT = "https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s&units=%s&lang=%s";
    private static final String API_URL_PATTERN_FIVE = "https://api.openweathermap.org/data/2.5/forecast?lat=%s&lon=%s&appid=%s&units=%s&lang=%s";
    private final String apiKey;

    public WeatherDataApiDataSource(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public CurrentWeatherData getCurrentWeatherData(City city, String units, String lang) throws IOException {
        URL url = new URL(String.format(API_URL_PATTERN_CURRENT, city.getLat(), city.getLon(), apiKey, units, lang));
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            return new CurrentWeatherData(city, readJsonStreamWeatherData(new BufferedInputStream(urlConnection.getInputStream())));
        } finally {
            urlConnection.disconnect();
        }
    }

    @Override
    public FiveDaysWeatherData getFiveDaysWeatherData(City city, String units, String lang) throws IOException {
        URL url = new URL(String.format(API_URL_PATTERN_FIVE, city.getLat(), city.getLon(), apiKey, units, lang));
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            return new FiveDaysWeatherData(city, readJsonStreamWeatherDataArray(new BufferedInputStream(urlConnection.getInputStream())));
        } finally {
            urlConnection.disconnect();
        }
    }

    private WeatherData readJsonStreamWeatherData(InputStream in) throws IOException {
        try (JsonReader reader = new JsonReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            return readWeatherData(reader);
        }
    }

    private List<WeatherData> readJsonStreamWeatherDataArray(InputStream in) throws IOException {
        try (JsonReader reader = new JsonReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            return readWeatherDataMultiple(reader);
        }
    }

    private List<WeatherData> readWeatherDataMultiple(JsonReader reader) throws IOException {
        List<WeatherData> weatherDataArray = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String jsonName = reader.nextName();
            if (jsonName.equals("list")) {
                weatherDataArray = readWeatherDataArray(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return weatherDataArray;
    }

    private List<WeatherData> readWeatherDataArray(JsonReader reader) throws IOException {
        List<WeatherData> weatherDataArray = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            weatherDataArray.add(readWeatherData(reader));
        }
        reader.endArray();
        return weatherDataArray;
    }

    private WeatherData readWeatherData(JsonReader reader) throws IOException {
        List<Weather> weathers = null;
        String base = "Unknown";
        Main main = null;
        int visibility = -1;
        Wind wind = null;
        Rain rain = null;
        Snow snow = null;
        Clouds clouds = null;
        int dt = -1;
        Sys sys = null;
        String dt_txt = "";
        int timezone = -1;

        reader.beginObject();
        while (reader.hasNext()) {
            String jsonName = reader.nextName();
            switch (jsonName) {
                case "weather":
                    weathers = readWeathersArray(reader);
                    break;
                case "base":
                    base = reader.nextString();
                    break;
                case "main":
                    main = readMain(reader);
                    break;
                case "visibility":
                    visibility = reader.nextInt();
                    break;
                case "wind":
                    wind = readWind(reader);
                    break;
                case "rain":
                    rain = readRain(reader);
                    break;
                case "snow":
                    snow = readSnow(reader);
                    break;
                case "clouds":
                    clouds = readClouds(reader);
                    break;
                case "dt":
                    dt = reader.nextInt();
                    break;
                case "sys":
                    sys = readSys(reader);
                    break;
                case "dt_txt":
                    dt_txt = reader.nextString();
                    break;
                case "timezone":
                    timezone = reader.nextInt();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();

        return new WeatherDataBuilder()
                .setWeathers(weathers)
                .setBase(base)
                .setMain(main)
                .setVisibility(visibility)
                .setWind(wind)
                .setRain(rain)
                .setSnow(snow)
                .setClouds(clouds)
                .setDt(dt)
                .setSys(sys)
                .setDt_txt(dt_txt)
                .setTimezone(timezone)
                .build();
    }

    private List<Weather> readWeathersArray(JsonReader reader) throws IOException {
        List<Weather> weathers = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            weathers.add(readWeather(reader));
        }
        reader.endArray();
        return weathers;
    }

    private Weather readWeather(JsonReader reader) throws IOException {
        int id = -1;
        String main = "Unknown";
        String description = "Unknown";
        String icon = "";

        reader.beginObject();
        while (reader.hasNext()) {
            String jsonName = reader.nextName();
            switch (jsonName) {
                case "id":
                    id = reader.nextInt();
                    break;
                case "main":
                    main = reader.nextString();
                    break;
                case "description":
                    description = reader.nextString();
                    break;
                case "icon":
                    icon = reader.nextString();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return new Weather(id, main, description, icon);
    }

    private Main readMain(JsonReader reader) throws IOException {
        double temp = 0.0;
        double feelsLike = 0.0;
        double tempMin = 0.0;
        double tempMax = 0.0;
        int pressure = 0;
        int humidity = 0;
        int seaLevel = 0;
        int grndLevel = 0;

        reader.beginObject();
        while (reader.hasNext()) {
            String jsonName = reader.nextName();
            switch (jsonName) {
                case "temp":
                    temp = reader.nextDouble();
                    break;
                case "feels_like":
                    feelsLike = reader.nextDouble();
                    break;
                case "temp_min":
                    tempMin = reader.nextDouble();
                    break;
                case "temp_max":
                    tempMax = reader.nextDouble();
                    break;
                case "pressure":
                    pressure = reader.nextInt();
                    break;
                case "humidity":
                    humidity = reader.nextInt();
                    break;
                case "sea_level":
                    seaLevel = reader.nextInt();
                    break;
                case "grnd_level":
                    grndLevel = reader.nextInt();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return new Main(temp, feelsLike, tempMin, tempMax, pressure, humidity, seaLevel, grndLevel);
    }

    private Wind readWind(JsonReader reader) throws IOException {
        double speed = 0.0;
        double deg = 0.0;
        double gust = 0.0;

        reader.beginObject();
        while (reader.hasNext()) {
            String jsonName = reader.nextName();
            switch (jsonName) {
                case "speed":
                    speed = reader.nextDouble();
                    break;
                case "deg":
                    deg = reader.nextDouble();
                    break;
                case "gust":
                    gust = reader.nextDouble();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return new Wind(speed, deg, gust);
    }

    private Rain readRain(JsonReader reader) throws IOException {
        double rain1h = 0.0;
        double rain3h = 0.0;

        reader.beginObject();
        while (reader.hasNext()) {
            String jsonName = reader.nextName();
            if (jsonName.equals("rain1h")) {
                rain1h = reader.nextDouble();
            } else if (jsonName.equals("rain3h")) {
                rain3h = reader.nextDouble();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Rain(rain1h, rain3h);
    }

    private Snow readSnow(JsonReader reader) throws IOException {
        double snow1h = 0.0;
        double snow3h = 0.0;

        reader.beginObject();
        while (reader.hasNext()) {
            String jsonName = reader.nextName();
            if (jsonName.equals("snow1h")) {
                snow1h = reader.nextDouble();
            } else if (jsonName.equals("snow3h")) {
                snow3h = reader.nextDouble();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Snow(snow1h, snow3h);
    }

    private Clouds readClouds(JsonReader reader) throws IOException {
        int all = 0;

        reader.beginObject();
        while (reader.hasNext()) {
            String jsonName = reader.nextName();
            if (jsonName.equals("all")) {
                all = reader.nextInt();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Clouds(all);
    }

    private Sys readSys(JsonReader reader) throws IOException {
        int sunrise = 0;
        int sunset = 0;
        String pod = "";

        reader.beginObject();
        while (reader.hasNext()) {
            String jsonName = reader.nextName();
            switch (jsonName) {
                case "sunrise":
                    sunrise = reader.nextInt();
                    break;
                case "sunset":
                    sunset = reader.nextInt();
                    break;
                case "pod":
                    pod = reader.nextString();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return new Sys(sunrise, sunset, pod);
    }
}
