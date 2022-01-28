package com.apollo.epos.activity.navigation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.apollo.epos.BuildConfig;
import com.apollo.epos.R;
import com.apollo.epos.activity.BaseActivity;
import com.apollo.epos.activity.CartActivity;
import com.apollo.epos.activity.login.LoginActivity;
import com.apollo.epos.adapter.NavigationDrawerAdapter;
import com.apollo.epos.databinding.DialogAlertCustomBinding;
import com.apollo.epos.databinding.DialogPermissionDeniedBinding;
import com.apollo.epos.db.SessionManager;
import com.apollo.epos.fragment.changepassword.ChangePasswordFragment;
import com.apollo.epos.fragment.dashboard.DashboardFragment;
import com.apollo.epos.fragment.help.HelpFragment;
import com.apollo.epos.fragment.myorders.MyOrdersFragment;
import com.apollo.epos.fragment.myorders.MyOrdersFragmentCallback;
import com.apollo.epos.fragment.notifications.NotificationsFragment;
import com.apollo.epos.fragment.profile.ProfileFragment;
import com.apollo.epos.fragment.takeneworder.TakeNewOrderFragment;
import com.apollo.epos.model.NavDrawerModel;
import com.apollo.epos.service.BatteryLevelLocationService;
import com.apollo.epos.service.FloatingTouchService;
import com.apollo.epos.utils.CommonUtils;
import com.apollo.epos.utils.FragmentUtils;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.novoda.merlin.Merlin;
import com.orhanobut.hawk.Hawk;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static com.apollo.epos.utils.AppConstants.LAST_ACTIVITY;
import static com.apollo.epos.utils.FragmentUtils.TRANSITION_FROM_LEFT_TO_RIGHT;

public class NavigationActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, NavigationActivityCallback {
    Toolbar toolbar;
    LinearLayout linearLayout;
    ActionBarDrawerToggle toggle;
    private final String TAG = "MainActivity";
    private int selectedItemPos = -1;
    private String mCurrentFrag;
    private static TextView cartCount, notificationText;
    private static NavigationActivity instance;
    private ViewGroup header;
    private LocationManager locationManager;
    private final static int GPS_REQUEST_CODE = 2;
    private boolean isLanchedByPushNotification;
    private boolean isFromNotificaionIcon;
    private MyOrdersFragmentCallback myOrdersFragmentCallback;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, NavigationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    public void setMyOrdersFragmentCallback(MyOrdersFragmentCallback myOrdersFragmentCallback) {
        this.myOrdersFragmentCallback = myOrdersFragmentCallback;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        instance = this;
        if (getIntent() != null) {
            isLanchedByPushNotification = (Boolean) getIntent().getBooleanExtra("isPushNotfication", false);
            isFromNotificaionIcon = (Boolean) getIntent().getBooleanExtra("is_from_notification", false);
        }
        LinearLayout locationDeniedLayout = (LinearLayout) findViewById(R.id.location_denied);
        locationDeniedLayout.setVisibility(View.GONE);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (isGpsEnambled()) {
            checkLocationPermission();
        } else {
            buildAlertMessageNoGps();
        }
        setUp();
    }

