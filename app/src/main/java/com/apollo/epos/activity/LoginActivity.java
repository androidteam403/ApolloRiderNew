package com.apollo.epos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.apollo.epos.R;
import com.apollo.epos.dialog.ForgotPwdDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.btn_signin)
    protected Button signInBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_signin)
    void onSignInClick() {
        Intent i = new Intent(getApplicationContext(), NavigationActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    @OnClick(R.id.forgot_password_text)
    void onForgotPasswordClick() {
        ForgotPwdDialog forgotPwdDialog = new ForgotPwdDialog(LoginActivity.this);
        forgotPwdDialog.setCancelable(false);
        forgotPwdDialog.show();
        Window window = forgotPwdDialog.getWindow();
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
        int height = WindowManager.LayoutParams.WRAP_CONTENT;
        if (window != null) {
            window.setLayout(width, height);
        }
    }
}
