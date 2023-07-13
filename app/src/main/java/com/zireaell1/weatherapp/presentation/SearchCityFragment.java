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
import com.zireaell1.weatherapp.domain.exceptions.AddCityException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SearchCityFragment extends Fragment {

    private static final String ARG_CITY_NAME = "cityName";

    private String mCityName;
    private WeatherFragment weatherFragment;
    private BottomNavigationView bottomNavigationView;
    private SavedCityListFragment savedCityListFragment;
    private AppStateViewModel viewModel;

    public SearchCityFragment() {
    }

    public static SearchCityFragment newInstance(String cityName) {
        SearchCityFragment fragment = new SearchCityFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CITY_NAME, cityName);
        fragment.setArguments(args);
        return fragment;
    }

    public void setWeatherFragment(WeatherFragment weatherFragment) {
        this.weatherFragment = weatherFragment;
    }

    public void setBottomNavigationView(BottomNavigationView bottomNavigationView) {
        this.bottomNavigationView = bottomNavigationView;
    }

    public void setSavedCityListFragment(SavedCityListFragment savedCityListFragment) {
        this.savedCityListFragment = savedCityListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCityName = getArguments().getString(ARG_CITY_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = ((MainActivity) requireActivity()).getViewModel();

        View view = inflater.inflate(R.layout.fragment_search_city, container, false);
        TextView internetStatus = view.findViewById(R.id.internet_status);
        internetStatus.setText("");

        CompletableFuture<List<City>> futureCities = viewModel.getGetCitiesByName().execute(mCityName);
        futureCities.exceptionally(e -> {
            Log.e("SearchCityFragment", "Failed to get city list", e);
            internetStatus.setText(R.string.internet_status);
            return new ArrayList<>();
        });
        futureCities.thenAccept(cities -> getActivity().runOnUiThread(() -> {
            CityListAdapter cityListAdapter = new CityListAdapter(getContext(), R.layout.city_list_item, cities);
            ListView listView = view.findViewById(R.id.search_city_fragment);
            listView.setAdapter(cityListAdapter);

            listView.setOnItemClickListener((parent, view1, position, id) -> {
                try {
                    City city = (City) listView.getItemAtPosition(position);
                    viewModel.getAddCity().execute(city);
                    viewModel.setCurrentCity(city);
                    getParentFragmentManager().beginTransaction().replace(R.id.main_container, weatherFragment).commit();
                    if (bottomNavigationView != null) {
                        bottomNavigationView.setSelectedItemId(R.id.action_weather);
                    } else {
                        Log.d("SearchCityFragment", "Recreate saved city list fragment");
                        savedCityListFragment.refresh();
                    }
                } catch (AddCityException e) {
                    Log.e("SearchCityFragment", "Failed to add city", e);
                }
            });
        }));

        return view;
    }
}
