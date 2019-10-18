package com.apollo.epos.activity;

import android.Manifest;
import android.animation.LayoutTransition;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.Bindable;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.ahmadrosid.lib.drawroutemap.DirectionApiCallback;
import com.ahmadrosid.lib.drawroutemap.DrawRouteMaps;
import com.ahmadrosid.lib.drawroutemap.TaskLoadedCallback;
import com.apollo.epos.R;
import com.apollo.epos.adapter.CustomReasonAdapter;
import com.apollo.epos.dialog.DialogManager;
import com.apollo.epos.listeners.DialogMangerCallback;
import com.apollo.epos.model.OrderItemModel;
import com.apollo.epos.service.GPSLocationService;
import com.apollo.epos.service.NetworkUtils;
import com.apollo.epos.utils.ActivityUtils;
import com.google.android.gms.maps.model.LatLng;
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

import java.lang.annotation.Annotation;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class NewOrderActivity extends BaseActivity implements DirectionApiCallback, TaskLoadedCallback, Connectable, Disconnectable, BindableInterface {
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
    private final int REQ_LOC_PERMISSION = 5002;

    private float firstTime = 0;
    private float secondTime = 0;

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
            gotoMapActivity();
        });

        getCurrentLocation();
    }


    private void getCurrentLocation() {
        GPSLocationService gps = new GPSLocationService(this);
        if (gps.canGetLocation()) {

            LatLng origin = new LatLng(gps.getLatitude(), gps.getLongitude());

            LatLng destination = new LatLng(17.4410197, 78.3788463);
            LatLng other = new LatLng(17.4411128, 78.3827845);

            DrawRouteMaps.getInstance(this, this, this::onTaskDone)
                    .draw(origin, destination, null, 0);
            DrawRouteMaps.getInstance(this, this, this::onTaskDone)
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

    private boolean checkForLocPermission() {
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            return ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    private void requestForLocPermission(final int reqCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_FINE_LOCATION)) {

            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            DialogManager.showSingleBtnPopup(this, new DialogMangerCallback() {
                @Override
                public void onOkClick(View v) {
                    ActivityCompat.requestPermissions(NewOrderActivity.this,
                            new String[]{ACCESS_FINE_LOCATION},
                            reqCode);
                }

                @Override
                public void onCancelClick(View view) {

                }
            }, getString(R.string.app_name), getString(R.string.locationPermissionMsg), getString(R.string.ok));
        } else {

            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(this,
                    new String[]{ACCESS_FINE_LOCATION},
                    reqCode);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQ_LOC_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    gotoMapActivity();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    DialogManager.showToast(this, getString(R.string.noAccessTo));
                }
            }
            break;
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void gotoMapActivity() {
        if (checkForLocPermission()) {
            if (checkGPSOn(this)) {
                if (NetworkUtils.isNetworkConnected(NewOrderActivity.this)) {
                    Intent intent = new Intent(this, MapViewActivity.class);
                    startActivity(intent);
                }else {
                    DialogManager.showToast(NewOrderActivity.this, getString(R.string.no_interent));
                }
            } else {
                showGPSDisabledAlertToUser(this);
            }
        } else {
            requestForLocPermission(REQ_LOC_PERMISSION);
            return;
        }
    }

    public boolean checkGPSOn(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }

    public void showGPSDisabledAlertToUser(Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(getString(R.string.alert));
        alertDialogBuilder.setMessage(getString(R.string.permission_gps_bogy))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.open_settings),
                        (dialog, id) -> {
                            Intent callGPSSettingIntent = new Intent(
                                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(callGPSSettingIntent);
                        });
        alertDialogBuilder.setNegativeButton(getString(R.string.cancel),
                (dialog, id) -> dialog.cancel());
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
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
    }

    @Override
    public void onBind(NetworkStatus networkStatus) {
        if (!networkStatus.isAvailable()) {
            onDisconnect();
        }
    }

    @Override
    public void onTaskDone(Object... values) {

    }

    @Override
    public void onConnect() {
        getCurrentLocation();
        ActivityUtils.hideDialog();
    }

    @Override
    public void onDisconnect() {
        ActivityUtils.showDialog(NewOrderActivity.this, "Getting Location");
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
