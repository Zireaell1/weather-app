package com.zireaell1.weatherapp.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.zireaell1.weatherapp.R;

public class CityFragment extends Fragment {
    private AppStateViewModel viewModel;

    public CityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = ((MainActivity) requireActivity()).getViewModel();

        View view = inflater.inflate(R.layout.fragment_city, container, false);

        TextView cityName = view.findViewById(R.id.city_name);
        cityName.setText(viewModel.getCurrentCity().getName());
        TextView countryName = view.findViewById(R.id.country_name);
        countryName.setText(viewModel.getCurrentCity().getCountry());
        TextView coords = view.findViewById(R.id.coords);
        coords.setText(String.format("%s | %s", viewModel.getCurrentCity().getLat(), viewModel.getCurrentCity().getLon()));

        return view;
    }
}
