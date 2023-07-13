package com.zireaell1.weatherapp.presentation;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class AppStateViewModelFactory implements ViewModelProvider.Factory {
    private final Context mParam;


    public AppStateViewModelFactory(Context param) {
        mParam = param;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AppStateViewModel(mParam);
    }
}
