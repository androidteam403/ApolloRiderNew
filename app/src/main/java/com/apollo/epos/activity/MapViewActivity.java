package com.apollo.epos.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ahmadrosid.lib.drawroutemap.DirectionApiCallback;
import com.ahmadrosid.lib.drawroutemap.DrawMarker;
import com.ahmadrosid.lib.drawroutemap.DrawRouteMaps;
import com.apollo.epos.R;
import com.ahmadrosid.lib.drawroutemap.TaskLoadedCallback;
import com.apollo.epos.utils.ActivityUtils;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapViewActivity extends BaseActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, DirectionApiCallback, TaskLoadedCallback {

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
    private Polyline currentPolyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_map_view);
        ButterKnife.bind(this);

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
        mMap.setPadding(0,0,0,80);
//        mMap.clear();
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
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng origin = new LatLng(location.getLatitude(), location.getLongitude());
        LatLng destination = new LatLng(17.4410197, 78.3788463);
        LatLng other = new LatLng(17.4411128, 78.3827845);

        if (!callOneTimeLocation) {
            DrawRouteMaps.getInstance(this,this,this)
                    .draw(origin, destination, mMap, 0);
            DrawRouteMaps.getInstance(this,this,this)
                    .draw(destination, other, mMap, 1);
            DrawMarker.getInstance(this).draw(mMap, origin, R.drawable.icon_delivery_person, "Current Location", 1, hashMap);
            DrawMarker.getInstance(this).draw(mMap, destination, R.drawable.icon_pharmacy, "Destination Location", 0, hashMap);
            DrawMarker.getInstance(this).draw(mMap, other, R.drawable.icon_ordered_user, "Other Location", 0, hashMap);

            LatLngBounds bounds = new LatLngBounds.Builder()
                    .include(origin)
                    .include(destination)
                    .include(other).build();

            Point displaySize = new Point();
            getWindowManager().getDefaultDisplay().getSize(displaySize);
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, displaySize.x, 300, 30));
            callOneTimeLocation = true;
        } else {
//            DrawRouteMaps.getInstance(this,this, this::onTaskDone)
//                    .draw(origin, destination, mMap, 0);
//            DrawMarker.getInstance(this).draw(mMap, origin, R.drawable.icon_delivery_person, "Current Location", 1, hashMap);
        }
    }


    private synchronized void GoogleClientBuild() {
        mGoogleApiClient = new GoogleApiClient.Builder(MapViewActivity.this).addApi(LocationServices.API).
                addConnectionCallbacks(this).
                addApi(AppIndex.API).
                addApi(AppIndex.API).
                addOnConnectionFailedListener(this).build();
    }

    public void createLocRequest() {
        mLocationRequest = new LocationRequest().setInterval(INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

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
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

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
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_LOCATION);
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
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

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
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_view);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (i == 1) {
            mGoogleApiClient.connect();
        }
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
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
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
    }

    @Override
    public void onTaskDone(Object... values) {
//        if (currentPolyline != null)
//            currentPolyline.remove();
//        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

    @OnClick(R.id.close_activity_img)
    void onMapCloseClick(){
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
