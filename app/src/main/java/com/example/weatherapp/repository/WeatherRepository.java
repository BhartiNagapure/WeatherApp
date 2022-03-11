package com.example.weatherapp.repository;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.weatherapp.response.WeatherResponse;
import com.example.weatherapp.retrofit.ApiRequest;
import com.example.weatherapp.retrofit.RetrofitRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherRepository {
    private static final String TAG = WeatherRepository.class.getSimpleName();
    private ApiRequest apiRequest;

    public WeatherRepository() {
        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);

    }

    public LiveData<WeatherResponse> getWeatherData(double latitude, double longitude, String key) {
        final MutableLiveData<WeatherResponse> data = new MutableLiveData<>();

        apiRequest.getWeatherData(latitude,longitude, "hourly,daily",key)
                .enqueue(new Callback<WeatherResponse>() {
                    @Override
                    public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                        Log.d(TAG, "onResponse response:: " + response);

                        if (response.body() != null) {
                            data.setValue(response.body());

                            Log.d(TAG, "Latitude:: " + response.body().getLon());
                            Log.d(TAG, "Longitude:: " + response.body().getLat());
                            Log.d(TAG, "TimeZone:: " + response.body().getTimezone());
                        }
                    }
                    @Override
                    public void onFailure(Call<WeatherResponse> call, Throwable t) {
                        data.setValue(null);
                    }
                });
        return data;
    }

}
