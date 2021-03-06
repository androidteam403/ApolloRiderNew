package com.apollo.epos.activity;

import android.Manifest;
import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.ahmadrosid.lib.drawroutemap.DirectionApiCallback;
import com.ahmadrosid.lib.drawroutemap.DrawRouteMaps;
import com.ahmadrosid.lib.drawroutemap.PiontsCallback;
import com.ahmadrosid.lib.drawroutemap.TaskLoadedCallback;
import com.apollo.epos.R;
import com.apollo.epos.activity.neworder.NewOrderActivityCallback;
import com.apollo.epos.activity.neworder.NewOrderActivityController;
import com.apollo.epos.activity.neworder.model.OrderDetailsResponse;
import com.apollo.epos.activity.orderdelivery.OrderDeliveryActivity;
import com.apollo.epos.adapter.CustomReasonAdapter;
import com.apollo.epos.dialog.DialogManager;
import com.apollo.epos.model.OrderItemModel;
import com.apollo.epos.service.FloatingTouchService;
import com.apollo.epos.service.GPSLocationService;
import com.apollo.epos.utils.ActivityUtils;
import com.apollo.epos.utils.CommonUtils;
import com.example.easywaylocation.EasyWayLocation;
import com.example.easywaylocation.GetLocationDetail;
import com.example.easywaylocation.Listener;
import com.example.easywaylocation.LocationData;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.novoda.merlin.BindableInterface;
import com.novoda.merlin.Connectable;
import com.novoda.merlin.Disconnectable;
import com.novoda.merlin.Merlin;
import com.novoda.merlin.NetworkStatus;
import com.orhanobut.hawk.Hawk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.apollo.epos.utils.AppConstants.LAST_ACTIVITY;
import static com.example.easywaylocation.EasyWayLocation.LOCATION_SETTING_REQUEST_CODE;

public class NewOrderActivity extends BaseActivity implements DirectionApiCallback, TaskLoadedCallback, Connectable, Disconnectable, BindableInterface, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        ResultCallback<LocationSettingsResult>, PiontsCallback, Listener, LocationData.AddressCallBack, NewOrderActivityCallback {
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
    private double currentLat, currentLon;

    private float firstTime = 0;
    private float secondTime = 0;

    //
    private String pickupPhoneNumber;
    private String customerPhoneNumber;
    private OrderDetailsResponse orderDetailsResponse;
    //
    private TextView orderNumber, deliveryon, crateAmount, paymentType;

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

    private final int REQ_LOC_PERMISSION = 5002;
    private boolean isGpsDataReceived = false;
    private EasyWayLocation easyWayLocation;
    private GetLocationDetail getLocationDetail;

    public static Intent getStartIntent(Context context, String orderNumber) {
        Intent intent = new Intent(context, NewOrderActivity.class);
        intent.putExtra("order_number", orderNumber);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        return intent;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            try {
                String orderNumberId = bundle.getString("uid");
                new NewOrderActivityController(this, this).orderDetailsApiCall(getSessionManager().getLoginToken(), orderNumberId);

                //bundle must contain all info sent in "data" field of the notification
            }catch (Exception e){
                System.out.println("push notification new order activity::::::::::::::::::::::::::::"+e.getMessage());
            }

        }
        if (getIntent() != null) {
            if (getIntent().getStringExtra("order_number") != null) {
                String orderNumberId = getIntent().getStringExtra("order_number");
                if (orderNumberId != null && !orderNumberId.isEmpty()) {
                    new NewOrderActivityController(this, this).orderDetailsApiCall(getSessionManager().getLoginToken(), orderNumberId);
                } else {
                    finish();
                    return;
                }
            }
        }
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setNavigationIcon(R.drawable.icon_back);// Toolbar icon in Drawable folder
//        setSupportActionBar(toolbar);

        ImageView iconback = (ImageView) findViewById(R.id.icon_back);
        iconback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
//        toolbar.setNavigationOnClickListener(v -> {
//            onBackPressed();// Do what do you want on toolbar button
//        });

//        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
//        collapsingToolbarLayout.setTitle("My Toolbar Title");
//        collapsingToolbarLayout.setTitleEnabled(false);
//        toolbar.setTitle("My Title");
//        toolbar.setTitleTextAppearance(this, R.style.ToolbarTextAppearance);
//        ActivityUtils.showDialog(NewOrderActivity.this, "Getting Location");

