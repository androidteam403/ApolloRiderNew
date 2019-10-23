package com.apollo.epos.activity;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ahmadrosid.lib.drawroutemap.DirectionApiCallback;
import com.ahmadrosid.lib.drawroutemap.DrawMarker;
import com.ahmadrosid.lib.drawroutemap.DrawRouteMaps;
import com.ahmadrosid.lib.drawroutemap.ProgressLoadedCallback;
import com.ahmadrosid.lib.drawroutemap.TaskLoadedCallback;
import com.apollo.epos.R;
import com.apollo.epos.dialog.DialogManager;
import com.apollo.epos.service.FloatingTouchService;
import com.apollo.epos.service.GPSLocationService;
import com.apollo.epos.utils.ActivityUtils;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.novoda.merlin.BindableInterface;
import com.novoda.merlin.Connectable;
import com.novoda.merlin.Disconnectable;
import com.novoda.merlin.Merlin;
import com.novoda.merlin.NetworkStatus;
import com.orhanobut.hawk.Hawk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.apollo.epos.utils.ActivityUtils.getBigFloatToDecimalFloat;
import static com.apollo.epos.utils.AppConstants.LAST_ACTIVITY;

public class MapViewActivity extends BaseActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, DirectionApiCallback, TaskLoadedCallback, Connectable,
        Disconnectable, BindableInterface, ProgressLoadedCallback {

    private GoogleMap mMap;
    private final int REQ_LOC_PERMISSION = 123;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    protected static GoogleApiClient mGoogleApiClient;
    protected static LocationRequest mLocationRequest;
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    private HashMap<Integer, Marker> hashMap = new HashMap<>();
    private boolean callOneTimeLocation;
    @BindView(R.id.close_activity_img)
    protected ImageView closeActivityImg;
    private Polyline currentPolyline, secondPolyline;
    private LatLng origin, destination, other;
    @BindView(R.id.follow_google_map)
    protected Button followGoogleMap;
    @BindView(R.id.travel_info_layout)
    protected LinearLayout travelInfoLayout;
    @BindView(R.id.travel_distance)
    protected TextView travelDistance;
    @BindView(R.id.travel_time)
    protected TextView travelTime;
    private float firstTime = 0, secondTime = 0, firstDistance = 0, secondDistance = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_map_view);
        ButterKnife.bind(this);

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

        travelInfoLayout.setVisibility(View.VISIBLE);
        ActivityUtils.showDialog(MapViewActivity.this, "Getting Location");

        followGoogleMap.setVisibility(View.GONE);

