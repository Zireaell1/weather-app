package com.zireaell1.weatherapp.presentation;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zireaell1.weatherapp.R;
import com.zireaell1.weatherapp.domain.entities.FiveDaysWeatherData;

import java.util.concurrent.CompletableFuture;

public class FiveDaysWeatherFragment extends Fragment {
    private AppStateViewModel viewModel;

    public FiveDaysWeatherFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = ((MainActivity) requireActivity()).getViewModel();

        View view = inflater.inflate(R.layout.fragment_five_days_weather, container, false);

        CompletableFuture<FiveDaysWeatherData> futureFiveDaysWeatherData = viewModel.getGetFiveDaysWeatherData().execute(viewModel.getCurrentCity(), viewModel.getGetConfig().execute().getTemperatureUnit(), "en");
        futureFiveDaysWeatherData.exceptionally(e -> {
            Log.e("FiveDaysWeatherFragment", "Failed to get five days weather data", e);
            return null;
        });
        futureFiveDaysWeatherData.thenAccept(fiveDaysWeatherData -> getActivity().runOnUiThread(() -> {
            RecyclerView recyclerView = view.findViewById(R.id.five_days_weather_list);
            FiveDaysWeatherListAdapter adapter = new FiveDaysWeatherListAdapter(fiveDaysWeatherData.getWeatherDataList(), getContext(), viewModel.getGetConfig());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        }));

        return view;
    }
}
