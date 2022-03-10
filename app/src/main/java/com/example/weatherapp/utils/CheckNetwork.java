package com.example.weatherapp.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;

public class CheckNetwork {

    public static boolean isNetworkAvailable(Activity activity) {

        boolean isOnline = false;
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
            // need ACCESS_NETWORK_STATE permission
            isOnline = capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
            return isOnline;
        }else{
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

            return activeNetwork != null
                    && activeNetwork.isConnectedOrConnecting();
        }

    }
}
