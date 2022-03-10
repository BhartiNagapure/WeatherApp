package com.example.weatherapp.retrofit;

import com.example.weatherapp.response.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiRequest {

    @GET("/data/2.5/onecall")
    Call<WeatherResponse> getWeatherData(@Query("lat") double lat,
                                         @Query("lon") double lon,
                                         @Query("exclude") String exclude,
                                         @Query("appid") String key);
}
