package com.apollo.epos.activity;

import android.Manifest;
import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.ahmadrosid.lib.drawroutemap.DirectionApiCallback;
import com.ahmadrosid.lib.drawroutemap.DrawRouteMaps;
import com.ahmadrosid.lib.drawroutemap.TaskLoadedCallback;
import com.apollo.epos.R;
import com.apollo.epos.adapter.CustomReasonAdapter;
import com.apollo.epos.dialog.DialogManager;
import com.apollo.epos.model.OrderItemModel;
import com.apollo.epos.service.GPSLocationService;
import com.apollo.epos.utils.ActivityUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.novoda.merlin.BindableInterface;
import com.novoda.merlin.Connectable;
import com.novoda.merlin.Disconnectable;
import com.novoda.merlin.Merlin;
import com.novoda.merlin.NetworkStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewOrderActivity extends BaseActivity implements DirectionApiCallback, TaskLoadedCallback, Connectable, Disconnectable, BindableInterface, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        ResultCallback<LocationSettingsResult> {
    @BindView(R.id.items_view_image)
    protected ImageView itemsViewImage;
    @BindView(R.id.items_view_layout)
    protected LinearLayout itemsViewLayout;
    @BindView(R.id.order_list_layout)
    protected LinearLayout orderListLayout;
    @BindView(R.id.btn_reject_order)
    Button rejectOrderBtn;
    String[] rejectReasons = {"Select from predefined statements", "Taken leave", "Not feeling well", "Soo far from my current location"};
    @BindView(R.id.order_delivery_time_layout)
    protected LinearLayout orderDeliveryTimeLayout;
    @BindView(R.id.anim_parent_layout)
    protected LinearLayout animParentLayout;
    @BindView(R.id.pharma_contact_number)
    protected ImageView pharmaContactNumber;
    @BindView(R.id.user_contact_number)
    protected ImageView userContactNumber;
    private boolean isContactingToUser = false;
    private static final int ACTIVITY_CHANGE = 10;
    @BindView(R.id.map_view_layout)
    protected LinearLayout mapViewLayout;
    @BindView(R.id.delivery_pharm_txt)
    protected TextView deliveryPharmTxt;
    @BindView(R.id.delivery_user_txt)
    protected TextView deliveryUserTxt;
    @BindView(R.id.total_distance_txt)
    protected TextView totalDistanceTxt;

    private float firstTime = 0;
    private float secondTime = 0;


    /**
     * Constant used in the location settings dialog.
     */
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    // Keys for storing activity state in the Bundle.
    protected final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    protected final static String KEY_LOCATION = "location";
    protected final static String KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string";

    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    protected LocationRequest mLocationRequest;

    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    protected LocationSettingsRequest mLocationSettingsRequest;

    /**
     * Represents a geographical location.
     */
    protected Location mCurrentLocation;

    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    protected Boolean mRequestingLocationUpdates;

    /**
     * Time when the location was updated represented as a String.
     */
    protected String mLastUpdateTime;

    private String TAG = "Location";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.icon_back);// Toolbar icon in Drawable folder
        setSupportActionBar(toolbar);

        ActivityUtils.showDialog(NewOrderActivity.this, "Getting Location");

        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();// Do what do you want on toolbar button
        });

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
//        collapsingToolbarLayout.setTitle("My Toolbar Title");
        collapsingToolbarLayout.setTitleEnabled(false);
        toolbar.setTitle("My Title");
        toolbar.setTitleTextAppearance(this, R.style.ToolbarTextAppearance);

        Animation RightSwipe = AnimationUtils.loadAnimation(this, R.anim.right_swipe);
        orderDeliveryTimeLayout.startAnimation(RightSwipe);

        mapViewLayout.setOnClickListener(v -> {
            startUpdatesButtonHandler(mapViewLayout);
        });


        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";

        // Update values using data stored in the Bundle.
        updateValuesFromBundle(savedInstanceState);

        // Kick off the process of building the GoogleApiClient, LocationRequest, and
        // LocationSettingsRequest objects.
        buildGoogleApiClient();
        createLocationRequest();
        buildLocationSettingsRequest();

        getCurrentLocation();
    }


    private void getCurrentLocation() {
        GPSLocationService gps = new GPSLocationService(this);
        if (gps.canGetLocation()) {

            LatLng origin = new LatLng(gps.getLatitude(), gps.getLongitude());

            LatLng destination = new LatLng(17.4410197, 78.3788463);
            LatLng other = new LatLng(17.4411128, 78.3827845);

            DrawRouteMaps.getInstance(this, this, this)
                    .draw(origin, destination, null, 0);
            DrawRouteMaps.getInstance(this, this, this)
                    .draw(destination, other, null, 1);
        } else {
            gps.showSettingsAlert();
        }
    }

    ArrayList<OrderItemModel> medicineList = new ArrayList<>();

    @OnClick(R.id.items_view_image)
    void onEyeImageClick() {
        if (itemsViewLayout.getVisibility() == View.VISIBLE) {
            LayoutTransition lt = new LayoutTransition();
            lt.disableTransitionType(LayoutTransition.CHANGE_DISAPPEARING);
            animParentLayout.setLayoutTransition(lt);

            itemsViewLayout.setVisibility(View.GONE);
            itemsViewImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_eye_open));
        } else {
            Transition transition = new Slide(Gravity.TOP);
            transition.setDuration(1000);
            transition.addTarget(R.id.anim_parent_layout);
            TransitionManager.beginDelayedTransition(animParentLayout, transition);

            itemsViewLayout.setVisibility(View.VISIBLE);

            itemsViewImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_eye_close));
            orderListLayout.removeAllViews();
            getMedicineList();
            for (int i = 0; i < medicineList.size(); i++) {
                LayoutInflater layoutInflater = LayoutInflater.from(this);
                LinearLayout layout = (LinearLayout) layoutInflater.inflate(R.layout.view_order_list, null, false);
                TextView orderName = layout.findViewById(R.id.order_name);
                TextView orderCount = layout.findViewById(R.id.order_count);
                TextView orderPrice = layout.findViewById(R.id.order_price);

                orderName.setText(medicineList.get(i).getMedicineName());
                orderCount.setText(medicineList.get(i).getQty());
                orderPrice.setText(medicineList.get(i).getItemPrice());
                orderListLayout.addView(layout);
            }
        }
    }

    private void getMedicineList() {
        medicineList.clear();
        OrderItemModel item = new OrderItemModel();
        item.setMedicineName("Tomoxifin 20mg");
        item.setQty("1");
        item.setItemPrice("78.00");
        medicineList.add(item);

        item = new OrderItemModel();
        item.setMedicineName("Atripla");
        item.setQty("1");
        item.setItemPrice("150.00");
        medicineList.add(item);

        item = new OrderItemModel();
        item.setMedicineName("Triaminic");
        item.setQty("1");
        item.setItemPrice("140.00");
        medicineList.add(item);

        item = new OrderItemModel();
        item.setMedicineName("Crocin");
        item.setQty("1");
        item.setItemPrice("150.00");
        medicineList.add(item);
    }

    @OnClick(R.id.btn_accept_order)
    public void onAcceptingOrder() {
        Intent i = new Intent(this, OrderDeliveryActivity.class);
        startActivityForResult(i, ACTIVITY_CHANGE);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    @OnClick(R.id.btn_reject_order)
    public void toggleBottomSheet() {
        View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(dialogView);
        dialog.setCancelable(false);
        dialog.show();

        ImageView closeDialog = dialogView.findViewById(R.id.close_icon);
        closeDialog.setOnClickListener(v -> {
            dialog.dismiss();
        });

        Spinner reasonSpinner = dialogView.findViewById(R.id.rejectReasonSpinner);
        CustomReasonAdapter customAdapter = new CustomReasonAdapter(this, rejectReasons);
        reasonSpinner.setAdapter(customAdapter);
        reasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(mActivity," "+reasonSpinner[position])
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @OnClick(R.id.pharma_contact_number)
    void onPharmaContactClick() {
        checkCallPermissionSetting();
    }

    @OnClick(R.id.user_contact_number)
    void onUserContactClick() {
        if (isContactingToUser)
            checkCallPermissionSetting();
    }

    private void checkCallPermissionSetting() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                Log.d("Permissions_Status", "Permissions not granted");
               /* if (ContextCompat.checkSelfPermission(CameraFunctionality.this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED)*/
                // ask for permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
            } else {
                Log.d("Permissions_Status", "We have a permission");
                // we have a permission
                requestACall();
            }
        } else {
            requestACall();
        }
    }

    private void requestACall() {
        String phonenumber = "+919440012212";
        Intent intentcall = new Intent();
        intentcall.setAction(Intent.ACTION_CALL);
        intentcall.setData(Uri.parse("tel:" + phonenumber)); // set the Uri
        startActivity(intentcall);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = getIntent();
            setResult(RESULT_OK, intent);
            finish();
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    Log.i(TAG, "User agreed to make required location settings changes.");
                    startLocationUpdates();
                    break;
                case Activity.RESULT_CANCELED:
                    Log.i(TAG, "User chose not to make required location settings changes.");
                    break;
            }
        } else if (requestCode == ACTIVITY_CHANGE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    boolean isOrderCompleted = Boolean.parseBoolean(data.getStringExtra("OrderCompleted"));
                    if (isOrderCompleted) {
                        Intent intent = getIntent();
                        intent.putExtra("OrderCompleted", "true");
                        setResult(RESULT_OK, intent);
                        finish();
                        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                    }
                }
            }
        }
    }

    @Override
    public void onDirectionApi(int colorFlag, JSONArray v) {
        String distance, time;
        float removing = 0;
        float removingTime = 0;
        try {
            if (v != null) {
                distance = ((JSONObject) v.get(0)).getJSONObject("distance").getString("text");
                time = ((JSONObject) v.get(0)).getJSONObject("duration").getString("text");
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
                                    deliveryPharmTxt.setText(finalRemoving + "");
                                    firstTime = finalTime;
                                } else if (colorFlag == 1) {
                                    deliveryUserTxt.setText(finalRemoving + "");
                                    secondTime = finalTime;
                                }
                                if (!deliveryPharmTxt.getText().toString().isEmpty() && !deliveryUserTxt.getText().toString().isEmpty() && firstTime != 0 && secondTime != 0) {
                                    float finalDistance = Float.parseFloat(deliveryPharmTxt.getText().toString()) + Float.parseFloat(deliveryUserTxt.getText().toString());
                                    float lastTime = firstTime + secondTime;
                                    totalDistanceTxt.setText("Total distance is " + finalDistance + "KM from your location and expected time is " + Math.round(lastTime) + "mins.");
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
    protected Merlin createMerlin() {
        return new Merlin.Builder()
                .withConnectableCallbacks()
                .withDisconnectableCallbacks()
                .withBindableCallbacks()
                .build(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (GPSLocationService.isFromSetting == true) {
            GPSLocationService.isFromSetting = false;
        }
        registerConnectable(this);
        registerDisconnectable(this);
        registerBindable(this);

        // Within {@code onPause()}, we pause location updates, but leave the
        // connection to GoogleApiClient intact.  Here, we resume receiving
        // location updates if the user has requested them.
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onBind(NetworkStatus networkStatus) {
        if (!networkStatus.isAvailable()) {
            onDisconnect();
        }
    }

    @Override
    public Polyline onTaskDone(Object... values) {
        return null;
    }

    @Override
    public Polyline onSecondTaskDone(Object... values) {
        return null;
    }

    @Override
    public void onConnect() {
//        ActivityUtils.hideDialog();
//        DialogManager.showToast(NewOrderActivity.this, "Network Connected");
        getCurrentLocation();
    }

    @Override
    public void onDisconnect() {
//        ActivityUtils.showDialog(NewOrderActivity.this, "Getting Location");
        DialogManager.showToast(NewOrderActivity.this, getString(R.string.no_interent));
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * Updates fields based on data stored in the bundle.
     *
     * @param savedInstanceState The activity state saved in the Bundle.
     */
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Update the value of mRequestingLocationUpdates from the Bundle, and make sure that
            // the Start Updates and Stop Updates buttons are correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(KEY_REQUESTING_LOCATION_UPDATES)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        KEY_REQUESTING_LOCATION_UPDATES);
            }

            // Update the value of mCurrentLocation from the Bundle and update the UI to show the
            // correct latitude and longitude.
            if (savedInstanceState.keySet().contains(KEY_LOCATION)) {
                // Since KEY_LOCATION was found in the Bundle, we can be sure that mCurrentLocation
                // is not null.
                mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            }

            // Update the value of mLastUpdateTime from the Bundle and update the UI.
            if (savedInstanceState.keySet().contains(KEY_LAST_UPDATED_TIME_STRING)) {
                mLastUpdateTime = savedInstanceState.getString(KEY_LAST_UPDATED_TIME_STRING);
            }
        }
    }

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     */
    public synchronized void buildGoogleApiClient() {
//        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(NewOrderActivity.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Uses a {@link com.google.android.gms.location.LocationSettingsRequest.Builder} to build
     * a {@link com.google.android.gms.location.LocationSettingsRequest} that is used for checking
     * if a device has the needed location settings.
     */
    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    /**
     * Check if the device's location settings are adequate for the app's needs using the
     * {@link com.google.android.gms.location.SettingsApi#checkLocationSettings(GoogleApiClient,
     * LocationSettingsRequest)} method, with the results provided through a {@code PendingResult}.
     */
    protected void checkLocationSettings() {
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        mLocationSettingsRequest
                );
        result.setResultCallback(this);
    }

    /**
     * The callback invoked when
     * {@link com.google.android.gms.location.SettingsApi#checkLocationSettings(GoogleApiClient,
     * LocationSettingsRequest)} is called. Examines the
     * {@link com.google.android.gms.location.LocationSettingsResult} object and determines if
     * location settings are adequate. If they are not, begins the process of presenting a location
     * settings dialog to the user.
     */
    @Override
    public void onResult(LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                Log.i(TAG, "All location settings are satisfied.");
                startLocationUpdates();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to" +
                        "upgrade location settings ");

                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    status.startResolutionForResult(NewOrderActivity.this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    Log.i(TAG, "PendingIntent unable to execute request.");
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog " +
                        "not created.");
                break;
        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            // Check for the integer request code originally supplied to startResolutionForResult().
//            case REQUEST_CHECK_SETTINGS:
//                switch (resultCode) {
//                    case Activity.RESULT_OK:
//                        Log.i(TAG, "User agreed to make required location settings changes.");
//                        startLocationUpdates();
//                        break;
//                    case Activity.RESULT_CANCELED:
//                        Log.i(TAG, "User chose not to make required location settings changes.");
//                        break;
//                }
//                break;
//        }
//    }

    /**
     * Handles the Start Updates button and requests start of location updates. Does nothing if
     * updates have already been requested.
     */
    public void startUpdatesButtonHandler(View view) {
        checkLocationSettings();
    }

    /**
     * Handles the Stop Updates button, and requests removal of location updates.
     */
    public void stopUpdatesButtonHandler(View view) {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        stopLocationUpdates();
    }

    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                mLocationRequest,
                this
        ).setResultCallback(status -> {
            mRequestingLocationUpdates = true;
            Intent intent = new Intent(NewOrderActivity.this, MapViewActivity.class);
            startActivity(intent);
        });

    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    protected void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient,
                this
        ).setResultCallback(status -> {
            mRequestingLocationUpdates = false;
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i("Location", "Connected to GoogleApiClient");

        // If the initial location was never previously requested, we use
        // FusedLocationApi.getLastLocation() to get it. If it was previously requested, we store
        // its value in the Bundle and check for it in onCreate(). We
        // do not request it again unless the user specifically requests location updates by pressing
        // the Start Updates button.
        //
        // Because we cache the value of the initial location in the Bundle, it means that if the
        // user launches the activity,
        // moves to a new location, and then changes the device orientation, the original location
        // is displayed as the activity is re-created.
        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        }
    }

    /**
     * Callback that fires when the location changes.
     */
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
    }

    @Override
    public void onConnectionSuspended(int cause) {

        Log.i("Location", "Connection suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i("Location", "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    /**
     * Stores activity data in the Bundle.
     */

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(KEY_REQUESTING_LOCATION_UPDATES, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(KEY_LOCATION, mCurrentLocation);
        savedInstanceState.putString(KEY_LAST_UPDATED_TIME_STRING, mLastUpdateTime);
// Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }


}
