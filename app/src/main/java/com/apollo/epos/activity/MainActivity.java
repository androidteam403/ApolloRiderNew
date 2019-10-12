package com.apollo.epos.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.apollo.epos.R;
import com.apollo.epos.adapter.NavigationDrawerAdapter;
import com.apollo.epos.fragment.changepassword.ChangePasswordFragment;
import com.apollo.epos.fragment.dashboard.DashboardFragment;
import com.apollo.epos.fragment.help.HelpFragment;
import com.apollo.epos.fragment.notifications.NotificationsFragment;
import com.apollo.epos.fragment.myorders.MyOrdersFragment;
import com.apollo.epos.fragment.profile.ProfileFragment;
import com.apollo.epos.model.NavDrawerModel;
import com.apollo.epos.utils.FragmentUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;

import static com.apollo.epos.utils.FragmentUtils.TRANSITION_SLIDE_LEFT_RIGHT;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    LinearLayout linearLayout;
    ActionBarDrawerToggle toggle;
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        initCustomNavigationDrawer();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void updateMenuUI() {
//        Menu navMenu = dataBinding.navView.getMenu();
//        Log.e("menu count", "" + navMenu.size());
//        for (int i = 0; i < navMenu.size(); i++) {
//            MenuItem item = navMenu.getItem(i);
//            if (item.getItemId() == R.id.nav_logout) {
//                SpannableString ss = new SpannableString(item.getTitle());
////                ChipDrawable chip = ChipDrawable.createFromResource(this, R.xml.standalone_chip);
////                chip.setBounds(0, 0, chip.getIntrinsicWidth(), chip.getIntrinsicHeight());
////                ImageSpan span2 = new ImageSpan(chip);
//                ss.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, ss.length(), 0);
////                ss.setSpan(span2, 0, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
////                ss.setSpan(new ForegroundColorSpan(Color.BLUE), 0, ss.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//                item.setTitle(ss);
//            } else if (item.getItemId() == R.id.nav_dashboard) {
//                SpannableString s = new SpannableString(item.getTitle());
//                s.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, s.length(), 0);
//                item.setTitle(s);
//            } else {
//                SpannableString s = new SpannableString(item.getTitle());
//                s.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, s.length(), 0);
//                item.setTitle(s);
//            }
//
//        }
    }

    @SuppressLint("WrongConstant")
    public void showFragment(Fragment fragment, @StringRes int titleResId) {
        FragmentUtils.replaceFragment(this, fragment, R.id.nav_host_fragment, false, 2);
//        if (titleResId == 0)
//            return;
        setTitle(titleResId);
    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        //noinspection SimplifiableIfStatement
//        /*if (id == R.id.action_settings) {
//            return true;
//        }*/
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Log.d("onNavigationItem", String.valueOf(id));
        displayView(id);
        return true;
    }

    private void displayView(Object mItemObject) {
        int mItem = (int) mItemObject;
        if (mItem == R.id.nav_dashboard) {
            FragmentUtils.replaceFragment(this, new DashboardFragment(), R.id.nav_host_fragment, false, TRANSITION_SLIDE_LEFT_RIGHT);
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

        ImageView userImg = findViewById(R.id.user_image);
        String imageUrl = "https://www.filmibeat.com/ph-big/2019/02/mahesh-babu_155056989980.jpg";
        Glide.with(MainActivity.this)
                .load(getResources().getDrawable(R.drawable.userimage))
                .apply(RequestOptions.circleCropTransform())
                .into(userImg);

        NavDrawerModel[] drawerItem = new NavDrawerModel[6];
        drawerItem[0] = new NavDrawerModel(mNavigationDrawerItemTitles[0], true, false);
        drawerItem[1] = new NavDrawerModel(mNavigationDrawerItemTitles[1], false, false);
        drawerItem[2] = new NavDrawerModel(mNavigationDrawerItemTitles[2], false, false);
        drawerItem[3] = new NavDrawerModel(mNavigationDrawerItemTitles[3], false, false);
        drawerItem[4] = new NavDrawerModel(mNavigationDrawerItemTitles[4], false, false);
        drawerItem[5] = new NavDrawerModel(mNavigationDrawerItemTitles[5], false, false);
//        drawerItem[6] = new NavDrawerModel(mNavigationDrawerItemTitles[6], false, false);
        adapter = new NavigationDrawerAdapter(this, R.layout.nav_item_row, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setupDrawerToggle();
        selectItem(1);
        adapter.notifyDataSetChanged();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (((NavigationDrawerAdapter) parent.getAdapter()).getItem(position).isSelected()) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            } else {
                selectItem(position);
                ((NavigationDrawerAdapter) parent.getAdapter()).onSelection(position);
            }
        }
    }

    public void updateSelection(int pos) {
        adapter.onSelection(pos);
    }

    public void selectItem(int position) {
        mDrawerLayout.closeDrawer(GravityCompat.START);
        switch (position) {
            case 1:
                showFragment(DashboardFragment.newInstance(), R.string.menu_dashboard);
                break;
            case 2:
                showFragment(ProfileFragment.newInstance(), R.string.menu_profile);
                break;
            case 3:
                showFragment(ChangePasswordFragment.newInstance(), R.string.menu_change_password);
                break;
            case 4:
                showFragment(NotificationsFragment.newInstance(), R.string.menu_notifications);
                break;
            case 5:
                showFragment(MyOrdersFragment.newInstance(), R.string.menu_my_orders);
                break;
            case 6:
                showFragment(HelpFragment.newInstance(), R.string.menu_help);
                break;
            default:
                break;
        }

        mDrawerList.setItemChecked(position, true);
        mDrawerList.setSelection(position);
        setTitle(mNavigationDrawerItemTitles[position]);
    }

    void setupToolbar() {
        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    void setupDrawerToggle() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mtoolbar, R.string.app_name, R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
//        mDrawerToggle.setDrawerIndicatorEnabled(true);
//        mDrawerToggle.setHomeAsUpIndicator(R.drawable.menu_hamburg);
        mDrawerToggle.setToolbarNavigationClickListener(view -> {
            mDrawerLayout.openDrawer(GravityCompat.START);
        });
        mDrawerToggle.syncState();
    }
}
