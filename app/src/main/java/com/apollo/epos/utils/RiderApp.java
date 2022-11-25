package com.apollo.epos.utils;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

public class RiderApp extends Application implements LifecycleObserver {
    @Override
    public void onCreate() {
        super.onCreate();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private void onAppBackgrounded() {
        Log.d("RiderApp", "App in background");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void onAppForegrounded() {
        Log.d("RiderApp", "App in foreground");
        if (CommonUtils.CURRENT_SCREEN != null && (CommonUtils.CURRENT_SCREEN.equals("OrderDeliveryActivity") || CommonUtils.CURRENT_SCREEN.equals("TrackMapActivity"))) {
            CommonUtils.isIs_order_delivery_or_track_map_screen = true;
        }
    }
}
