package com.apollo.epos.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.novoda.merlin.BindableInterface;
import com.novoda.merlin.Connectable;
import com.novoda.merlin.Disconnectable;
import com.novoda.merlin.Logger;
import com.novoda.merlin.Merlin;
import com.orhanobut.hawk.Hawk;

/**
 * Created by Rakesh on 14,October,2019
 */
public abstract class BaseActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private ProgressDialog dialog;
    protected static GoogleApiClient mGoogleApiClient;
    protected static LocationRequest mLocationRequest;
    private final String TAG = "mGoogleApiClient";
    protected static String geoLocation;
    protected static boolean checkForLocation = true;
    protected final int COUNT_DOWN_TIME = 30000;
    protected final int COUNT_DOWN_TIME_INTERVAL = 1000;
    private CountDownTimer mLocationCountDownTimer;

    private DemoLogHandle logHandle;
    protected Merlin merlin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Hawk.init(getApplicationContext()).build();
        logHandle = new DemoLogHandle();
        merlin = createMerlin();
    }

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


    protected abstract Merlin createMerlin();

    protected void registerConnectable(Connectable connectable) {
        merlin.registerConnectable(connectable);
    }

    protected void registerDisconnectable(Disconnectable disconnectable) {
        merlin.registerDisconnectable(disconnectable);
    }

    protected void registerBindable(BindableInterface bindable) {
        merlin.registerBindable(bindable);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Logger.attach(logHandle);
        merlin.bind();
    }

    @Override
    protected void onStop() {
        super.onStop();
        merlin.unbind();
        Logger.detach(logHandle);
    }

    private static class DemoLogHandle implements Logger.LogHandle {

        private static final String TAG = "DemoLogHandle";

        @Override
        public void v(Object... message) {
            Log.v(TAG, message[0].toString());
        }

        @Override
        public void i(Object... message) {
            Log.i(TAG, message[0].toString());
        }

        @Override
        public void d(Object... msg) {
            Log.d(TAG, msg[0].toString());
        }

        @Override
        public void d(Throwable throwable, Object... message) {
            Log.d(TAG, message[0].toString(), throwable);
        }

        @Override
        public void w(Object... message) {
            Log.w(TAG, message[0].toString());
        }

        @Override
        public void w(Throwable throwable, Object... message) {
            Log.w(TAG, message[0].toString(), throwable);
        }

        @Override
        public void e(Object... message) {
            Log.e(TAG, message[0].toString());
        }

        @Override
        public void e(Throwable throwable, Object... message) {
            Log.e(TAG, message[0].toString(), throwable);
        }
    }
}
