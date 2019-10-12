package com.apollo.epos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.apollo.epos.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashScreen extends AppCompatActivity {
    private static final long SPLASH_DISPLAY_LENGTH = 2500;
    @BindView(R.id.image_app_logo)
    ImageView iv_splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Animation animZoomOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
        iv_splash.startAnimation(animZoomOut);

        new Handler().postDelayed(() -> {
            Intent mainIntent = new Intent(SplashScreen.this, LoginActivity.class);
            startActivity(mainIntent);
            finish();
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        }, SPLASH_DISPLAY_LENGTH);
    }
}
