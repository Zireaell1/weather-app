package com.zireaell1.weatherapp.presentation;

import android.util.Log;

import com.zireaell1.weatherapp.domain.usecases.interfaces.GetConfig;

import java.util.Timer;
import java.util.TimerTask;

public class AutoRefresh {
    private static final long TIMER_MULTIPLIER = 1000;
    private final WeatherFragment weatherFragment;
    private final GetConfig getConfig;
    private final AppStateViewModel viewModel;
    private Timer autoRefreshTimer;

    public AutoRefresh(WeatherFragment weatherFragment, GetConfig getConfig, AppStateViewModel viewModel) {
        this.weatherFragment = weatherFragment;
        this.getConfig = getConfig;
        this.viewModel = viewModel;
    }

    public void start() {
        autoRefreshTimer = new Timer();
        autoRefreshTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d("AutoRefresh", "Auto data refresh");
                weatherFragment.autoRefresh(viewModel);
            }
        }, 0, getConfig.execute().getRefreshTime() * TIMER_MULTIPLIER);
        Log.d("AutoRefresh", "Timer started");
    }

    public void stop() {
        autoRefreshTimer.cancel();
        Log.d("AutoRefresh", "Timer stopped");
    }

    public void restart() {
        stop();
        start();
        Log.d("AutoRefresh", String.format("Timer restarted with refresh time %d", getConfig.execute().getRefreshTime()));
    }

    public void forceRefresh() {
        weatherFragment.autoRefresh(viewModel);
        Log.d("AutoRefresh", "Forced refresh");
    }
}