    private void setUp() {
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        initCustomNavigationDrawer();
        handleAssistiveTouchWindow();
    }

    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 10);
            return;
        }
        startService();
    }

    private boolean isGpsEnambled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void buildAlertMessageNoGps() {
        new AlertDialog.Builder(this)
                .setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), GPS_REQUEST_CODE))
                .setNegativeButton("No", (dialog, id) -> {
                    dialog.dismiss();
                    dialog.cancel();
                    Dialog dialog1 = new Dialog(this, R.style.fadeinandoutcustomDialog);
                    DialogPermissionDeniedBinding permissionDeniedBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_permission_denied, null, false);
                    dialog1.setContentView(permissionDeniedBinding.getRoot());
                    dialog1.setCancelable(false);
                    permissionDeniedBinding.locationPermissionDeniedText.setText(R.string.label_gps_enable_to_access_application);
                    permissionDeniedBinding.locationPermissionBtn.setText(R.string.gps_enable);
                    permissionDeniedBinding.locationPermissionBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isGpsEnambled()) {
                                checkLocationPermission();
                            } else {
                                buildAlertMessageNoGps();
                            }
                            dialog1.dismiss();
                        }
                    });
                    dialog1.show();

                }).create().show();
    }

    public void startService() {
        startService(new Intent(getBaseContext(), BatteryLevelLocationService.class));
    }

    // Method to stop the BatteryLevelLocationService
    public void stopBatteryLevelLocationService() {
        stopService(new Intent(getBaseContext(), BatteryLevelLocationService.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (isGpsEnambled()) {
                    checkLocationPermission();
                } else {
                    buildAlertMessageNoGps();
                }
            } else {
                Dialog dialog1 = new Dialog(this, R.style.fadeinandoutcustomDialog);
                DialogPermissionDeniedBinding permissionDeniedBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_permission_denied, null, false);
                dialog1.setContentView(permissionDeniedBinding.getRoot());
                dialog1.setCancelable(false);
                permissionDeniedBinding.locationPermissionDeniedText.setText(R.string.label_location_permission_must_be_required_to_access_application);
                permissionDeniedBinding.locationPermissionBtn.setText(R.string.label_location_permission);
                permissionDeniedBinding.locationPermissionBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isGpsEnambled()) {
                            checkLocationPermission();
                        } else {
                            buildAlertMessageNoGps();
                        }
                        dialog1.dismiss();
                    }
                });
                dialog1.show();
            }
        }
    }


    @Override
    protected Merlin createMerlin() {
        return new Merlin.Builder().withConnectableCallbacks().withDisconnectableCallbacks().withBindableCallbacks().build(this);
    }

    public static NavigationActivity getInstance() {
        return instance;
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finish();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    @SuppressLint("WrongConstant")
    public void showFragment(Fragment fragment, @StringRes int titleResId) {
        FragmentUtils.replaceFragment(this, fragment, R.id.content_frame, true, 5);
        if (titleResId == 0)
            return;
        setTitle(titleResId);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.d("onNavigationItem", String.valueOf(id));
        displayView(id);
        return true;
    }

    private void displayView(Object mItemObject) {
        int mItem = (int) mItemObject;
        if (mItem == R.id.take_order) {
//            FragmentUtils.replaceFragment(this, new DashboardFragment(), R.id.content_frame, false, TRANSITION_FROM_LEFT_TO_RIGHT);
            showFragment(new TakeNewOrderFragment(), R.string.menu_take_order);
        } else if (mItem == R.id.nav_dashboard) {
            FragmentUtils.replaceFragment(this, new DashboardFragment(), R.id.content_frame, false, TRANSITION_FROM_LEFT_TO_RIGHT);
//            showFragment(new DashboardFragment(), R.string.menu_dashboard);
        } else if (mItem == R.id.nav_profile) {
            showFragment(new ProfileFragment(), R.string.menu_profile);
        } else if (mItem == R.id.nav_change_password) {
            showFragment(new ChangePasswordFragment(), R.string.menu_change_password);
        } else if (mItem == R.id.nav_notifications) {
            showFragment(new NotificationsFragment(), R.string.menu_notifications);
        } else if (mItem == R.id.nav_orders) {
            showFragment(new MyOrdersFragment(), R.string.menu_my_orders);
        } else if (mItem == R.id.nav_faqs) {
            showFragment(new HelpFragment(), R.string.menu_help);
        }
//        else if (mItem == R.id.nav_logout) {
//            ActivityUtils.startActivity(this, LoginActivity.class, null);
//            finishAffinity();
//        }get
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mDrawerToggle != null)
            mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    /* custom navigation drawer components */
    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    Toolbar mtoolbar;
    NavigationDrawerAdapter adapter;

    @SuppressLint("SetTextI18n")
    private void initCustomNavigationDrawer() {
        mNavigationDrawerItemTitles = getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.left_drawer);
        setupToolbar();

        LayoutInflater inflater = getLayoutInflater();
        header = (ViewGroup) inflater.inflate(R.layout.nav_header_main, mDrawerList, false);
        mDrawerList.addHeaderView(header);

        NavDrawerModel[] drawerItem = new NavDrawerModel[5];
        drawerItem[0] = new NavDrawerModel(mNavigationDrawerItemTitles[0], false, true);
        drawerItem[1] = new NavDrawerModel(mNavigationDrawerItemTitles[1], false, false);
//        drawerItem[2] = new NavDrawerModel(mNavigationDrawerItemTitles[2], false, false);
        drawerItem[2] = new NavDrawerModel(mNavigationDrawerItemTitles[2], false, false);
        drawerItem[3] = new NavDrawerModel(mNavigationDrawerItemTitles[3], false, false);
        drawerItem[4] = new NavDrawerModel(mNavigationDrawerItemTitles[4], false, false);
//        drawerItem[6] = new NavDrawerModel(mNavigationDrawerItemTitles[6], false, false);
        adapter = new NavigationDrawerAdapter(this, R.layout.nav_item_row, drawerItem);

        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new NavigationActivity.DrawerItemClickListener());
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        setupDrawerToggle();
        if (isLanchedByPushNotification)
            selectItem(1);
        else
            selectItem(0);
