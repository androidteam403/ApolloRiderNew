package com.apollo.epos.activity;

import android.content.Context;
import android.os.Bundle;

import com.apollo.epos.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class DrawerActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private RecyclerView list;
    private adapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);
//        ImageView userImg = hView.findViewById(R.id.user_image);
//        String imageUrl = "https://www.filmibeat.com/ph-big/2019/02/mahesh-babu_155056989980.jpg";
//        Glide.with(DrawerActivity.this)
//                .load(getResources().getDrawable(R.drawable.userimage))
//                .apply(RequestOptions.circleCropTransform())
//                .into(userImg);

//        list = (RecyclerView) findViewById(R.id.list);
        //Data
        ArrayList<String> nav_item = new ArrayList<>();
        nav_item.add("Home");
        nav_item.add("App");
        nav_item.add("Blog");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);
        recyclerAdapter = new adapter(DrawerActivity.this, nav_item);
        list.setAdapter(recyclerAdapter);

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,drawer, toolbar,R.string.app_name, R.string.app_name);

        drawer.addDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();

//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_dashboard, R.id.nav_profile, R.id.nav_change_password,
//                R.id.nav_notifications, R.id.nav_orders, R.id.nav_faqs)
//                .setDrawerLayout(drawer)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.content_frame); //nav_host_fragment
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void hideStatusBar(View view) {
        // Hide status bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void showStatusBar(View view) {
        // Show status bar
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private class adapter extends RecyclerView.Adapter<adapter.myViewHolder> {
        Context context;
        List<String> mData;

        public adapter(Context context, List<String> data) {
            this.context = context;
            this.mData = data;
        }

        @Override
        public adapter.myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.navigation_recyclerview_adapter, parent, false);
            return new myViewHolder(view);
        }

        @Override
        public void onBindViewHolder(adapter.myViewHolder holder, int position) {
            holder.nav.setText(mData.get(position));
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
        public class myViewHolder extends RecyclerView.ViewHolder {
            TextView nav;

            public myViewHolder(View itemView) {
                super(itemView);
                nav = (TextView) itemView.findViewById(R.id.nav);
            }
        }
    }
}
