package com.apollo.epos.fragment.dashboard;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.apollo.epos.R;
import com.apollo.epos.activity.LoginActivity;
import com.apollo.epos.activity.NavigationActivity;
import com.apollo.epos.activity.NewOrderActivity;
import com.apollo.epos.activity.SplashScreen;
import com.apollo.epos.fragment.cancelorderitem.CancelOrderItemFragment;
import com.apollo.epos.fragment.neworder.NewOrderFragment;
import com.apollo.epos.utils.XYMarkerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DashboardFragment extends Fragment implements OnChartValueSelectedListener, DashboardView {
    private Activity mActivity;
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
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    final AnimationDrawable drawable = new AnimationDrawable();
    final Handler handler = new Handler();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        Glide.with(mActivity)
                .load(getResources().getDrawable(R.drawable.userimage))
                .apply(RequestOptions.circleCropTransform())
                .into(userImage);

        sw.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                userStatus.setText("Online");
//                Toast.makeText(mActivity, "ON", Toast.LENGTH_SHORT).show();
            } else {
                userStatus.setText("Offline");
//                Toast.makeText(mActivity, "OFF", Toast.LENGTH_SHORT).show();
            }
        });

        Animation animSlideFromBottom = AnimationUtils.loadAnimation(mActivity, R.anim.slide_from_bottom);
        ordersInformationLayout.clearAnimation();
        ordersInformationLayout.startAnimation(animSlideFromBottom);

        dashboardView = this;
        dashboardView.setGraphData();

//        Animation anim = new AlphaAnimation(0.0f, 1.0f);
//        anim.setDuration(50); //You can manage the blinking time with this parameter
//        anim.setStartOffset(20);
//        anim.setRepeatMode(Animation.REVERSE);
//        anim.setRepeatCount(Animation.INFINITE);
//        newOrderLayout.startAnimation(anim);

        //textColor
        ObjectAnimator anim = ObjectAnimator.ofInt(newOrderLayout, "backgroundColor",
                mActivity.getResources().getColor(R.color.colorPrimary),
                mActivity.getResources().getColor(R.color.btn_color));
        anim.setDuration(1000);
        anim.setEvaluator(new ArgbEvaluator());
        anim.setRepeatMode(ValueAnimator.REVERSE);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.start();

        newOrderLayout.setOnClickListener(v -> {
            ((NavigationActivity) Objects.requireNonNull(mActivity)).showFragment(new NewOrderFragment(), R.string.menu_take_order);
            ((NavigationActivity) Objects.requireNonNull(mActivity)).updateSelection(-1);
//            Intent mainIntent = new Intent(mActivity, NewOrderActivity.class);
//            startActivityForResult(mainIntent, 1);
//            mActivity.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        });

        setData(5, 15, 0, 12, 8, 22, 10, 45, 3 , 15, 0 , 28, 5 ,15);
        totalOrdersVal.setText("183");
        deliveredOrdersVal.setText("152");
        cancelledOrdersVal.setText("31");
        codReceivedVal.setText("2050");
        codPendingVal.setText("560");
        salesGeneratedVal.setText("24 Orders");
        travelledDistanceVal.setText("110.4 KM");

//        setHasOptionsMenu(true);
//        sharedPref = new SharedPreferenceUtils(mActivity);
//        networkCall = new NetworkCall(mActivity);
//        appIcon.setVisibility(View.GONE);
//        bottomLayout.setVisibility(View.VISIBLE);
//        mReceiver = new MyResultReceiver(new Handler());
//        mReceiver.setReceiver(ChangePasswordFragment.this);
//        if (Constants.IS_REMOTE_SUPPORT_REQUIRED) {
//            if (Boolean.parseBoolean(sharedPref.getKeyValue(Constants.IS_MANUAL_ONLINE_MODE))) {
//                try {
//                    ChatApplication app = (ChatApplication) Objects.requireNonNull(mActivity).getApplication();
//                    mSocket = app.getSocket();
//                    mSocket.on("notification-count-response", onNotificationCountResponse);
//                    JSONObject obj1 = new JSONObject();
//                    obj1.put("userId", Integer.parseInt(sharedPref.getKeyValue(Constants.CHAT_LOGIN_USER_ID)));
//                    mSocket.emit("lastSeenUpdate", obj1);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                mRegistrationBroadcastReceiver = new BroadcastReceiver() {
//                    @SuppressLint("NewApi")
//                    @Override
//                    public void onReceive(Context context, Intent intent) {
//                        if (Objects.equals(intent.getAction(), Constants.PUSH_NOTIFICATION)) {
//                            handleNotificationMethod();
//                        }
//                    }
//                };
//            }
//        }
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

    private void setData(float monCan, float monDel, float tueCan, float tueDel, float wedCan, float wedDel, float thuCan, float thuDel,
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
    void onFirstOrdersClick(){
        if(isFirstOrdersClicked)
            return;

        Animation animSlideFromBottom = AnimationUtils.loadAnimation(mActivity, R.anim.slide_from_bottom);
        ordersInformationLayout.clearAnimation();
        ordersInformationLayout.startAnimation(animSlideFromBottom);

        setData(3, 10, 5, 22, 0, 20, 10, 35, 2 , 20, 8 , 40, 0 ,10);
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
    void onSecondOrdersClick(){
        if(isSecondOrdersClicked)
            return;

        Animation animSlideFromBottom = AnimationUtils.loadAnimation(mActivity, R.anim.slide_from_bottom);
        ordersInformationLayout.clearAnimation();
        ordersInformationLayout.startAnimation(animSlideFromBottom);

        setData(2, 10, 6, 26, 10, 29, 0, 10, 5 , 25, 18 , 30, 6 ,15);
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
    void onThirdOrdersClick(){
        if(isThirdOrdersClicked)
            return;

        Animation animSlideFromBottom = AnimationUtils.loadAnimation(mActivity, R.anim.slide_from_bottom);
        ordersInformationLayout.clearAnimation();
        ordersInformationLayout.startAnimation(animSlideFromBottom);

        setData(5, 20, 0, 10, 8, 30, 2, 15, 3 , 12, 8 , 26, 2 ,30);
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
    void onFourthOrdersClick(){
        if(isFourthOrdersClicked)
            return;

        Animation animSlideFromBottom = AnimationUtils.loadAnimation(mActivity, R.anim.slide_from_bottom);
        ordersInformationLayout.clearAnimation();
        ordersInformationLayout.startAnimation(animSlideFromBottom);

        firstOrdersList.setBackgroundColor(mActivity.getResources().getColor(R.color.colorWhite));
        secondOrdersList.setBackgroundColor(mActivity.getResources().getColor(R.color.colorWhite));
        thirdOrdersList.setBackgroundColor(mActivity.getResources().getColor(R.color.colorWhite));
        fourthOrdersList.setBackgroundColor(mActivity.getResources().getColor(R.color.btn_color));

        setData(5, 15, 0, 12, 8, 22, 10, 45, 3 , 15, 0 , 28, 5 ,15);
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
}