//        ((NavigationDrawerAdapter) ((HeaderViewListAdapter) mDrawerList.getAdapter()).getWrappedAdapter()).notifyDataSetChanged();
        adapter.notifyDataSetChanged();
        TextView appVersion = findViewById(R.id.app_version);
        appVersion.setText("Version " + BuildConfig.VERSION_NAME);
        TextView logoutText = findViewById(R.id.logout_btn);
        logoutText.setOnClickListener(v -> {
//            mDrawerLayout.closeDrawer(GravityCompat.START);
            Dialog alertDialog = new Dialog(this);
            DialogAlertCustomBinding alertCustomBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_alert_custom, null, false);
            alertDialog.setContentView(alertCustomBinding.getRoot());
            alertCustomBinding.title.setText(R.string.label_alert);
            alertCustomBinding.subtitle.setText(R.string.label_are_you_sure_want_to_logout);
            alertCustomBinding.dialogButtonNO.setText(R.string.label_no);
            alertCustomBinding.dialogButtonOK.setText(R.string.label_yes);
            alertCustomBinding.dialogButtonOK.setOnClickListener(v1 -> {
                alertDialog.dismiss();
                new NavigationActivityController(this, this).riderUpdateStauts(getSessionManager().getLoginToken(), "Offline");
            });
            alertCustomBinding.dialogButtonNO.setOnClickListener(v2 -> alertDialog.dismiss());
            alertDialog.show();
        });
    }

    @Override
    public void onFialureMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessRiderUpdateStatusApiCall() {
        new SessionManager(this).clearAllSharedPreferences();
        stopBatteryLevelLocationService();
        Intent intent = new Intent(NavigationActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    public void onFailureRiderUpdateStatusApiCall(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

//    @OnClick(R.id.logout_btn)
//    void onLogoutClick() {
//        mDrawerLayout.closeDrawer(GravityCompat.START);
//        finish();
//    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.e(TAG, "selected Pos : " + position);
//            if ((adapter).getItem(position).isSelected()) {
//                mDrawerLayout.closeDrawer(GravityCompat.START);
//            } else {
//                selectItem(position - 1);
//                (adapter).onSelection(position-1);
//            }
            if (position != 0)
                mDrawerLayout.closeDrawer(GravityCompat.START);
            selectItem(position - 1);
//            if (((NavigationDrawerAdapter) parent.getAdapter()).getItem(position).isSelected()) {
//                mDrawerLayout.closeDrawer(GravityCompat.START);
//            } else {
//                selectItem(position);
//                ((NavigationDrawerAdapter) parent.getAdapter()).onSelection(position);
//            }
        }
    }

    public void updateSelection(int pos) {
        selectedItemPos = pos;
        adapter.onSelection(pos);
    }

    public void selectItem(int position) {
        if (position != -1) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            adapter.onSelection(position);
            if (selectedItemPos != position) {
                selectedItemPos = position;
                switch (position) {
//                case 0:
//                    mCurrentFrag = getString(R.string.menu_take_order);
//                    showFragment(TakeNewOrderFragment.newInstance(), R.string.menu_take_order);
//                    break;
                    case 0:
                        mCurrentFrag = getString(R.string.menu_dashboard);
                        showFragment(DashboardFragment.newInstance(), R.string.menu_dashboard);
                        break;
                    case 1:
                        mCurrentFrag = getString(R.string.menu_my_orders);
                        showFragment(MyOrdersFragment.newInstance(), R.string.menu_my_orders);
                        break;
//                    case 2:
//                        mCurrentFrag = getString(R.string.menu_notifications);
//                        showFragment(NotificationsFragment.newInstance(), R.string.menu_notifications);
//                        break;
                    case 2:
                        mCurrentFrag = getString(R.string.menu_profile);
                        showFragment(ProfileFragment.newInstance(), R.string.menu_profile);
                        break;
                    case 3:
                        mCurrentFrag = getString(R.string.menu_change_password);
                        showFragment(ChangePasswordFragment.newInstance(), R.string.menu_change_password);
                        break;
                    case 4:
                        mCurrentFrag = getString(R.string.menu_help);
                        showFragment(HelpFragment.newInstance(), R.string.menu_help);
                        break;
                    default:
                        break;
                }
                mDrawerList.setItemChecked(position, true);
                mDrawerList.setSelection(position);
//            setTitle(mNavigationDrawerItemTitles[position]);
            }
        }
    }

    void setupToolbar() {
        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        mtoolbar.setTitleTextAppearance(this, R.style.ToolbarTextAppearance);
        setSupportActionBar(mtoolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
    }

    void setupDrawerToggle() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mtoolbar, R.string.app_name, R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
//        mDrawerToggle.setDrawerIndicatorEnabled(true);
//        mDrawerToggle.setHomeAsUpIndicator(R.drawable.menu_hamburg);
        mDrawerToggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        mDrawerToggle.setToolbarNavigationClickListener(view -> {
            mDrawerLayout.openDrawer(GravityCompat.START);
        });
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_notification, menu);

        final MenuItem menuNotificationItem = menu.findItem(R.id.action_setting_icon);
        View actionNotificationView = MenuItemCompat.getActionView(menuNotificationItem);
        notificationText = actionNotificationView.findViewById(R.id.notification_text);
        if (!getSessionManager().getNotificationStatus()) {
            notificationText.setVisibility(View.GONE);
            notificationText.clearAnimation();
            DashboardFragment.newOrderViewVisibility(false);
            getSessionManager().setNotificationStatus(false);
        }
        actionNotificationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onOptionsItemSelected(menuNotificationItem);
                notificationText.setVisibility(View.GONE);
                notificationText.clearAnimation();
