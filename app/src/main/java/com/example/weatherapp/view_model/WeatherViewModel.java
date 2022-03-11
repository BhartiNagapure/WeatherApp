package com.example.weatherapp.view_model;

import static com.example.weatherapp.constants.AppConstant.API_KEY;
import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.weatherapp.model.LocationModel;
import com.example.weatherapp.repository.LocationRepository;
import com.example.weatherapp.repository.WeatherRepository;
import com.example.weatherapp.response.WeatherResponse;

public class WeatherViewModel extends AndroidViewModel {

    private WeatherRepository weatherRepository;
    private LiveData<WeatherResponse> weatherResponseLiveData;

    private LocationRepository locationRepository;
    private LiveData<LocationModel> locationModelLiveData;

    public WeatherViewModel(@NonNull Application application) {
        super(application);

        locationRepository = new LocationRepository(application);
        this.locationModelLiveData = locationRepository.getLocation(application.getApplicationContext());

        LocationModel model = locationModelLiveData.getValue();
        if (model != null) {
            weatherRepository = new WeatherRepository();
            this.weatherResponseLiveData = weatherRepository.getWeatherData(model.getLatitude(),model.getLongitude(), API_KEY);
        }
    }

    public LiveData<WeatherResponse> getWeatherResponseLiveData() {
        return weatherResponseLiveData;
    }

    public LiveData<LocationModel> getLocationLiveData() {
        return locationModelLiveData;
    }


}
