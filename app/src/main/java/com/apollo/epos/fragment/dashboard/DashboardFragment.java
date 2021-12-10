package com.apollo.epos.fragment.dashboard;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.AnimationDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.apollo.epos.R;
import com.apollo.epos.activity.NavigationActivity;
import com.apollo.epos.activity.NewOrderActivity;
import com.apollo.epos.base.BaseFragment;
import com.apollo.epos.databinding.FragmentDashboardBinding;
import com.apollo.epos.model.GetRiderProfileResponse;
import com.apollo.epos.utils.ActivityUtils;
import com.apollo.epos.utils.XYMarkerView;
import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.StackedValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;
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
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DashboardFragment extends BaseFragment implements DashboardFragmentCallback, OnChartValueSelectedListener, DashboardView, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        ResultCallback<LocationSettingsResult> {
    private Activity mActivity;
    private FragmentDashboardBinding dashboardBinding;
    @BindView(R.id.user_image)
    ImageView userImage;
    @BindView(R.id.switch1)
    Switch sw;
    @BindView(R.id.barChart)
    protected BarChart mChart;
    protected RectF mOnValueSelectedRectF = new RectF();
    private DashboardViewModel dashboardViewModel;
    private DashboardView dashboardView;

    @BindColor(R.color.dashboard_pending_text_color)
    protected int cancelledOrdersColour;
    @BindColor(R.color.theme_end_color)
    protected int deliveredOrdersColour;
    @BindColor(R.color.btn_color)
    protected int totalOrdersColour;
    @BindView(R.id.new_order_layout)
    protected LinearLayout newOrderLayout;

    @BindView(R.id.orders_information_layout)
    protected LinearLayout ordersInformationLayout;

    @BindView(R.id.first_orders_list)
    protected TextView firstOrdersList;
    @BindView(R.id.second_orders_list)
    protected TextView secondOrdersList;
    @BindView(R.id.third_orders_list)
    protected TextView thirdOrdersList;
    @BindView(R.id.fourth_orders_list)
    protected TextView fourthOrdersList;
    private boolean isFirstOrdersClicked = false;
    private boolean isSecondOrdersClicked = false;
    private boolean isThirdOrdersClicked = false;
    private boolean isFourthOrdersClicked = false;

    @BindView(R.id.total_orders_val)
    protected TextView totalOrdersVal;
    @BindView(R.id.delivered_orders_val)
    protected TextView deliveredOrdersVal;
    @BindView(R.id.cancelled_orders_val)
    protected TextView cancelledOrdersVal;
    @BindView(R.id.cod_received_val)
    protected TextView codReceivedVal;
    @BindView(R.id.cod_pending_val)
    protected TextView codPendingVal;
    @BindView(R.id.sales_generated_val)
    protected TextView salesGeneratedVal;
    @BindView(R.id.travelled_distance_val)
    protected TextView travelledDistanceVal;

    @BindView(R.id.user_status)
    protected TextView userStatus;
    private static final int ACTIVITY_CHANGE = 10;

    private static LinearLayout newOrderLayoutView;


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;


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

    private static TextView timeView;


    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dashboardBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false);
        return dashboardBinding.getRoot();
    }

    final AnimationDrawable drawable = new AnimationDrawable();
    final Handler handler = new Handler();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        newOrderLayoutView = view.findViewById(R.id.new_order_layout);
        timeView = view.findViewById(R.id.time);
        if (getSessionManager().getNotificationStatus()) {
            dashboardBinding.newOrderLayout.setVisibility(View.VISIBLE);
            dashboardBinding.time.setText(getSessionManager().getNotificationArrivedTime());
        } else {
            dashboardBinding.newOrderLayout.setVisibility(View.GONE);
        }

        if (getSessionManager().getRiderProfileResponse() != null)
            onSuccessGetProfileDetailsApi(getSessionManager().getRiderProfileResponse());
        else
            new DashboardFragmentController(getContext(), this).getRiderProfileDetailsApi(getSessionManager().getLoginToken());

        if (getSessionManager().getRiderActiveStatus().equals("Offline")) {
            userStatus.setText("Offline");
            sw.setChecked(false);
            new DashboardFragmentController(getContext(), this).riderUpdateStauts(getSessionManager().getLoginToken(), "Offline");

        } else {
            userStatus.setText("Online");
            sw.setChecked(true);
            new DashboardFragmentController(getContext(), this).riderUpdateStauts(getSessionManager().getLoginToken(), "Online");
        }

        sw.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                userStatus.setText("Online");
                new DashboardFragmentController(getContext(), this).riderUpdateStauts(getSessionManager().getLoginToken(), "Online");
            } else {
                userStatus.setText("Offline");
                new DashboardFragmentController(getContext(), this).riderUpdateStauts(getSessionManager().getLoginToken(), "Offline");
            }
        });

        Animation animSlideFromBottom = AnimationUtils.loadAnimation(mActivity, R.anim.slide_from_bottom);
        ordersInformationLayout.clearAnimation();
        ordersInformationLayout.startAnimation(animSlideFromBottom);

        dashboardView = this;
        dashboardView.setGraphData();
        //textColor
        ObjectAnimator anim = ObjectAnimator.ofInt(newOrderLayout, "backgroundColor",
                mActivity.getResources().getColor(R.color.colorPrimary),
                mActivity.getResources().getColor(R.color.dashboard_pending_text_color));
        anim.setDuration(1000);
        anim.setEvaluator(new ArgbEvaluator());
        anim.setRepeatMode(ValueAnimator.REVERSE);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.start();

        setData(5, 15, 0, 12, 8, 22, 10, 45, 3, 15, 0, 28, 5, 15);
        totalOrdersVal.setText("183");
        deliveredOrdersVal.setText("152");
        cancelledOrdersVal.setText("31");
        codReceivedVal.setText("2050");
        codPendingVal.setText("560");
        salesGeneratedVal.setText("24 Orders");
        travelledDistanceVal.setText("110.4 KM");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";

        // Update values using data stored in the Bundle.
        updateValuesFromBundle(savedInstanceState);

        // Kick off the process of building the GoogleApiClient, LocationRequest, and
        // LocationSettingsRequest objects.

        //show error dialog if GoolglePlayServices not available
        if (ActivityUtils.checkPlayServices(mActivity)) {
            buildGoogleApiClient();
        }
        createLocationRequest();
        GoogleClientBuild();
        buildLocationSettingsRequest();

        newOrderLayout.setOnClickListener(v -> {

            NavigationActivity.getInstance().selectItem(1);
//            startUpdatesButtonHandler(newOrderLayout);

        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permission)) {
                //denied
                Log.e("denied", permission);
            } else {
                if (ActivityCompat.checkSelfPermission(mActivity, permission) == PackageManager.PERMISSION_GRANTED) {
                    //allowed
                    Log.e("allowed", permission);

                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        // permission was granted, yay! Do the
                        // location-related task you need to do.
                        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

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

        Permissions.check(mActivity, permissions, null, null, new PermissionHandler() {
            @Override
            public void onGranted() {
//                Toast.makeText(mActivity, "Location granted.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                Toast.makeText(mActivity, "Location denied.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private synchronized void GoogleClientBuild() {
        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .enableAutoManage(getActivity(), 1, this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }

//        mGoogleApiClient = new GoogleApiClient.Builder(mActivity).addApi(LocationServices.API).addConnectionCallbacks(this).addApi(AppIndex.API).addApi(AppIndex.API).addOnConnectionFailedListener(this).build();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;
        RectF bounds = mOnValueSelectedRectF;
        mChart.getBarBounds((BarEntry) e, bounds);
        MPPointF position = mChart.getPosition(e, YAxis.AxisDependency.LEFT);

        Log.i("bounds", bounds.toString());
        Log.i("position", position.toString());

        Log.i("x-index",
                "low: " + mChart.getLowestVisibleX() + ", high: "
                        + mChart.getHighestVisibleX());

        MPPointF.recycleInstance(position);
    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void setGraphData() {
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(false);
        mChart.getDescription().setEnabled(false);
        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(50);
        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);
        mChart.setScaleEnabled(false);
        mChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);

        ArrayList<String> xVals = new ArrayList<>();
        xVals.add(getResources().getString(R.string.label_monday));
        xVals.add(getResources().getString(R.string.label_tuesday));
        xVals.add(getResources().getString(R.string.label_wednesday));
        xVals.add(getResources().getString(R.string.label_thursday));
        xVals.add(getResources().getString(R.string.label_friday));
        xVals.add(getResources().getString(R.string.label_saturday));
        xVals.add(getResources().getString(R.string.label_sunday));
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        IAxisValueFormatter xAxisFormatter = new IndexAxisValueFormatter(xVals);
        xAxis.setValueFormatter(xAxisFormatter);
        mChart.getAxisRight().setEnabled(false);
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setLabelCount(5, false);
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(30f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.NONE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(7f);

        XYMarkerView mv = new XYMarkerView(mActivity, xAxisFormatter);
        mv.setChartView(mChart); // For bounds control
        mChart.getLegend().setEnabled(false);   // Hide the legend
        mChart.setMarker(mv); // Set the marker to the chart
    }

    private void setData(float monCan, float monDel, float tueCan, float tueDel, float wedCan,
                         float wedDel, float thuCan, float thuDel,
                         float friCan, float friDel, float satCan, float satDel, float sunCan, float sunDel) {
        mChart.invalidate();
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
//        yVals1.add(new BarEntry(0, (int) monVal));

        yVals1.add(new BarEntry(0, new float[]{monCan, monDel}, ""));
        yVals1.add(new BarEntry(1, new float[]{tueCan, tueDel}, ""));
        yVals1.add(new BarEntry(2, new float[]{wedCan, wedDel}, ""));
        yVals1.add(new BarEntry(3, new float[]{thuCan, thuDel}, ""));
        yVals1.add(new BarEntry(4, new float[]{friCan, friDel}, ""));
        yVals1.add(new BarEntry(5, new float[]{satCan, satDel}, ""));
        yVals1.add(new BarEntry(6, new float[]{sunCan, sunDel}, ""));

        BarDataSet set1 = new BarDataSet(yVals1, "");
//        set1.setDrawIcons(false);
        set1.setColors(getColors());
//        set1.setColors(deliveredOrdersColour);

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
//        data.setValueTextSize(8f);
//             data.setValueTypeface(mTfLight);
        data.setBarWidth(0.5f);
        data.setValueFormatter(new StackedValueFormatter(false, "", 1));
        data.setValueTextColor(Color.WHITE);

        mChart.setData(data);
        mChart.animateY(2000);

//        for (IBarDataSet iSet : dataSets) {
//            BarDataSet set = (BarDataSet) iSet;
//            set.setDrawValues(!set.isDrawValuesEnabled());
//        }

        for (IBarDataSet set : dataSets)
            set.setDrawValues(!set.isDrawValuesEnabled());
        //    }
    }

    private int[] getColors() {
        int[] colors = {Color.rgb(255, 1, 1), Color.rgb(0, 194, 195)};
        System.arraycopy(colors, 0, colors, 0, 2);
        return colors;
    }

    @OnClick(R.id.first_orders_list)
    void onFirstOrdersClick() {
        if (isFirstOrdersClicked)
            return;

        Animation animSlideFromBottom = AnimationUtils.loadAnimation(mActivity, R.anim.slide_from_bottom);
        ordersInformationLayout.clearAnimation();
        ordersInformationLayout.startAnimation(animSlideFromBottom);

        setData(3, 10, 5, 22, 0, 20, 10, 35, 2, 20, 8, 40, 0, 10);
        totalOrdersVal.setText("172");
        deliveredOrdersVal.setText("147");
        cancelledOrdersVal.setText("25");
        codReceivedVal.setText("1580");
        codPendingVal.setText("210");
        salesGeneratedVal.setText("36 Orders");
        travelledDistanceVal.setText("153.8 KM");

        firstOrdersList.setBackgroundColor(mActivity.getResources().getColor(R.color.btn_color));
        secondOrdersList.setBackgroundColor(mActivity.getResources().getColor(R.color.colorWhite));
        thirdOrdersList.setBackgroundColor(mActivity.getResources().getColor(R.color.colorWhite));
        fourthOrdersList.setBackgroundColor(mActivity.getResources().getColor(R.color.colorWhite));

        isFirstOrdersClicked = true;
        isSecondOrdersClicked = false;
        isThirdOrdersClicked = false;
        isFourthOrdersClicked = false;
    }

    @OnClick(R.id.second_orders_list)
    void onSecondOrdersClick() {
        if (isSecondOrdersClicked)
            return;

        Animation animSlideFromBottom = AnimationUtils.loadAnimation(mActivity, R.anim.slide_from_bottom);
        ordersInformationLayout.clearAnimation();
        ordersInformationLayout.startAnimation(animSlideFromBottom);

        setData(2, 10, 6, 26, 10, 29, 0, 10, 5, 25, 18, 30, 6, 15);
        totalOrdersVal.setText("192");
        deliveredOrdersVal.setText("145");
        cancelledOrdersVal.setText("49");
        codReceivedVal.setText("3270");
        codPendingVal.setText("845");
        salesGeneratedVal.setText("42 Orders");
        travelledDistanceVal.setText("124.6 KM");

        firstOrdersList.setBackgroundColor(mActivity.getResources().getColor(R.color.colorWhite));
        secondOrdersList.setBackgroundColor(mActivity.getResources().getColor(R.color.btn_color));
        thirdOrdersList.setBackgroundColor(mActivity.getResources().getColor(R.color.colorWhite));
        fourthOrdersList.setBackgroundColor(mActivity.getResources().getColor(R.color.colorWhite));

        isFirstOrdersClicked = false;
        isSecondOrdersClicked = true;
        isThirdOrdersClicked = false;
        isFourthOrdersClicked = false;
    }

    @OnClick(R.id.third_orders_list)
    void onThirdOrdersClick() {
        if (isThirdOrdersClicked)
            return;

        Animation animSlideFromBottom = AnimationUtils.loadAnimation(mActivity, R.anim.slide_from_bottom);
        ordersInformationLayout.clearAnimation();
        ordersInformationLayout.startAnimation(animSlideFromBottom);

        setData(5, 20, 0, 10, 8, 30, 2, 15, 3, 12, 8, 26, 2, 30);
        totalOrdersVal.setText("171");
        deliveredOrdersVal.setText("143");
        cancelledOrdersVal.setText("28");
        codReceivedVal.setText("2930");
        codPendingVal.setText("675");
        salesGeneratedVal.setText("33 Orders");
        travelledDistanceVal.setText("99.3 KM");

        firstOrdersList.setBackgroundColor(mActivity.getResources().getColor(R.color.colorWhite));
        secondOrdersList.setBackgroundColor(mActivity.getResources().getColor(R.color.colorWhite));
        thirdOrdersList.setBackgroundColor(mActivity.getResources().getColor(R.color.btn_color));
        fourthOrdersList.setBackgroundColor(mActivity.getResources().getColor(R.color.colorWhite));

        isFirstOrdersClicked = false;
        isSecondOrdersClicked = false;
        isThirdOrdersClicked = true;
        isFourthOrdersClicked = false;
    }

    @OnClick(R.id.fourth_orders_list)
    void onFourthOrdersClick() {
        if (isFourthOrdersClicked)
            return;

        Animation animSlideFromBottom = AnimationUtils.loadAnimation(mActivity, R.anim.slide_from_bottom);
        ordersInformationLayout.clearAnimation();
        ordersInformationLayout.startAnimation(animSlideFromBottom);

        firstOrdersList.setBackgroundColor(mActivity.getResources().getColor(R.color.colorWhite));
        secondOrdersList.setBackgroundColor(mActivity.getResources().getColor(R.color.colorWhite));
        thirdOrdersList.setBackgroundColor(mActivity.getResources().getColor(R.color.colorWhite));
        fourthOrdersList.setBackgroundColor(mActivity.getResources().getColor(R.color.btn_color));

        setData(5, 15, 0, 12, 8, 22, 10, 45, 3, 15, 0, 28, 5, 15);
        totalOrdersVal.setText("183");
        deliveredOrdersVal.setText("152");
        cancelledOrdersVal.setText("31");
        codReceivedVal.setText("2050");
        codPendingVal.setText("560");
        salesGeneratedVal.setText("24 Orders");
        travelledDistanceVal.setText("110.4 KM");

        isFirstOrdersClicked = false;
        isSecondOrdersClicked = false;
        isThirdOrdersClicked = false;
        isFourthOrdersClicked = true;
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == ACTIVITY_CHANGE) {
//            if (data != null) {
//                boolean isOrderCompleted = Boolean.parseBoolean(data.getStringExtra("OrderCompleted"));
//                if (isOrderCompleted) {
//                    Log.e("DashboardFrag", "Order Completed Successful");
//                }
//            }
//        }
//    }

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
    protected synchronized void buildGoogleApiClient() {
//        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
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
                    status.startResolutionForResult(mActivity, REQUEST_CHECK_SETTINGS);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(TAG, "User agreed to make required location settings changes.");
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(TAG, "User chose not to make required location settings changes.");
                        break;
                }
                break;

            case ACTIVITY_CHANGE:
                if (data != null) {
                    boolean isOrderCompleted = Boolean.parseBoolean(data.getStringExtra("OrderCompleted"));
                    if (isOrderCompleted) {
                        Log.e("DashboardFrag", "Order Completed Successful");
                    }
                }
                break;
        }
    }

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
        int checkSelf = ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION);
        if (checkSelf != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.ACCESS_FINE_LOCATION)) {
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
                Intent intent = new Intent(mActivity, NewOrderActivity.class);
                startActivityForResult(intent, ACTIVITY_CHANGE);
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
    }

    @Override
    public void onResume() {
        super.onResume();
        // Within {@code onPause()}, we pause location updates, but leave the
        // connection to GoogleApiClient intact.  Here, we resume receiving
        // location updates if the user has requested them.
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
        }
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
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(KEY_REQUESTING_LOCATION_UPDATES, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(KEY_LOCATION, mCurrentLocation);
        savedInstanceState.putString(KEY_LAST_UPDATED_TIME_STRING, mLastUpdateTime);
    }

    @Override
    public void onSuccessGetProfileDetailsApi(GetRiderProfileResponse getRiderProfileResponse) {
        if (getRiderProfileResponse != null) {
            getSessionManager().setRiderProfileDetails(getRiderProfileResponse);
            NavigationActivity.getInstance().setProfileData();
            if (getRiderProfileResponse != null && getRiderProfileResponse.getData() != null && getRiderProfileResponse.getData().getPic() != null && getRiderProfileResponse.getData().getPic().size() > 0)
                Glide.with(getContext()).load(getSessionManager().getrRiderIconUrl()).circleCrop().error(R.drawable.apollo_app_logo).into(dashboardBinding.userImage);
            dashboardBinding.riderName.setText(getRiderProfileResponse.getData().getFirstName() + " " + getRiderProfileResponse.getData().getLastName());
            dashboardBinding.riderPhoneNumber.setText(getRiderProfileResponse.getData().getPhone());
        }
    }

    @Override
    public void onFialureMessage(String message) {
        Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
    }

    public static void newOrderViewVisibility(boolean show) {
        if (show) {
            newOrderLayoutView.setVisibility(View.VISIBLE);
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm");
            String strDate = mdformat.format(calendar.getTime());
            timeView.setText("Today," + strDate);

        } else {
            newOrderLayoutView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getSessionManager().setNotificationStatus(false);
    }
}