package com.apollo.epos.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.apollo.epos.R;
import com.apollo.epos.adapter.NavigationDrawerAdapter;
import com.apollo.epos.fragment.changepassword.ChangePasswordFragment;
import com.apollo.epos.fragment.dashboard.DashboardFragment;
import com.apollo.epos.fragment.help.HelpFragment;
import com.apollo.epos.fragment.myorders.MyOrdersFragment;
import com.apollo.epos.fragment.notifications.NotificationsFragment;
import com.apollo.epos.fragment.profile.ProfileFragment;
import com.apollo.epos.fragment.takeneworder.TakeNewOrderFragment;
import com.apollo.epos.model.NavDrawerModel;
import com.apollo.epos.utils.FragmentUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import butterknife.OnClick;

import static com.apollo.epos.utils.ActivityUtils.getCurrentTime;
import static com.apollo.epos.utils.FragmentUtils.TRANSITION_FROM_LEFT_TO_RIGHT;

public class NavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    LinearLayout linearLayout;
    ActionBarDrawerToggle toggle;
    private String TAG = "MainActivity";
    private int selectedItemPos = -1;
    private String mCurrentFrag;
    private static TextView cartCount;
    private static NavigationActivity instance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample);
        instance = this;
        initCustomNavigationDrawer();
    }

    public static NavigationActivity getInstance() {
        return instance;
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
        }
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
//        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
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

    private void initCustomNavigationDrawer() {
        mNavigationDrawerItemTitles = getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.left_drawer);
        setupToolbar();

        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.nav_header_main, mDrawerList, false);
        mDrawerList.addHeaderView(header);

        NavDrawerModel[] drawerItem = new NavDrawerModel[7];
        drawerItem[0] = new NavDrawerModel(mNavigationDrawerItemTitles[0], false, true);
        drawerItem[1] = new NavDrawerModel(mNavigationDrawerItemTitles[1], false, false);
        drawerItem[2] = new NavDrawerModel(mNavigationDrawerItemTitles[2], false, false);
        drawerItem[3] = new NavDrawerModel(mNavigationDrawerItemTitles[3], false, false);
        drawerItem[4] = new NavDrawerModel(mNavigationDrawerItemTitles[4], false, false);
        drawerItem[5] = new NavDrawerModel(mNavigationDrawerItemTitles[5], false, false);
        drawerItem[6] = new NavDrawerModel(mNavigationDrawerItemTitles[6], false, false);
        adapter = new NavigationDrawerAdapter(this, R.layout.nav_item_row, drawerItem);

        ImageView userImg = header.findViewById(R.id.user_image);
        String imageUrl = "https://www.filmibeat.com/ph-big/2019/02/mahesh-babu_155056989980.jpg";
        Glide.with(NavigationActivity.this)
                .load(getResources().getDrawable(R.drawable.userimage))
                .apply(RequestOptions.circleCropTransform())
                .into(userImg);

        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new NavigationActivity.DrawerItemClickListener());
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        setupDrawerToggle();
        selectItem(1);
//        ((NavigationDrawerAdapter) ((HeaderViewListAdapter) mDrawerList.getAdapter()).getWrappedAdapter()).notifyDataSetChanged();
        adapter.notifyDataSetChanged();

        TextView logoutText = findViewById(R.id.logout_btn);
        logoutText.setOnClickListener(v -> {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            finish();
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        });
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
        mDrawerLayout.closeDrawer(GravityCompat.START);
        adapter.onSelection(position);
        if (selectedItemPos != position) {
            selectedItemPos = position;
            switch (position) {
                case 0:
                    mCurrentFrag = getString(R.string.menu_take_order);
                    showFragment(TakeNewOrderFragment.newInstance(), R.string.menu_take_order);
                    break;
                case 1:
                    mCurrentFrag = getString(R.string.menu_dashboard);
                    showFragment(DashboardFragment.newInstance(), R.string.menu_dashboard);
                    break;
                case 2:
                    mCurrentFrag = getString(R.string.menu_my_orders);
                    showFragment(MyOrdersFragment.newInstance(), R.string.menu_my_orders);
                    break;
                case 3:
                    mCurrentFrag = getString(R.string.menu_notifications);
                    showFragment(NotificationsFragment.newInstance(), R.string.menu_notifications);
                    break;
                case 4:
                    mCurrentFrag = getString(R.string.menu_profile);
                    showFragment(ProfileFragment.newInstance(), R.string.menu_profile);
                    break;
                case 5:
                    mCurrentFrag = getString(R.string.menu_change_password);
                    showFragment(ChangePasswordFragment.newInstance(), R.string.menu_change_password);
                    break;
                case 6:
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

    void setupToolbar() {
        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        mtoolbar.setTitleTextAppearance(this, R.style.ToolbarTextAppearance);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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
        TextView notificationText = actionNotificationView.findViewById(R.id.notification_text);
        actionNotificationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuNotificationItem);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                return true;
            case R.id.action_setting_icon:
                selectItem(3);
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
        }
    }
}