        Animation RightSwipe = AnimationUtils.loadAnimation(this, R.anim.right_swipe);
        orderDeliveryTimeLayout.startAnimation(RightSwipe);

        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";

        // Update values using data stored in the Bundle.
        updateValuesFromBundle(savedInstanceState);

        // Kick off the process of building the GoogleApiClient, LocationRequest, and
        // LocationSettingsRequest objects.

        //show error dialog if GoolglePlayServices not available
        if (ActivityUtils.checkPlayServices(NewOrderActivity.this)) {
            buildGoogleApiClient();
        }
        createLocationRequest();
        GoogleClientBuild();
        buildLocationSettingsRequest();

        mapViewLayout.setOnClickListener(v -> {
            startUpdatesButtonHandler(mapViewLayout);

        });

        orderDeliveryTimeLayout.setVisibility(View.INVISIBLE);

        getLocationDetail = new GetLocationDetail(this, this);
        easyWayLocation = new EasyWayLocation(this, false, this);
        if (permissionIsGranted()) {
            doLocationWork();
        } else {
            // Permission not granted, ask for it
            //testLocationRequest.requestPermission(121);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(NewOrderActivity.this, permission)) {
                //denied
                Log.e("denied", permission);
            } else {
                if (ActivityCompat.checkSelfPermission(NewOrderActivity.this, permission) == PackageManager.PERMISSION_GRANTED) {
                    //allowed
                    Log.e("allowed", permission);

                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        // permission was granted, yay! Do the
                        // location-related task you need to do.
                        if (ContextCompat.checkSelfPermission(NewOrderActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                            if (mGoogleApiClient != null) {
                                mGoogleApiClient.connect();
                            } else {
                                buildGoogleApiClient();
                                mGoogleApiClient.connect();
                            }
                        }
                    }
                } else {
                    //set to never ask again
                    Log.e("set to never ask again", permission);
                    //do something here.
                    requestLocation();
                }
            }
        }
//        switch (requestCode) {
//            case REQ_LOC_PERMISSION:
//            case MY_PERMISSIONS_REQUEST_LOCATION: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    // permission was granted, yay! Do the
//                    // location-related task you need to do.
//                    if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//
//                        if (mGoogleApiClient != null) {
//                            mGoogleApiClient.connect();
//                        } else {
//                            buildGoogleApiClient();
//                            mGoogleApiClient.connect();
//                        }
//                    }
//                } else {
//
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
////                    Toast.makeText(mActivity, "permission denied", Toast.LENGTH_LONG).show();
//                    requestLocation();
//
//                }
//                return;
//            }
//
//            // other 'case' lines to check for other
//            // permissions this app might request
//        }
    }

    public void requestLocation() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
//        String rationale = "Please provide location permission so that you can ...";
//        Permissions.Options options = new Permissions.Options()
//                .setRationaleDialogTitle("Info")
//                .setSettingsDialogTitle("Warning");

//        Permiss ions.check(NewOrderActivity.this, permissions, null, null, new PermissionHandler() {
//            @Override
//            public void onGranted() {
////                Toast.makeText(mActivity, "Location granted.", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
//                Toast.makeText(NewOrderActivity.this, "Location denied.", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private synchronized void GoogleClientBuild() {
        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, 1, this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }
