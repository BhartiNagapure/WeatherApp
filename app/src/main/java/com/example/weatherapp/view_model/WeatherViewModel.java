package com.example.weatherapp.view_model;

import static com.example.weatherapp.constants.AppConstant.API_KEY;
import static com.example.weatherapp.constants.AppConstant.LATITUDE;
import static com.example.weatherapp.constants.AppConstant.LONGITUDE;
import static com.example.weatherapp.constants.AppConstant.PREFERENCES;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.weatherapp.repository.WeatherRepository;
import com.example.weatherapp.response.WeatherResponse;


public class WeatherViewModel extends AndroidViewModel {

    private WeatherRepository weatherRepository;
    private LiveData<WeatherResponse> weatherResponseLiveData;

    public WeatherViewModel(@NonNull Application application) {
        super(application);

        SharedPreferences preferences = application.getApplicationContext().getSharedPreferences(PREFERENCES, 0);
        double latitude = Double.parseDouble(preferences.getString(LATITUDE, "0.0"));
        double longitude = Double.parseDouble(preferences.getString(LONGITUDE, "0.0"));

        weatherRepository = new WeatherRepository();
        this.weatherResponseLiveData = weatherRepository.getWeatherData(latitude,longitude, API_KEY);
    }

    public LiveData<WeatherResponse> getWeatherResponseLiveData() {
        return weatherResponseLiveData;
    }
}
