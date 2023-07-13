package com.zireaell1.weatherapp.presentation;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.zireaell1.weatherapp.R;
import com.zireaell1.weatherapp.domain.entities.City;
import com.zireaell1.weatherapp.domain.exceptions.DataRetrieveException;

import java.util.ArrayList;
import java.util.List;

public class SavedCityListFragment extends Fragment {
    private WeatherFragment weatherFragment;
    private BottomNavigationView bottomNavigationView;
    private SavedCityListAdapter cityListAdapter;
    private AppStateViewModel viewModel;

    public SavedCityListFragment() {
    }

    public void setWeatherFragment(WeatherFragment weatherFragment) {
        this.weatherFragment = weatherFragment;
    }

    public void setBottomNavigationView(BottomNavigationView bottomNavigationView) {
        this.bottomNavigationView = bottomNavigationView;
    }

    public void refresh() {
        cityListAdapter.refresh();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = ((MainActivity) requireActivity()).getViewModel();

        View view = inflater.inflate(R.layout.fragment_saved_city_list, container, false);

        try {
            List<City> cities = viewModel.getGetSavedCities().execute();
            cityListAdapter = new SavedCityListAdapter(getContext(), R.layout.saved_city_list_item, cities, viewModel.getDeleteWeatherData(), viewModel.getGetSavedCities(), weatherFragment, bottomNavigationView, viewModel);
            ListView listView = view.findViewById(R.id.saved_city_list_fragment);
            listView.setAdapter(cityListAdapter);
            TextView emptyList = view.findViewById(R.id.empty_list);
            emptyList.setText("");
            if(cities.isEmpty()) {
                emptyList.setText(R.string.empty_city_list);
            }
        } catch (DataRetrieveException e) {
            Log.e("SavedCityFragment", "Failed to get saved cities", e);
            List<City> cities = new ArrayList<>();
            CityListAdapter cityListAdapter = new CityListAdapter(getContext(), R.layout.city_list_item, cities);
            ListView listView = view.findViewById(R.id.saved_city_list_fragment);
            listView.setAdapter(cityListAdapter);
        }

        return view;
    }
}