//        mGoogleApiClient = new GoogleApiClient.Builder(NewOrderActivity.this).addApi(LocationServices.API).addConnectionCallbacks(this).addApi(AppIndex.API).addApi(AppIndex.API).addOnConnectionFailedListener(this).build();
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
//            orderListLayout.removeAllViews();
//            getMedicineList();
//            for (int i = 0; i < medicineList.size(); i++) {
//                LayoutInflater layoutInflater = LayoutInflater.from(this);
//                LinearLayout layout = (LinearLayout) layoutInflater.inflate(R.layout.view_order_list, null, false);
//                TextView orderName = layout.findViewById(R.id.order_name);
//                TextView orderCount = layout.findViewById(R.id.order_count);
//                TextView orderPrice = layout.findViewById(R.id.order_price);
//
//                orderName.setText(medicineList.get(i).getMedicineName());
//                orderCount.setText(medicineList.get(i).getQty());
//                orderPrice.setText(medicineList.get(i).getItemPrice());
//                orderListLayout.addView(layout);
//            }
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
//        Intent i = new Intent(this, OrderDeliveryActivity.class);
//        startActivityForResult(i, ACTIVITY_CHANGE);
        if (orderDetailsResponse != null) {
            startActivityForResult(OrderDeliveryActivity.getStartIntent(this, orderDetailsResponse), ACTIVITY_CHANGE);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        }
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
        CustomReasonAdapter customAdapter = new CustomReasonAdapter(this, rejectReasons,null);
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
        String phonenumber = "+919177551736";
        Intent intentcall = new Intent();
        intentcall.setAction(Intent.ACTION_CALL);
        intentcall.setData(Uri.parse("tel:" + pickupPhoneNumber)); // set the Uri
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
        } else if (requestCode == LOCATION_SETTING_REQUEST_CODE) {
            easyWayLocation.onActivityResult(resultCode);
        }
    }

    @Override
    public void onDirectionApi(int colorFlag, JSONArray v) {
        String distance, time;
        float removing = 0;
        int removingTime = 0;
        try {
            if (v != null) {
                distance = ((JSONObject) v.get(0)).getJSONObject("distance").getString("text");
                time = ((JSONObject) v.get(0)).getJSONObject("duration").getString("value");
                removing = Float.parseFloat(distance.replace("\"", "").replace("km", ""));//Pattern.compile("\"").matcher(distance).replaceAll("");
//                removingTime = Float.parseFloat(time.replace("\"", "").replace("mins", ""));//Pattern.compile("\"").matcher(distance).replaceAll("");
                removingTime = Integer.parseInt(time) / 60;
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
//                                    totalDistanceTxt.setText("Total distance is " + getBigFloatToDecimalFloat(finalDistance) + "KM from your location and expected time is " + Math.round(lastTime) + "mins.");
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
        runOnUiThread(() -> {
            orderDeliveryTimeLayout.setVisibility(View.VISIBLE);
            Animation RightSwipe = AnimationUtils.loadAnimation(NewOrderActivity.this, R.anim.right_swipe);
            orderDeliveryTimeLayout.startAnimation(RightSwipe);
        });
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
    public Polyline onTaskDone(boolean flag, Object... values) {
        return null;
    }

    @Override
    public Polyline onSecondTaskDone(boolean flag, Object... values) {
        return null;
    }

    @Override
    public void onConnect() {
//        ActivityUtils.hideDialog();
//        DialogManager.showToast(NewOrderActivity.this, "Network Connected");
//        getCurrentLocation();
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

        easyWayLocation.endUpdates();
    }

    @Override
    protected void onResume() {
        Hawk.put(LAST_ACTIVITY, getClass().getSimpleName());
        super.onResume();
        if (GPSLocationService.isFromSetting == true) {
            GPSLocationService.isFromSetting = false;
        }
        registerConnectable(this);
        registerDisconnectable(this);
        registerBindable(this);
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
        easyWayLocation.startLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(NewOrderActivity.this, FloatingTouchService.class);
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
        checkPermission();
    }


    private void checkPermission() {
        int checkSelf = ActivityCompat.checkSelfPermission(NewOrderActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (checkSelf != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(NewOrderActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_LOC_PERMISSION);
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_LOC_PERMISSION);
                }
            }
        } else {
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
//        getCurrentLocation();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
//        isGpsDataReceived = false;
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
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
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

    @Override
    public void pointsFirst(List<LatLng> pionts) {

    }

    @Override
    public void pointsSecond(List<LatLng> pionts) {

    }

    public boolean permissionIsGranted() {

        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void doLocationWork() {
        easyWayLocation.startLocation();
    }

    @Override
    public void locationOn() {
        Toast.makeText(this, "Location On", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void currentLocation(Location location) {

        Location locationA = new Location("point A");
        locationA.setLatitude(currentLat);
        locationA.setLongitude(currentLon);
        Location locationB = new Location("point B");
        locationB.setLatitude(location.getLatitude());
        locationB.setLongitude(location.getLongitude());

        double distance = locationA.distanceTo(locationB);

        if (Math.round(distance) > 100) {
            isGpsDataReceived = false;
        }

        LatLng origin = new LatLng(location.getLatitude(), location.getLongitude());
        currentLat = location.getLatitude();
        currentLon = location.getLongitude();
        LatLng destination = new LatLng(17.4410197, 78.3788463);
        LatLng other = new LatLng(17.4411128, 78.3827845);

        if (!isGpsDataReceived) {

            DrawRouteMaps.getInstance(this, this, this, this)
                    .draw(origin, destination, null, 0);
            DrawRouteMaps.getInstance(this, this, this, this)
                    .draw(destination, other, null, 1);

            isGpsDataReceived = true;
        }
    }

    @Override
    public void locationCancelled() {
        Toast.makeText(this, "Location Cancelled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void locationData(LocationData locationData) {
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSuccessOrderDetailsApiCall(OrderDetailsResponse orderDetailsResponse) {
        if (orderDetailsResponse != null) {
            this.orderDetailsResponse = orderDetailsResponse;
            try {
                orderNumber = (TextView) findViewById(R.id.order_number);
                deliveryon = (TextView) findViewById(R.id.deliveryon_datetime);
                crateAmount = (TextView) findViewById(R.id.crate_amount);
                paymentType = (TextView) findViewById(R.id.payment_type);
                if (orderDetailsResponse.getData() != null) {
                    orderNumber.setText(orderDetailsResponse.getData().getOrderNumber());

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    String orderDate = orderDetailsResponse.getData().getCreatedTime();
                    Date orderDates = formatter.parse(orderDate);
                    long orderDateMills = orderDates.getTime();
                    deliveryon.setText(CommonUtils.getTimeFormatter(orderDateMills));

                    crateAmount.setText(String.valueOf(orderDetailsResponse.getData().getCrateAmount()));
                    paymentType.setText(orderDetailsResponse.getData().getPaymentType().getName());
                    TextView pharmacyAddress = (TextView) findViewById(R.id.pharmacy_address);
                    TextView customerAddress = (TextView) findViewById(R.id.customer_address);
                    TextView pharmacyAddId = (TextView) findViewById(R.id.pharmacy_add_id);
                    TextView customerName = (TextView) findViewById(R.id.customer_name);

                    LinearLayout pickupInstruction = (LinearLayout) findViewById(R.id.pickup_instruction);
                    LinearLayout deliveryInstruction = (LinearLayout) findViewById(R.id.delivery_instruction);
                    TextView pickupInstructionText = (TextView) findViewById(R.id.pickup_instruction_text);
                    TextView deliveryInstructionText = (TextView) findViewById(R.id.delivery_instruction_text);

                    TextView pharmacyLandmark = (TextView) findViewById(R.id.pharmacy_landmark);
                    TextView customerLandmark = (TextView) findViewById(R.id.customer_landmark);

                    TextView pickupPhoneNumber = (TextView) findViewById(R.id.pickup_phone_number);
                    TextView customerPhoneNumber = (TextView) findViewById(R.id.customer_phone_number);
                    String pickupAddress = orderDetailsResponse.getData().getDeliverApartment() + ", " + orderDetailsResponse.getData().getDeliverStreetName() + ", " + orderDetailsResponse.getData().getDeliverCity() + ", " + orderDetailsResponse.getData().getDeliverState() + ", " + orderDetailsResponse.getData().getDelPincode() + ", " + orderDetailsResponse.getData().getDeliverCountry();
                    String customerAddresss = orderDetailsResponse.getData().getPickupApt() + ", " + orderDetailsResponse.getData().getPickupStreetName() + ", " + orderDetailsResponse.getData().getPickupCity() + ", " + orderDetailsResponse.getData().getPickupState() + ", " + orderDetailsResponse.getData().getPickupPincode() + ", " + orderDetailsResponse.getData().getPickupCountry();
                    if (orderDetailsResponse.getData().getOrderState().getName().equals("RETURN")) {
                        pharmacyLandmark.setText(orderDetailsResponse.getData().getDeliverLandmark());
                        customerLandmark.setText(orderDetailsResponse.getData().getPickupLndmrk());
                        pharmacyAddId.setText(orderDetailsResponse.getData().getDelAddId());
                        customerName.setText(orderDetailsResponse.getData().getPickupAddId());
                        pharmacyAddress.setText(customerAddresss);
                        customerAddress.setText(pickupAddress);
                        String pickupPhoneNumbers = String.valueOf(orderDetailsResponse.getData().getDelPn());
                        String customerPhoneNumbers = String.valueOf(orderDetailsResponse.getData().getPickupPn());
                        pickupPhoneNumber.setText("*******" + pickupPhoneNumbers.substring(pickupPhoneNumbers.length() - 3));
                        customerPhoneNumber.setText("*******" + customerPhoneNumbers.substring(customerPhoneNumbers.length() - 3));

                        if (orderDetailsResponse.getData().getPickupNotes() != null && orderDetailsResponse.getData().getPickupNotes().isEmpty()) {
                            deliveryInstruction.setVisibility(View.GONE);
                        } else {
                            deliveryInstruction.setVisibility(View.VISIBLE);
                            deliveryInstructionText.setText(orderDetailsResponse.getData().getPickupNotes());
                        }

                        if (orderDetailsResponse.getData().getDeliverNotes() != null && orderDetailsResponse.getData().getDeliverNotes().isEmpty()) {
                            pickupInstruction.setVisibility(View.GONE);
                        } else {
                            pickupInstruction.setVisibility(View.VISIBLE);
                            pickupInstructionText.setText(orderDetailsResponse.getData().getPickupNotes());
                        }
                        this.pickupPhoneNumber = String.valueOf(orderDetailsResponse.getData().getDelPn());
                        this.customerPhoneNumber = String.valueOf(orderDetailsResponse.getData().getPickupPn());
                    } else {
                        pharmacyLandmark.setText(orderDetailsResponse.getData().getPickupLndmrk());
                        customerLandmark.setText(orderDetailsResponse.getData().getDeliverLandmark());
                        pharmacyAddId.setText(orderDetailsResponse.getData().getPickupAddId());
                        customerName.setText(orderDetailsResponse.getData().getDelAddId());
                        pharmacyAddress.setText(pickupAddress);
                        customerAddress.setText(customerAddresss);
                        String pickupPhoneNumbers = String.valueOf(orderDetailsResponse.getData().getPickupPn());
                        String customerPhoneNumbers = String.valueOf(orderDetailsResponse.getData().getDelPn());
                        pickupPhoneNumber.setText("*******" + pickupPhoneNumbers.substring(pickupPhoneNumbers.length() - 3));
                        customerPhoneNumber.setText("*******" + customerPhoneNumbers.substring(customerPhoneNumbers.length() - 3));
                        if (orderDetailsResponse.getData().getPickupNotes() != null && orderDetailsResponse.getData().getPickupNotes().isEmpty()) {
                            pickupInstruction.setVisibility(View.GONE);
                        } else {
                            pickupInstruction.setVisibility(View.VISIBLE);
                            pickupInstructionText.setText(orderDetailsResponse.getData().getPickupNotes());
                        }

                        if (orderDetailsResponse.getData().getDeliverNotes() != null && orderDetailsResponse.getData().getDeliverNotes().isEmpty()) {
                            deliveryInstruction.setVisibility(View.GONE);
                        } else {
                            deliveryInstruction.setVisibility(View.VISIBLE);
                            deliveryInstructionText.setText(orderDetailsResponse.getData().getPickupNotes());
                        }
                        this.pickupPhoneNumber = String.valueOf(orderDetailsResponse.getData().getPickupPn());
                        this.customerPhoneNumber = String.valueOf(orderDetailsResponse.getData().getDelPn());
                    }

                    orderListLayout.removeAllViews();
                    if (orderDetailsResponse.getData().getItems() != null && orderDetailsResponse.getData().getItems().size() > 0) {
                        TextView itemsCount = (TextView) findViewById(R.id.items_count);
                        itemsCount.setText(orderDetailsResponse.getData().getItems().size() + " " + "Items");
                        for (int i = 0; i < orderDetailsResponse.getData().getItems().size(); i++) {
                            LayoutInflater layoutInflater = LayoutInflater.from(this);
                            LinearLayout layout = (LinearLayout) layoutInflater.inflate(R.layout.view_order_list, null, false);

                            TextView orderName = layout.findViewById(R.id.order_name);
                            TextView orderCount = layout.findViewById(R.id.order_count);
                            TextView orderPrice = layout.findViewById(R.id.order_price);

                            orderName.setText(orderDetailsResponse.getData().getItems().get(i).getItemname());
                            orderCount.setText(orderDetailsResponse.getData().getItems().get(i).getItemquantity());
                            orderPrice.setText(String.valueOf(orderDetailsResponse.getData().getItems().get(i).getItemprice()));

                            orderListLayout.addView(layout);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("NewOrderActivity :: :: :: :::::::::::::::::::::::::::::::::::  " + e.getMessage());
            }
        }
    }

    @Override
    public void onFialureOrderDetailsApiCall() {

    }

    @Override
    public void onFialureMessage(String message) {

    }
}
