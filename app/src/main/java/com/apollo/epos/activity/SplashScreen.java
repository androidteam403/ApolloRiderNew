package com.apollo.epos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.databinding.DataBindingUtil;

import com.apollo.epos.R;
import com.apollo.epos.activity.login.LoginActivity;
import com.apollo.epos.databinding.ActivitySplashBinding;
import com.apollo.epos.db.SessionManager;
import com.novoda.merlin.Merlin;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashScreen extends BaseActivity {
    private static final long SPLASH_DISPLAY_LENGTH = 2500;
    @BindView(R.id.image_app_logo)
    ImageView iv_splash;
    private ActivitySplashBinding splashBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        splashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        ButterKnife.bind(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Animation slideLefttoRight = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_from_left);
        slideLefttoRight.setDuration(1700);
        splashBinding.riderIcon.setVisibility(View.VISIBLE);
        splashBinding.riderIcon.startAnimation(slideLefttoRight);

        Animation animZoomOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
        iv_splash.startAnimation(animZoomOut);

        new Handler().postDelayed(() -> {
            if (new SessionManager(this).getLoginToken() != null && !new SessionManager(this).getLoginToken().isEmpty()) {
                Intent mainIntent = new Intent(SplashScreen.this, NavigationActivity.class);
                startActivity(mainIntent);
                finish();
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            } else {
                Intent mainIntent = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(mainIntent);
                finish();
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    protected Merlin createMerlin() {
        return new Merlin.Builder()
                .withConnectableCallbacks()
                .withDisconnectableCallbacks()
                .withBindableCallbacks()
                .build(this);
    }
}