//show error dialog if GoolglePlayServices not available
        if (ActivityUtils.checkPlayServices(MapViewActivity.this)) {
            buildGoogleApiClient();
        }
        createLocRequest();
        GoogleClientBuild();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setPadding(0, 0, 0, 80);
        mMap.clear();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(MapViewActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                GoogleClientBuild();
//                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            GoogleClientBuild();
//            mGoogleMap.setMyLocationEnabled(true);
        }

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider);

        if (origin == null && destination == null && other == null) {
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                LatLng latLng = new LatLng(latitude, longitude);
//            myPosition = new LatLng(latitude, longitude);

                LatLng coordinate = new LatLng(latitude, longitude);
                CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 19);
                mMap.animateCamera(yourLocation);
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        origin = new LatLng(location.getLatitude(), location.getLongitude());
        destination = new LatLng(17.4410197, 78.3788463);
        other = new LatLng(17.4411128, 78.3827845);


        DrawMarker.getInstance(this).draw(mMap, origin, R.drawable.location_current, "Current Location", 1, hashMap);
        DrawMarker.getInstance(this).draw(mMap, destination, R.drawable.location_pharmacy, "Destination Location", 0, hashMap);
        DrawMarker.getInstance(this).draw(mMap, other, R.drawable.location_destination, "Other Location", 0, hashMap);

        if (!callOneTimeLocation) {
            DrawRouteMaps.getInstance(this, this, this).draw(origin, destination, mMap, 0);
            DrawRouteMaps.getInstance(this, this, this).draw(destination, other, mMap, 1);

            LatLngBounds bounds = new LatLngBounds.Builder().include(origin).include(destination).include(other).build();

            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;
            int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
            mMap.moveCamera(cu);
            mMap.animateCamera(cu);

            callOneTimeLocation = true;
        }
        ActivityUtils.hideDialog();
    }

    private synchronized void GoogleClientBuild() {
        mGoogleApiClient = new GoogleApiClient.Builder(MapViewActivity.this).addApi(LocationServices.API).addConnectionCallbacks(this).addApi(AppIndex.API).addApi(AppIndex.API).addOnConnectionFailedListener(this).build();
    }

    public void createLocRequest() {
        mLocationRequest = new LocationRequest().setInterval(INTERVAL).setFastestInterval(FASTEST_INTERVAL).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdate();
        }
    }

    public void startLocationUpdate() {
        checkPermission();
    }

    private void checkPermission() {
        int checkSelf = ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (checkSelf != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MapViewActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_LOC_PERMISSION);
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_LOC_PERMISSION);
                }
            }
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            initializeMap();
        }
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    new AlertDialog.Builder(getApplicationContext())
                            .setTitle(getString(R.string.permission_location_need_title))
                            .setMessage(getString(R.string.permission_location_need_body))
                            .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //Prompt the user once explanation has been shown
                                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                            MY_PERMISSIONS_REQUEST_LOCATION);
                                }
                            })
                            .create()
                            .show();
                } else {
                    // No explanation needed, we can request the permission.
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQ_LOC_PERMISSION:
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient != null) {
                            mGoogleApiClient.connect();
                        } else {
                            buildGoogleApiClient();
                            mGoogleApiClient.connect();
                        }

                        initializeMap();
                    }
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getApplicationContext(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private void initializeMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_view);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        ActivityUtils.showDialog(MapViewActivity.this, "Getting Location");
    }

    @Override
    public void onStop() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        callOneTimeLocation = false;
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
        hashMap.clear();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onDirectionApi(int colorFlag, JSONArray jsonArray) {
        String distance, time;
        float removing = 0;
        float removingTime = 0;
        try {
            if (jsonArray != null) {
                distance = ((JSONObject) jsonArray.get(0)).getJSONObject("distance").getString("text");
                time = ((JSONObject) jsonArray.get(0)).getJSONObject("duration").getString("text");
                removing = Float.parseFloat(distance.replace("\"", "").replace("km", ""));//Pattern.compile("\"").matcher(distance).replaceAll("");
                removingTime = Float.parseFloat(time.replace("\"", "").replace("mins", ""));//Pattern.compile("\"").matcher(distance).replaceAll("");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        float finalRemoving = removing;
        float finalTime = removingTime;
        if (finalRemoving != 0) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    float i;
                    try {
                        for (i = 0; i <= 100; i++) {
                            runOnUiThread(() -> {
                                if (colorFlag == 0) {
                                    firstDistance = finalRemoving;
                                    firstTime = finalTime;
                                } else if (colorFlag == 1) {
                                    secondDistance = finalRemoving;
                                    secondTime = finalTime;
                                }
                                if (firstDistance != 0 && secondDistance != 0 && firstTime != 0 && secondTime != 0) {
                                    float finalDistance = firstDistance + secondDistance;
                                    float lastTime = firstTime + secondTime;
                                    travelDistance.setText("Distance " + getBigFloatToDecimalFloat(finalDistance) + "KM");
                                    travelTime.setText("Time " + Math.round(lastTime) + "Mins.");
                                }
                            });
                            sleep(500);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            thread.start();
        }
        ActivityUtils.hideDialog();
    }

    @Override
    public Polyline onTaskDone(Object... values) {
        if (currentPolyline != null) {
            currentPolyline.remove();
        }
        return currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

    @Override
    public Polyline onSecondTaskDone(Object... values) {
        if (secondPolyline != null) {
            secondPolyline.remove();
        }
        return secondPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

    @OnClick(R.id.close_activity_img)
    void onMapCloseClick() {
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    protected Merlin createMerlin() {
        return new Merlin.Builder()
                .withConnectableCallbacks()
                .withDisconnectableCallbacks()
                .withBindableCallbacks()
                .build(this);
    }

    @Override
    public void onBind(NetworkStatus networkStatus) {
        if (!networkStatus.isAvailable()) {
            onDisconnect();
        }
    }

    @Override
    public void onConnect() {
//        ActivityUtils.hideDialog();
//        DialogManager.showToast(MapViewActivity.this, "Network Connected");
    }

    @Override
    public void onDisconnect() {
//        ActivityUtils.showDialog(MapViewActivity.this, "Getting Location");
        DialogManager.showToast(MapViewActivity.this, getString(R.string.no_interent));
    }

    @Override
    public void onTaskDone(boolean flag) {

    }

    @Override
    protected void onResume() {
        Hawk.put(LAST_ACTIVITY, getClass().getSimpleName());
        super.onResume();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }

        registerConnectable(this);
        registerDisconnectable(this);
        registerBindable(this);

        hashMap.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(MapViewActivity.this, FloatingTouchService.class);
        if (isMyServiceRunning(FloatingTouchService.class)) {
            stopService(intent);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
