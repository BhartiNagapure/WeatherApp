package com.example.weatherapp.model;

import android.os.Build;

import androidx.annotation.RequiresApi;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LocationModel {

     double latitude;
     double longitude;

    public LocationModel(double latitude, double longitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public final double component1() {
        return this.longitude;
    }

    public final double component2() {
        return this.latitude;
    }

    @NotNull
    public final LocationModel copy(double longitude, double latitude) {
        return new LocationModel(longitude, latitude);
    }

    // $FF: synthetic method
    public static LocationModel copy$default(LocationModel var0, double var1, double var3, int var5, Object var6) {
        if ((var5 & 1) != 0) {
            var1 = var0.longitude;
        }

        if ((var5 & 2) != 0) {
            var3 = var0.latitude;
        }

        return var0.copy(var1, var3);
    }

    @NotNull
    public String toString() {
        return "LocationModel(longitude=" + this.longitude + ", latitude=" + this.latitude + ")";
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int hashCode() {
        return Double.hashCode(this.longitude) * 31 + Double.hashCode(this.latitude);
    }

    public boolean equals(@Nullable Object var1) {
        if (this != var1) {
            if (var1 instanceof LocationModel) {
                LocationModel var2 = (LocationModel)var1;
                if (Double.compare(this.longitude, var2.longitude) == 0 && Double.compare(this.latitude, var2.latitude) == 0) {
                    return true;
                }
            }

            return false;
        } else {
            return true;
        }
    }
}
