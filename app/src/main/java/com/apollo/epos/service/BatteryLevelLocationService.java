package com.apollo.epos.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.apollo.epos.db.SessionManager;
import com.apollo.epos.fragment.dashboard.model.RiderLalangBatteryStatusResponse;
import com.apollo.epos.fragment.dashboard.model.RiderLatlangBatteryStatusRequest;
import com.apollo.epos.network.ApiClient;
import com.apollo.epos.network.ApiInterface;
import com.apollo.epos.utils.ActivityUtils;
import com.apollo.epos.utils.CommonUtils;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BatteryLevelLocationService extends Service implements LocationListener {
    protected LocationManager locationManager;
    private Location currentLocation;

    @Override
    public void onCreate() {
        super.onCreate();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
        batteryPercentage();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void batteryPercentage() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            BatteryManager bm = (BatteryManager) getSystemService(BATTERY_SERVICE);
            int percentage = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
            if (currentLocation != null) {
                riderLatlangBatteryStatusApi(new SessionManager(this).getLoginToken(), String.valueOf(currentLocation.getLatitude()), String.valueOf(currentLocation.getLongitude()), String.valueOf(percentage));
            }
        }
        new Handler().postDelayed(this::batteryPercentage, 60000);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        this.currentLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        Toast.makeText(this, provider, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        Toast.makeText(this, provider, Toast.LENGTH_SHORT).show();
    }

    public void riderLatlangBatteryStatusApi(String token, String lattitude, String langitude, String batteryPercentage) {
        if (NetworkUtils.isNetworkConnected(this)) {
            RiderLatlangBatteryStatusRequest riderLatlangBatteryStatusRequest = new RiderLatlangBatteryStatusRequest();
            RiderLatlangBatteryStatusRequest.UserAddInfo userAddInfo = new RiderLatlangBatteryStatusRequest.UserAddInfo();
            userAddInfo.setBatteryStatus(batteryPercentage);
            userAddInfo.setLat(lattitude);
            userAddInfo.setLong(langitude);
            userAddInfo.setLastActive(ActivityUtils.getCurrentTimeDate());
            riderLatlangBatteryStatusRequest.setUserAddInfo(userAddInfo);

            ApiInterface apiInterface = ApiClient.getApiService();
            Call<RiderLalangBatteryStatusResponse> call = apiInterface.RIDER_LALANG_BATTERY_STATUS_API_CALL("Bearer " + token, riderLatlangBatteryStatusRequest);
            call.enqueue(new Callback<RiderLalangBatteryStatusResponse>() {
                @Override
                public void onResponse(@NotNull Call<RiderLalangBatteryStatusResponse> call, @NotNull Response<RiderLalangBatteryStatusResponse> response) {
                }

                @Override
                public void onFailure(@NotNull Call<RiderLalangBatteryStatusResponse> call, @NotNull Throwable t) {
                    System.out.println("Battery latlang and last active status api error:::::::::::::::::::::::::::::::"+t.getMessage());
                }
            });
        }
    }
}