package com.zireaell1.weatherapp.presentation;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.zireaell1.weatherapp.R;
import com.zireaell1.weatherapp.domain.entities.City;
import com.zireaell1.weatherapp.domain.entities.CurrentWeatherData;
import com.zireaell1.weatherapp.domain.entities.FiveDaysWeatherData;
import com.zireaell1.weatherapp.domain.exceptions.DataRetrieveException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class WeatherFragment extends Fragment {
    private CityFragment cityFragment;
    private CurrentWeatherFragment currentWeatherFragment;
    private FiveDaysWeatherFragment fiveDaysWeatherFragment;
    private AppStateViewModel viewModel;
    private View view;

    public WeatherFragment() {
    }

    public void refresh(City city, TextView refreshStatus, TextView internetStatus) {
        CompletableFuture<CurrentWeatherData> futureCurrentWeatherData = viewModel.getUpdateCurrentWeatherData().execute(city, viewModel.getGetConfig().execute().getTemperatureUnit(), "en");
        futureCurrentWeatherData.exceptionally(e -> {
            internetStatus.setText(R.string.internet_status);
            return null;
        });
        futureCurrentWeatherData.thenAccept(currentWeatherData -> getActivity().runOnUiThread(() -> {
            currentWeatherFragment = new CurrentWeatherFragment();
            viewModel.setCurrentCity(city);

            city.updateDate();
            viewModel.getConfigRepository().saveCityUpdateDate(city);
            DateFormat format = new SimpleDateFormat("HH:mm:ss - dd.MM.yyyy", Locale.ENGLISH);
            refreshStatus.setText(format.format(viewModel.getConfigRepository().getCityUpdateDate(city)));
            internetStatus.setText("");

            getChildFragmentManager().beginTransaction().replace(R.id.current_weather_fragment, currentWeatherFragment).commit();
        }));

        CompletableFuture<FiveDaysWeatherData> futureFiveDaysWeatherData = viewModel.getUpdateFiveDaysWeatherData().execute(city, viewModel.getGetConfig().execute().getTemperatureUnit(), "en");
        futureFiveDaysWeatherData.exceptionally(e -> {
            internetStatus.setText(R.string.internet_status);
            return null;
        });
        futureFiveDaysWeatherData.thenAccept(fiveDaysWeatherData -> getActivity().runOnUiThread(() -> {
            fiveDaysWeatherFragment = new FiveDaysWeatherFragment();
            viewModel.setCurrentCity(city);

            city.updateDate();
            viewModel.getConfigRepository().saveCityUpdateDate(city);
            DateFormat format = new SimpleDateFormat("HH:mm:ss - dd.MM.yyyy", Locale.ENGLISH);
            refreshStatus.setText(format.format(viewModel.getConfigRepository().getCityUpdateDate(city)));
            internetStatus.setText("");

            getChildFragmentManager().beginTransaction().replace(R.id.five_days_weather_fragment, fiveDaysWeatherFragment).commit();
        }));
    }

    public void autoRefresh(AppStateViewModel viewModel) {
        try {
            List<City> cities = viewModel.getGetSavedCities().execute();
            if (cities.size() != 0) {
                City city = cities.get(0);
                if (viewModel.getCurrentCity() != null) {
                    city = viewModel.getCurrentCity();
                }

                if (view.getResources().getBoolean(R.bool.isTablet)) {
                    cityFragment = new CityFragment();
                    getChildFragmentManager().beginTransaction().add(R.id.city_fragment, cityFragment).commit();
                }

                CompletableFuture<CurrentWeatherData> futureCurrentWeatherData = viewModel.getUpdateCurrentWeatherData().execute(city, viewModel.getGetConfig().execute().getTemperatureUnit(), "en");
                futureCurrentWeatherData.exceptionally(e -> {
                    TextView internetStatus = view.findViewById(R.id.internet_status);
                    TextView refreshStatus = view.findViewById(R.id.refresh_status);
                    internetStatus.setText(R.string.internet_status);
                    return null;
                });
                City finalCity = city;
                futureCurrentWeatherData.thenAccept(currentWeatherData -> getActivity().runOnUiThread(() -> {
                    currentWeatherFragment = new CurrentWeatherFragment();
                    viewModel.setCurrentCity(finalCity);

                    finalCity.updateDate();
                    viewModel.getConfigRepository().saveCityUpdateDate(finalCity);
                    TextView refreshStatus = view.findViewById(R.id.refresh_status);
                    DateFormat format = new SimpleDateFormat("HH:mm:ss - dd.MM.yyyy", Locale.ENGLISH);
                    refreshStatus.setText(format.format(viewModel.getConfigRepository().getCityUpdateDate(finalCity)));
                    TextView internetStatus = view.findViewById(R.id.internet_status);
                    internetStatus.setText("");

                    getChildFragmentManager().beginTransaction().replace(R.id.current_weather_fragment, currentWeatherFragment).commit();
                }));

                CompletableFuture<FiveDaysWeatherData> futureFiveDaysWeatherData = viewModel.getUpdateFiveDaysWeatherData().execute(city, viewModel.getGetConfig().execute().getTemperatureUnit(), "en");
                futureFiveDaysWeatherData.exceptionally(e -> {
                    TextView internetStatus = view.findViewById(R.id.internet_status);
                    TextView refreshStatus = view.findViewById(R.id.refresh_status);
                    internetStatus.setText(R.string.internet_status);
                    return null;
                });

                futureFiveDaysWeatherData.thenAccept(fiveDaysWeatherData -> getActivity().runOnUiThread(() -> {
                    fiveDaysWeatherFragment = new FiveDaysWeatherFragment();
                    viewModel.setCurrentCity(finalCity);

                    finalCity.updateDate();
                    viewModel.getConfigRepository().saveCityUpdateDate(finalCity);
                    TextView refreshStatus = view.findViewById(R.id.refresh_status);
                    DateFormat format = new SimpleDateFormat("HH:mm:ss - dd.MM.yyyy", Locale.ENGLISH);
                    refreshStatus.setText(format.format(viewModel.getConfigRepository().getCityUpdateDate(finalCity)));
                    TextView internetStatus = view.findViewById(R.id.internet_status);
                    internetStatus.setText("");

                    getChildFragmentManager().beginTransaction().replace(R.id.five_days_weather_fragment, fiveDaysWeatherFragment).commit();
                }));
            }
        } catch (DataRetrieveException e) {
            Log.e("WeatherFragment", "Failed to auto refresh data", e);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cityFragment = new CityFragment();
        currentWeatherFragment = new CurrentWeatherFragment();
        fiveDaysWeatherFragment = new FiveDaysWeatherFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = ((MainActivity) requireActivity()).getViewModel();

        view = inflater.inflate(R.layout.fragment_weather, container, false);
        try {
            List<City> cities = viewModel.getGetSavedCities().execute();
            if (cities.size() == 0) {
                TextView refreshStatus = view.findViewById(R.id.refresh_status);
                refreshStatus.setText(R.string.empty_city_list);
                ImageButton refreshButton = view.findViewById(R.id.refresh_button);
                refreshButton.setVisibility(View.INVISIBLE);
                Log.d("WeatherFragment", "Saved city list empty");
            } else {
                City city = cities.get(0);
                if (viewModel.getCurrentCity() != null) {
                    city = viewModel.getCurrentCity();
                }
                TextView internetStatus = view.findViewById(R.id.internet_status);
                internetStatus.setText("");
                TextView refreshStatus = view.findViewById(R.id.refresh_status);
                DateFormat format = new SimpleDateFormat("HH:mm:ss - dd.MM.yyyy", Locale.ENGLISH);
                refreshStatus.setText(format.format(viewModel.getConfigRepository().getCityUpdateDate(city)));

                ImageButton refreshButton = view.findViewById(R.id.refresh_button);
                City finalCity = city;
                refreshButton.setOnClickListener(v -> {
                    Log.d("WeatherFragment", "Attempt to refresh weather data");
                    refresh(finalCity, refreshStatus, internetStatus);
                });

                viewModel.setCurrentCity(city);
                getChildFragmentManager().beginTransaction().add(R.id.city_fragment, cityFragment).commit();
                getChildFragmentManager().beginTransaction().add(R.id.current_weather_fragment, currentWeatherFragment).commit();
                getChildFragmentManager().beginTransaction().add(R.id.five_days_weather_fragment, fiveDaysWeatherFragment).commit();
            }
        } catch (DataRetrieveException e) {
            Log.e("WeatherFragment", "Failed to get saved cities", e);
            TextView refreshStatus = view.findViewById(R.id.refresh_status);
            refreshStatus.setText(R.string.refresh_status_failed);
        }

        return view;
    }
}
