package com.zireaell1.weatherapp.presentation;

import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.zireaell1.weatherapp.R;

public class MainActivity extends AppCompatActivity {
    private AutoRefresh autoRefresh;
    private AppStateViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, new AppStateViewModelFactory(this)).get(AppStateViewModel.class);
        if (!this.getResources().getBoolean(R.bool.isTablet)) {
            Log.d("MainActivity", "Loading smartphone layout");
            setContentView(R.layout.activity_main);

            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);

            WeatherFragment weatherFragment = new WeatherFragment();
            autoRefresh = new AutoRefresh(weatherFragment, viewModel.getGetConfig(), viewModel);

            SettingsFragment settingsFragment = new SettingsFragment();
            settingsFragment.setAutoRefresh(autoRefresh);

            SavedCityListFragment savedCityListFragment = new SavedCityListFragment();
            savedCityListFragment.setWeatherFragment(weatherFragment);
            savedCityListFragment.setBottomNavigationView(bottomNavigationView);

            EditText searchEditText = findViewById(R.id.search_edit_text);
            searchEditText.setOnEditorActionListener((textView, i, keyEvent) -> {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    SearchCityFragment searchCityFragment = SearchCityFragment.newInstance(searchEditText.getText().toString());
                    searchCityFragment.setWeatherFragment(weatherFragment);
                    searchCityFragment.setBottomNavigationView(bottomNavigationView);
                    viewModel.setCurrentFragment("search");
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, searchCityFragment).commit();
                    return true;
                }
                return false;
            });

            bottomNavigationView.setOnItemSelectedListener(item -> {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();
                if (itemId == R.id.action_city_list) {
                    selectedFragment = savedCityListFragment;
                    viewModel.setCurrentFragment("list");
                } else if (itemId == R.id.action_weather) {
                    selectedFragment = weatherFragment;
                    viewModel.setCurrentFragment("weather");
                } else if (itemId == R.id.action_settings) {
                    selectedFragment = settingsFragment;
                    viewModel.setCurrentFragment("settings");
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, selectedFragment).commit();
                }

                return true;
            });

            switch (viewModel.getCurrentFragment()) {
                case "list":
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, savedCityListFragment).commit();
                    bottomNavigationView.setSelectedItemId(R.id.action_city_list);
                    break;
                case "weather":
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, weatherFragment).commit();
                    bottomNavigationView.setSelectedItemId(R.id.action_weather);
                    break;
                case "settings":
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, settingsFragment).commit();
                    bottomNavigationView.setSelectedItemId(R.id.action_settings);
                    break;
            }
        } else {
            Log.d("MainActivity", "Loading tablet layout");
            setContentView(R.layout.activity_main_tablet);

            WeatherFragment weatherFragment = new WeatherFragment();
            autoRefresh = new AutoRefresh(weatherFragment, viewModel.getGetConfig(), viewModel);

            SettingsFragment settingsFragment = new SettingsFragment();
            settingsFragment.setAutoRefresh(autoRefresh);

            SavedCityListFragment savedCityListFragment = new SavedCityListFragment();
            savedCityListFragment.setWeatherFragment(weatherFragment);

            EditText searchEditText = findViewById(R.id.search_edit_text);
            searchEditText.setOnEditorActionListener((textView, i, keyEvent) -> {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    SearchCityFragment searchCityFragment = SearchCityFragment.newInstance(searchEditText.getText().toString());
                    searchCityFragment.setWeatherFragment(weatherFragment);
                    searchCityFragment.setSavedCityListFragment(savedCityListFragment);
                    viewModel.setCurrentFragment("search");
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, searchCityFragment).commit();
                    return true;
                }
                return false;
            });

            getSupportFragmentManager().beginTransaction().replace(R.id.main_container, weatherFragment).commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.saved_cities_container, savedCityListFragment).commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.settings_container, settingsFragment).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        autoRefresh.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        autoRefresh.stop();
    }

    public AppStateViewModel getViewModel() {
        return viewModel;
    }
}