//                DashboardFragment.newOrderViewVisibility(false);
//                getSessionManager().setNotificationStatus(false);
            }
        });

        final MenuItem menuCartItem = menu.findItem(R.id.action_cart_icon);
        if (getString(R.string.menu_take_order).equals(mCurrentFrag)) {
            menuCartItem.setVisible(true);
        } else {
            menuCartItem.setVisible(false);
        }

        View actionCartView = MenuItemCompat.getActionView(menuCartItem);
        cartCount = actionCartView.findViewById(R.id.cart_count_text);
        actionCartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuCartItem);
            }
        });
        return true;
    }

    public static void updateCartCount(String cartCnt) {
//        cartCount.setText(cartCnt);
    }

    static Animation anim;

    public static void notificationDotVisibility(boolean show) {
        if (show) {
            try {
                notificationText.setVisibility(View.VISIBLE);
                anim = new AlphaAnimation(0.0f, 1.0f);
                anim.setDuration(350); //You can manage the blinking time with this parameter
                anim.setStartOffset(20);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(Animation.INFINITE);
                notificationText.startAnimation(anim);

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String orderDate = CommonUtils.getCurrentTimeDate();
                Date orderDates = formatter.parse(orderDate);
                long orderDateMills = orderDates.getTime();

                getInstance().getSessionManager().setNotificationArrivedTime(CommonUtils.getTimeFormatter(orderDateMills));

            } catch (Exception e) {
                System.out.println("NavigationActivity:::::::::::::::::::::::::::::" + e.getMessage());
            }

        } else {
            notificationText.setVisibility(View.GONE);
            notificationText.clearAnimation();

        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                return true;
            case R.id.action_setting_icon:
                if (getSessionManager().getNotificationStatus())
                    selectItem(0);
                else
                    Toast.makeText(this, "No Notification.", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_cart_icon:
                Intent i = new Intent(this, CartActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 1) {
            boolean isFinishingActivity = Boolean.parseBoolean(data.getStringExtra("FinishingActivity"));
            if (isFinishingActivity) {
                selectItem(3);
            }
        } else if (requestCode == REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    Intent intent = new Intent(NavigationActivity.this, FloatingTouchService.class);
                    if (!isMyServiceRunning(FloatingTouchService.class)) {
                        startService(intent);
                    }
                }
            }
        } else if (requestCode == GPS_REQUEST_CODE) {
            if (isGpsEnambled()) {
                LinearLayout locationDeniedLayout = (LinearLayout) findViewById(R.id.location_denied);
                locationDeniedLayout.setVisibility(View.GONE);

                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (isGpsEnambled()) {
                    checkLocationPermission();
                } else {
                    buildAlertMessageNoGps();
                }
            } else {
                LinearLayout locationDeniedLayout = (LinearLayout) findViewById(R.id.location_denied);
                TextView locationPermissionDeniedText = (TextView) findViewById(R.id.location_permission_denied_text);
                locationPermissionDeniedText.setText(R.string.label_gps_enable_to_access_application);
                Button locationPermissionBtn = (Button) findViewById(R.id.location_permission_btn);
                locationPermissionBtn.setText(R.string.gps_enable);
                locationDeniedLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onResume() {
        CommonUtils.CURRENT_SCREEN = getClass().getSimpleName();
        Hawk.put(LAST_ACTIVITY, getClass().getSimpleName());
        super.onResume();
        startService(new Intent(NavigationActivity.this, FloatingTouchService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CommonUtils.CURRENT_SCREEN = "";
        Intent intent = new Intent(NavigationActivity.this, FloatingTouchService.class);
        if (isMyServiceRunning(FloatingTouchService.class)) {
            stopService(intent);
        }
        Intent i = new Intent(NavigationActivity.this, BatteryLevelLocationService.class);
        if (isMyServiceRunning(BatteryLevelLocationService.class)) {
            startService(i);
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

    public final static int REQUEST_CODE = 1234;

    private void handleAssistiveTouchWindow() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(NavigationActivity.this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, REQUEST_CODE);
            } else {
                Intent intent = new Intent(NavigationActivity.this, FloatingTouchService.class);
                if (!isMyServiceRunning(FloatingTouchService.class)) {
                    startService(intent);
                }
            }
        } else {
            Intent intent = new Intent(NavigationActivity.this, FloatingTouchService.class);
            if (!isMyServiceRunning(FloatingTouchService.class)) {
                startService(intent);
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public void setProfileData() {
        if (getSessionManager().getRiderProfileResponse() != null) {
            ImageView userImg = header.findViewById(R.id.user_image);
            if (getSessionManager().getRiderProfileResponse() != null && getSessionManager().getRiderProfileResponse().getData() != null && getSessionManager().getRiderProfileResponse().getData().getPic() != null && getSessionManager().getRiderProfileResponse().getData().getPic().size() > 0)
                Glide.with(this).load(getSessionManager().getrRiderIconUrl()).circleCrop().error(R.drawable.apollo_app_logo).into(userImg);
            TextView riderName = header.findViewById(R.id.nav_header_rider_name);
            riderName.setText(getSessionManager().getRiderProfileResponse().getData().getFirstName() + " " + getSessionManager().getRiderProfileResponse().getData().getLastName());
            TextView riderPhoneNumber = header.findViewById(R.id.nav_header_rider_phone_number);
            riderPhoneNumber.setText(getSessionManager().getRiderProfileResponse().getData().getPhone());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
