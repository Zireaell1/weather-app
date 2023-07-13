package com.zireaell1.weatherapp.presentation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zireaell1.weatherapp.R;
import com.zireaell1.weatherapp.domain.entities.City;

import java.util.List;

public class CityListAdapter extends ArrayAdapter<City> {
    private final @LayoutRes int resource;

    public CityListAdapter(Context context, @LayoutRes int resource, List<City> items) {
        super(context, resource, items);
        this.resource = resource;
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

        return convertView;
    }
}
