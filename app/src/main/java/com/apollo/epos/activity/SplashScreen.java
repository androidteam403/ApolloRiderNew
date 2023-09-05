package com.apollo.epos.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.databinding.DataBindingUtil;

import com.apollo.epos.BuildConfig;
import com.apollo.epos.R;
import com.apollo.epos.activity.login.LoginActivity;
import com.apollo.epos.activity.navigation.NavigationActivity;
import com.apollo.epos.activity.orderdelivery.OrderDeliveryActivity;
import com.apollo.epos.databinding.ActivitySplashBinding;
import com.apollo.epos.databinding.DialogForceUpdateBinding;
import com.apollo.epos.model.ForceUpdateResponse;
import com.novoda.merlin.Merlin;

import butterknife.ButterKnife;

public class SplashScreen extends BaseActivity implements SplashScreenCallback {

    private static final long SPLASH_DISPLAY_LENGTH = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        ActivitySplashBinding splashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        ButterKnife.bind(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Animation slideLefttoRight = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_from_left);
        slideLefttoRight.setDuration(1700);
        splashBinding.riderIcon.setVisibility(View.VISIBLE);
        splashBinding.riderIcon.startAnimation(slideLefttoRight);

        Animation animZoomOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
        splashBinding.imageAppLogo.startAnimation(animZoomOut);
        new SplashScreenController(this, this).forceUpdateApiCall();
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
    public void onSuccessForceUpdateApiCall(ForceUpdateResponse forceUpdateResponse) {
        if (forceUpdateResponse != null && forceUpdateResponse.getAndroid().getAppAvailability().equalsIgnoreCase("true")) {
            if (forceUpdateResponse.getAndroid().getBuildNo() > BuildConfig.VERSION_CODE) {
                showUpdateDialog(forceUpdateResponse);
            } else {
                handleIntent();
            }
        } else {
            handleIntent();
        }
    }

    private void showUpdateDialog(ForceUpdateResponse forceUpdateResponse) {
        Dialog updateDialog = new Dialog(this);
        updateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        updateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        DialogForceUpdateBinding dialogForceUpdateBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_force_update, null, false);
        updateDialog.setContentView(dialogForceUpdateBinding.getRoot());
        updateDialog.setCancelable(false);
        dialogForceUpdateBinding.title.setText(forceUpdateResponse.getAndroid().getTitle());
        dialogForceUpdateBinding.message.setText(forceUpdateResponse.getAndroid().getMessage());
        if (forceUpdateResponse.getAndroid().getForceUpdate().equalsIgnoreCase("true")) {
            dialogForceUpdateBinding.noThanksBtn.setVisibility(View.GONE);
        } else {
            dialogForceUpdateBinding.noThanksBtn.setVisibility(View.VISIBLE);
        }
        dialogForceUpdateBinding.noThanksBtn.setOnClickListener(view -> {
            updateDialog.dismiss();
            handleIntent();
        });
        dialogForceUpdateBinding.updateNowBtn.setOnClickListener(view -> {
            Uri uri = Uri.parse(forceUpdateResponse.getAndroid().getUrl());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
        updateDialog.show();
    }

    private void handleIntent() {
        new Handler().postDelayed(() -> {
            if (getSessionManager().getLoginToken() != null && !getSessionManager().getLoginToken().isEmpty()) {
                if (getIntent() != null) {
                    String notificationType = getIntent().getStringExtra("notification_type");
                    if (notificationType != null && notificationType.equals("ORDER_ASSIGNED")) {
                        startActivity(OrderDeliveryActivity.getStartIntent(SplashScreen.this, getIntent().getStringExtra("uid"), true));
                        finish();
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    } else if (notificationType != null && notificationType.equals("COMPLAINT_RESOLVED")) {
                        Intent mainIntent = new Intent(SplashScreen.this, NavigationActivity.class);
                        mainIntent.putExtra("COMPLAINT_RESOLVED", true);
                        startActivity(mainIntent);
                        finish();
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    } else {
                        Intent mainIntent = new Intent(SplashScreen.this, NavigationActivity.class);
                        startActivity(mainIntent);
                        finish();
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    }
                } else {
                    Intent mainIntent = new Intent(SplashScreen.this, NavigationActivity.class);
                    startActivity(mainIntent);
                    finish();
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                }
            } else {
                Intent mainIntent = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(mainIntent);
                finish();
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    public void onFailureForceUpdateApiCall(String message) {
        Log.i("FORCE_UPDATE_API", "onFailureForceUpdateApiCall: " + message);
    }
}
