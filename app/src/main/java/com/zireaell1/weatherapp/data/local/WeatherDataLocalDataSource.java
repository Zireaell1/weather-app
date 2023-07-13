package com.zireaell1.weatherapp.data.local;

import android.content.Context;
import android.util.JsonReader;
import android.util.JsonWriter;

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
import com.zireaell1.weatherapp.domain.repositories.WeatherDataLocalRepository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeatherDataLocalDataSource implements WeatherDataLocalRepository {
    private final Context context;

    public WeatherDataLocalDataSource(Context context) {
        this.context = context;
    }

    @Override
    public List<City> getSavedCities() throws IOException {
        String[] fileNameList = context.fileList();
        List<City> cityList = new ArrayList<>();
        for (String fileName : fileNameList) {
            if (fileName.startsWith("city")) {
                try (FileInputStream file = context.openFileInput(fileName)) {
                    cityList.add(readJsonStreamCity(file));
                }
            }
        }
        return cityList;
    }

    @Override
    public City getCityByName(String cityName, String countryName) throws IOException {
        String fileName = String.format("city-%s-%s.json", cityName, countryName);
        try (FileInputStream file = context.openFileInput(fileName)) {
            return readJsonStreamCity(file);
        }
    }

    @Override
    public CurrentWeatherData getCurrentWeatherData(City city) throws IOException {
        String fileName = String.format("current-%s-%s.json", city.getName(), city.getCountry());
        try (FileInputStream file = context.openFileInput(fileName)) {
            return new CurrentWeatherData(city, readJsonStreamWeatherData(file));
        }
    }

    @Override
    public FiveDaysWeatherData getFiveDaysWeatherData(City city) throws IOException {
        String fileName = String.format("five-%s-%s.json", city.getName(), city.getCountry());
        try (FileInputStream file = context.openFileInput(fileName)) {
            return new FiveDaysWeatherData(city, readJsonStreamWeatherDataArray(file));
        }
    }

    @Override
    public void saveCity(City city) throws IOException {
        String fileName = String.format("city-%s-%s.json", city.getName(), city.getCountry());
        try (FileOutputStream file = context.openFileOutput(fileName, Context.MODE_PRIVATE)) {
            writeJsonStreamCity(file, city);
        }
    }

    @Override
    public void saveCurrentWeatherData(CurrentWeatherData currentWeatherData) throws IOException {
        String fileName = String.format("current-%s-%s.json", currentWeatherData.getCity().getName(), currentWeatherData.getCity().getCountry());
        try (FileOutputStream file = context.openFileOutput(fileName, Context.MODE_PRIVATE)) {
            writeJsonStreamWeatherData(file, currentWeatherData.getWeatherData());
        }
    }

    @Override
    public void saveFiveDaysWeatherData(FiveDaysWeatherData fiveDaysWeatherData) throws IOException {
        String fileName = String.format("five-%s-%s.json", fiveDaysWeatherData.getCity().getName(), fiveDaysWeatherData.getCity().getCountry());
        try (FileOutputStream file = context.openFileOutput(fileName, Context.MODE_PRIVATE)) {
            writeJsonStreamWeatherDataArray(file, fiveDaysWeatherData.getWeatherDataList());
        }
    }

    @Override
    public void deleteCity(City city) throws IOException {
        String fileName = String.format("city-%s-%s.json", city.getName(), city.getCountry());
        if (!context.deleteFile(fileName)) {
            throw new IOException();
        }
    }

    @Override
    public void deleteCurrentWeatherData(City city) throws IOException {
        String fileName = String.format("current-%s-%s.json", city.getName(), city.getCountry());
        if (!context.deleteFile(fileName)) {
            throw new IOException();
        }
    }

    @Override
    public void deleteFiveDaysWeatherData(City city) throws IOException {
        String fileName = String.format("five-%s-%s.json", city.getName(), city.getCountry());
        if (!context.deleteFile(fileName)) {
            throw new IOException();
        }
    }

    private void writeJsonStreamCity(OutputStream out, City city) throws IOException {
        JsonWriter writer = new JsonWriter(new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8)));
        writer.setIndent("  ");
        writeCity(writer, city);
        writer.close();
    }

    private void writeCity(JsonWriter writer, City city) throws IOException {
        writer.beginObject();
        writer.name("name").value(city.getName());
        if (city.getLocal_names() != null) {
            writer.name("local_names");
            writeLocalNames(writer, city.getLocal_names());
        }
        writer.name("lat").value(city.getLat());
        writer.name("lon").value(city.getLon());
        writer.name("country").value(city.getCountry());
        writer.name("state").value(city.getState());
        writer.endObject();
    }

    private void writeLocalNames(JsonWriter writer, Map<String, String> localNames) throws IOException {
        writer.beginObject();
        for (Map.Entry<String, String> entry : localNames.entrySet()) {
            writer.name(entry.getKey()).value(entry.getValue());
        }
        writer.endObject();
    }

    private void writeJsonStreamWeatherData(OutputStream out, WeatherData weatherData) throws IOException {
        JsonWriter writer = new JsonWriter(new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8)));
        writer.setIndent("  ");
        writeWeatherData(writer, weatherData);
        writer.close();
    }

    private void writeJsonStreamWeatherDataArray(OutputStream out, List<WeatherData> weatherDataArray) throws IOException {
        JsonWriter writer = new JsonWriter(new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8)));
        writer.setIndent("  ");
        writer.beginArray();
        for (WeatherData weatherData : weatherDataArray) {
            writeWeatherData(writer, weatherData);
        }
        writer.endArray();
        writer.close();
    }

    private void writeWeatherData(JsonWriter writer, WeatherData weatherData) throws IOException {
        writer.beginObject();
        writer.name("weather");
        writeWeathersArray(writer, weatherData.getWeathers());
        writer.name("base").value(weatherData.getBase());
        writer.name("main");
        writeMain(writer, weatherData.getMain());
        writer.name("visibility").value(weatherData.getVisibility());
        if (weatherData.getWind() != null) {
            writer.name("wind");
            writeWind(writer, weatherData.getWind());
        }
        if (weatherData.getRain() != null) {
            writer.name("rain");
            writeRain(writer, weatherData.getRain());
        }
        if (weatherData.getSnow() != null) {
            writer.name("snow");
            writeSnow(writer, weatherData.getSnow());
        }
        writer.name("clouds");
        writeClouds(writer, weatherData.getClouds());
        writer.name("dt").value(weatherData.getDt());
        writer.name("sys");
        writeSys(writer, weatherData.getSys());
        writer.name("dt_txt").value(weatherData.getDt_txt());
        writer.name("timezone").value(weatherData.getTimezone());
        writer.endObject();
    }

    private void writeWeathersArray(JsonWriter writer, List<Weather> weathers) throws IOException {
        writer.beginArray();
        for (Weather weather : weathers) {
            writeWeather(writer, weather);
        }
        writer.endArray();
    }

    private void writeWeather(JsonWriter writer, Weather weather) throws IOException {
        writer.beginObject();
        writer.name("id").value(weather.getId());
        writer.name("main").value(weather.getMain());
        writer.name("description").value(weather.getDescription());
        writer.name("icon").value(weather.getIcon());
        writer.endObject();
    }

    private void writeMain(JsonWriter writer, Main main) throws IOException {
        writer.beginObject();
        writer.name("temp").value(main.getTemp());
        writer.name("feels_like").value(main.getFeelsLike());
        writer.name("temp_min").value(main.getTempMin());
        writer.name("temp_max").value(main.getTempMax());
        writer.name("pressure").value(main.getPressure());
        writer.name("humidity").value(main.getHumidity());
        writer.name("sea_level").value(main.getSeaLevel());
        writer.name("grnd_level").value(main.getGrndLevel());
        writer.endObject();
    }

    private void writeWind(JsonWriter writer, Wind wind) throws IOException {
        writer.beginObject();
        writer.name("speed").value(wind.getSpeed());
        writer.name("deg").value(wind.getDeg());
        writer.name("gust").value(wind.getGust());
        writer.endObject();
    }

    private void writeRain(JsonWriter writer, Rain rain) throws IOException {
        writer.beginObject();
        writer.name("rain1h").value(rain.getRain1h());
        writer.name("rain3h").value(rain.getRain3h());
        writer.endObject();
    }

    private void writeSnow(JsonWriter writer, Snow snow) throws IOException {
        writer.beginObject();
        writer.name("snow1h").value(snow.getSnow1h());
        writer.name("snow3h").value(snow.getSnow3h());
        writer.endObject();
    }

    private void writeClouds(JsonWriter writer, Clouds clouds) throws IOException {
        writer.beginObject();
        writer.name("all").value(clouds.getAll());
        writer.endObject();
    }

    private void writeSys(JsonWriter writer, Sys sys) throws IOException {
        writer.beginObject();
        writer.name("sunrise").value(sys.getSunrise());
        writer.name("sunset").value(sys.getSunset());
        writer.name("pod").value(sys.getPod());
        writer.endObject();
    }

    private City readJsonStreamCity(InputStream in) throws IOException {
        try (JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)))) {
            return readCity(reader);
        }
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

    private WeatherData readJsonStreamWeatherData(InputStream in) throws IOException {
        try (JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)))) {
            return readWeatherData(reader);
        }
    }

    private List<WeatherData> readJsonStreamWeatherDataArray(InputStream in) throws IOException {
        try (JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)))) {
            return readWeatherDataArray(reader);
        }
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
