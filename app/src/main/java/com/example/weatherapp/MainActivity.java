package com.example.weatherapp;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.example.weatherapp.constants.AppConstant.PREFERENCES;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.weatherapp.response.WeatherResponse;
import com.example.weatherapp.utils.CheckNetwork;
import com.example.weatherapp.view_model.WeatherViewModel;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    WeatherViewModel weatherViewModel;

    TextView txt_address, txt_updated_at, txt_status, txt_temp, txt_sunrise,
            txt_sunset, txt_wind, txt_pressure, txt_humidity;
    SwipeRefreshLayout swipeRefreshLayout;

    SharedPreferences sharedpreferences;
    LocationManager locationManager;

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAndRequestPermissions();
        }

        initialization();

    }

    private void initialization() {

        txt_address = findViewById(R.id.address);
        txt_updated_at = findViewById(R.id.updated_at);
        txt_status = findViewById(R.id.status);
        txt_temp = findViewById(R.id.temp);
        txt_sunrise = findViewById(R.id.sunrise);
        txt_sunset = findViewById(R.id.sunset);
        txt_wind = findViewById(R.id.wind);
        txt_pressure = findViewById(R.id.pressure);
        txt_humidity = findViewById(R.id.humidity);
        swipeRefreshLayout = findViewById(R.id.refresh_layout);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        swipeRefreshLayout.setOnRefreshListener(this);

        sharedpreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);

        weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);

    }

    /**
     * get weather data from news api
     *
     * @param @null
     */
    private void getWeatherData() {

        swipeRefreshLayout.setRefreshing(true);
        if(CheckNetwork.isNetworkAvailable(this)) {
            weatherViewModel.getWeatherResponseLiveData().observe(this, weatherResponse -> {
                if (weatherResponse != null) {
                    Log.d(TAG, ": " + weatherResponse);
                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    if (weatherResponse.getLat() != 0.0) {
                        String address = getAddress(weatherResponse.getLat(), weatherResponse.getLon());
                        updateView(weatherResponse, address);
                    }
                }
            });
        }else{
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getLocationData() {
        weatherViewModel.getLocationLiveData().observe(this, location -> {
                if (location != null) {
                    Log.d(TAG, ": " + location);

                    getWeatherData();
                }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            getLocationData();
        }


    }

    @Override
    public void onRefresh() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAndRequestPermissions();
        }
    }

    /* Get Address from LatLong */
    private String getAddress(double latitude, double longitude) {

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        String address = "";
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();

            address = city + "," +state+ "," + country;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;

    }

    /* Update UI as per the response */
    private void updateView(WeatherResponse weatherResponse, String address) {

        if (weatherResponse != null) {

            long updatedAt = weatherResponse.getCurrentData().getDate();
            String updatedAtText = "Updated at: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));
            double temp = weatherResponse.getCurrentData().getTemp();
            String pressure = weatherResponse.getCurrentData().getPressure();
            String humidity = weatherResponse.getCurrentData().getHumidity();

            long sunrise = weatherResponse.getCurrentData().getSunrise();
            long sunset = weatherResponse.getCurrentData().getSunset();
            String windSpeed = weatherResponse.getCurrentData().getSpeed();

            String weatherDescription = weatherResponse.getCurrentData().getWeatherData().get(0).getDescription();

            /* Populating extracted data into our views */
            txt_address.setText(address);
            txt_updated_at.setText(updatedAtText);
            if (weatherDescription != null) {
                txt_status.setText(weatherDescription.toUpperCase());
            }

            DecimalFormat df = new DecimalFormat("#.##");
            String t = df.format(KtoC(temp)) + "°C";
            txt_temp.setText(t);
            txt_sunrise.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunrise * 1000)));
            txt_sunset.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunset * 1000)));
            txt_wind.setText(windSpeed);
            txt_pressure.setText(pressure);
            txt_humidity.setText(humidity);

            /* Views populated, Hiding the loader, Showing the main design */
            findViewById(R.id.loader).setVisibility(View.GONE);
            findViewById(R.id.mainContainer).setVisibility(View.VISIBLE);
            findViewById(R.id.errorText).setVisibility(View.GONE);
        } else {
            findViewById(R.id.loader).setVisibility(View.GONE);
            findViewById(R.id.errorText).setVisibility(View.VISIBLE);
            findViewById(R.id.mainContainer).setVisibility(View.GONE);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkAndRequestPermissions() {
        int fine_location = ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION);
        int course_location = ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (fine_location != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(ACCESS_FINE_LOCATION);
        }
        if (course_location != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(ACCESS_COARSE_LOCATION);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
        }
    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /* Converting Kelvin to Celsius */
    public static Double KtoC(Double k) {
        return k - 273.15;
    }
}