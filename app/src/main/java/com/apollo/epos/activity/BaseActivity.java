package com.apollo.epos.activity;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Rakesh on 14,October,2019
 */
public class BaseActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {


    protected static GoogleApiClient mGoogleApiClient;
    protected static LocationRequest mLocationRequest;
    private final String TAG = "mGoogleApiClient";
    protected static String geoLocation;
    protected static boolean checkForLocation = true;
    protected final int COUNT_DOWN_TIME = 30000;
    protected final int COUNT_DOWN_TIME_INTERVAL = 1000;
    private CountDownTimer mLocationCountDownTimer;

    public synchronized void buildGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API).build();
            createLocationRequest();
        }
    }


    /**
     * Creating location request object
     */
    protected void createLocationRequest() {
        if (mLocationRequest == null) {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(0);
            mLocationRequest.setFastestInterval(0);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setSmallestDisplacement(0); // 10 meters
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());

    }

    @Override
    public void onConnected(Bundle arg0) {
        if (geoLocation != null && geoLocation.length() > 0)
            return;
        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            startLocationUpdates();
            mLocationCountDownTimer = new CountDownTimer(COUNT_DOWN_TIME, COUNT_DOWN_TIME_INTERVAL) {

                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    stopLocationUpdates();
                    try {
                        Location loc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                        if (loc != null) {
                            geoLocation = "" + loc.getLatitude() + "," + loc.getLongitude();
                        }
                    } catch (SecurityException e) {

                    }
                }
            };
            mLocationCountDownTimer.start();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    /**
     * Starting the location updates
     */
    protected void startLocationUpdates() {
        if (mGoogleApiClient.isConnected()) {
            try {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            } catch (SecurityException e) {

            }
        }

    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        if (mGoogleApiClient.isConnected())
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onLocationChanged(Location loc) {
        // Assign the new location
        //mLastLocation = location;

        if (loc != null) {
            stopLocationUpdates();
            cancelLocationTimer();
            geoLocation = "" + loc.getLatitude() + "," + loc.getLongitude();
            if (this instanceof MapViewActivity) {
                MapViewActivity mapViewActivity = (MapViewActivity) this;
                mapViewActivity.refresh();
            }

        }

    }

    private void cancelLocationTimer() {
        if (mLocationCountDownTimer != null) {
            mLocationCountDownTimer.cancel();
            mLocationCountDownTimer = null;
        }
    }

    protected void getCurrentLocation(final Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (lm != null && lm.isProviderEnabled(LocationManager.GPS_PROVIDER) && mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                mGoogleApiClient.disconnect();
            }
            mGoogleApiClient.connect();
            new CountDownTimer(COUNT_DOWN_TIME, COUNT_DOWN_TIME_INTERVAL) {

                @Override
                public void onTick(long millisUntilFinished) {
                    if (geoLocation != null) {
                        if (BaseActivity.this instanceof MapViewActivity) {
                            MapViewActivity mapViewActivity = (MapViewActivity) BaseActivity.this;
                            mapViewActivity.refresh();
                        }
                    }

                    this.cancel();
                }

                @Override
                public void onFinish() {
                    this.cancel();
                }
            }.start();
        } else {

        }
    }

    protected void refresh() {

    }

}
