package com.zireaell1.weatherapp.presentation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.zireaell1.weatherapp.R;
import com.zireaell1.weatherapp.domain.entities.City;
import com.zireaell1.weatherapp.domain.exceptions.DataDeleteException;
import com.zireaell1.weatherapp.domain.exceptions.DataRetrieveException;
import com.zireaell1.weatherapp.domain.usecases.interfaces.DeleteWeatherData;
import com.zireaell1.weatherapp.domain.usecases.interfaces.GetSavedCities;

import java.util.List;

public class SavedCityListAdapter extends ArrayAdapter<City> {
    private final @LayoutRes int resource;
    private final List<City> items;
    private final DeleteWeatherData deleteWeatherData;
    private final GetSavedCities getSavedCities;
    private final WeatherFragment weatherFragment;
    private final BottomNavigationView bottomNavigationView;
    private final AppStateViewModel viewModel;

    public SavedCityListAdapter(Context context, @LayoutRes int resource, List<City> items, DeleteWeatherData deleteWeatherData, GetSavedCities getSavedCities, WeatherFragment weatherFragment, BottomNavigationView bottomNavigationView, AppStateViewModel viewModel) {
        super(context, resource, items);
        this.resource = resource;
        this.items = items;
        this.deleteWeatherData = deleteWeatherData;
        this.getSavedCities = getSavedCities;
        this.weatherFragment = weatherFragment;
        this.bottomNavigationView = bottomNavigationView;
        this.viewModel = viewModel;
    }

    public void refresh() {
        try {
            items.clear();
            items.addAll(getSavedCities.execute());
            notifyDataSetChanged();
        } catch (DataRetrieveException e) {

        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        }
        City city = getItem(position);

        TextView cityTextView = convertView.findViewById(R.id.city_text_view);
        cityTextView.setText(city.getName());
        TextView countryTextView = convertView.findViewById(R.id.country_text_view);
        countryTextView.setText(city.getCountry());

        ImageButton selectCityButton = convertView.findViewById(R.id.select_city_button);
        ImageButton removeCityButton = convertView.findViewById(R.id.remove_city_button);

        selectCityButton.setOnClickListener(v -> {
            viewModel.setCurrentCity(city);
            ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.main_container, weatherFragment).commit();
            weatherFragment.autoRefresh(viewModel);
            if (bottomNavigationView != null) {
                bottomNavigationView.setSelectedItemId(R.id.action_weather);
            }
        });

        removeCityButton.setOnClickListener(v -> {
            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.confirm_remove_city_dialog_title)
                    .setMessage(R.string.confirm_remove_city_dialog_message)
                    .setPositiveButton(R.string.confirm_remove_city_dialog_positive, (dialogInterface, i) -> {
                        try {
                            deleteWeatherData.execute(city);
                            items.clear();
                            items.addAll(getSavedCities.execute());
                            notifyDataSetChanged();
                        } catch (DataDeleteException e) {
                            Log.d("SavedCityListFragment", "Weather data files not found");
                            try {
                                items.clear();
                                items.addAll(getSavedCities.execute());
                                notifyDataSetChanged();
                            } catch (DataRetrieveException ex) {
                                Log.e("SavedCityListFragment", "Unable to get current city list", e);
                            }
                        } catch (DataRetrieveException e) {
                            Log.e("SavedCityListFragment", "Unable to get current city list", e);
                        }
                    })
                    .setNegativeButton(R.string.confirm_remove_city_dialog_negative, null)
                    .show();
        });

        return convertView;
    }
}
