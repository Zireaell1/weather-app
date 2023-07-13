package com.zireaell1.weatherapp.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.zireaell1.weatherapp.R;
import com.zireaell1.weatherapp.domain.entities.Config;

import java.util.Arrays;
import java.util.List;

public class SettingsFragment extends Fragment {
    private AutoRefresh autoRefresh;
    private AppStateViewModel viewModel;

    public SettingsFragment() {
    }

    public void setAutoRefresh(AutoRefresh autoRefresh) {
        this.autoRefresh = autoRefresh;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = ((MainActivity) requireActivity()).getViewModel();

        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        if(view.getResources().getBoolean(R.bool.isTablet)) {
            view = inflater.inflate(R.layout.fragment_settings_tablet, container, false);
        }
        Config config = viewModel.getGetConfig().execute();

        Spinner refreshTimeDropdown = view.findViewById(R.id.refresh_time_dropdown);
        List<Integer> refreshTimeValues = Arrays.asList(10, 60, 300);
        ArrayAdapter<Integer> refreshTimeAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, refreshTimeValues);
        refreshTimeDropdown.setAdapter(refreshTimeAdapter);
        refreshTimeDropdown.setSelection(refreshTimeValues.indexOf(config.getRefreshTime()));

        Spinner temperatureUnitDropdown = view.findViewById(R.id.temperature_unit_dropdown);
        List<String> temperatureUnitValues = Arrays.asList("standard", "metric", "imperial");
        ArrayAdapter<String> temperatureUnitAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, temperatureUnitValues);
        temperatureUnitDropdown.setAdapter(temperatureUnitAdapter);
        temperatureUnitDropdown.setSelection(temperatureUnitValues.indexOf(config.getTemperatureUnit()));

        refreshTimeDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int selected = (int) parent.getItemAtPosition(position);
                Config newConfig = new Config(selected, (String) temperatureUnitDropdown.getItemAtPosition(temperatureUnitValues.indexOf(config.getTemperatureUnit())));
                if (!viewModel.getGetConfig().execute().equals(newConfig)) {
                    viewModel.getSaveConfig().execute(newConfig);
                    autoRefresh.forceRefresh();
                    autoRefresh.restart();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        temperatureUnitDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String) parent.getItemAtPosition(position);
                Config newConfig = new Config((int) refreshTimeDropdown.getItemAtPosition(refreshTimeValues.indexOf(config.getRefreshTime())), selected);
                if (!viewModel.getGetConfig().execute().equals(newConfig)) {
                    viewModel.getSaveConfig().execute(newConfig);
                    autoRefresh.forceRefresh();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }
}
