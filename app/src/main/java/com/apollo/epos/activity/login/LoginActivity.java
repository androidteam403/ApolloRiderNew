package com.apollo.epos.activity.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.apollo.epos.R;
import com.apollo.epos.activity.BaseActivity;
import com.apollo.epos.activity.NavigationActivity;
import com.apollo.epos.activity.login.model.LoginResponse;
import com.apollo.epos.databinding.ActivityLoginBinding;
import com.apollo.epos.dialog.ForgotPwdDialog;
import com.apollo.epos.model.GetRiderProfileResponse;
import com.apollo.epos.utils.ActivityUtils;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.novoda.merlin.Merlin;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements LoginActivityCallback {
    @BindView(R.id.btn_signin)
    protected Button signInBtn;
    private ActivityLoginBinding loginBinding;
    private String firebaseToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        ButterKnife.bind(this);
        setUp();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUp() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, instanceIdResult -> {
            String newToken = instanceIdResult.getToken();
            this.firebaseToken = newToken;
            Log.e("newToken", newToken);
//            getActivity().getPreferences(Context.MODE_PRIVATE).edit().putString("fb", newToken).apply();
        });//        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
//            if (!TextUtils.isEmpty(token)) {
//                this.firebaseToken = token;
//                Log.d("Login Activity", "retrieve token successful : " + token);
//            } else {
//                Log.w("Login Activity", "token should not be null...");
//            }
//        }).addOnFailureListener(e -> {
//            //handle e
//        }).addOnCanceledListener(() -> {
//            //handle cancel
//        }).addOnCompleteListener(task -> Log.v("Login Activity", "This is the token : " + task.getResult()));


        loginBinding.loginParentLayout.setOnTouchListener((v, event) -> {
            hideKeyboard();
            return false;
        });
    }

    @Override
    protected Merlin createMerlin() {
        return new Merlin.Builder()
                .withConnectableCallbacks()
                .withDisconnectableCallbacks()
                .withBindableCallbacks()
                .build(this);
    }

    private Boolean validate() {
        if (loginBinding.etUserEmail.getText().toString().trim().isEmpty()) {
            loginBinding.etUserEmail.setError("User name should not be empty");
            loginBinding.etUserEmail.requestFocus();
            return false;
        } else if (loginBinding.etUserPassword.getText().toString().trim().isEmpty()) {
            loginBinding.etUserPassword.setError("Password should not be empty");
            loginBinding.etUserPassword.requestFocus();
            return false;
        }
        return true;
    }

    @OnClick(R.id.btn_signin)
    void onSignInClick() {
        if (validate()) {
            new LoginActivityController(this, this).loginApiCall(
                    loginBinding.etUserEmail.getText().toString().trim(),
                    loginBinding.etUserPassword.getText().toString().trim(), firebaseToken);
        }
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

    @Override
    public void onFialureMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessLoginApi(LoginResponse loginResponse) {
        if (loginResponse != null && loginResponse.getData() != null && loginResponse.getSuccess() && loginResponse.getData().getToken() != null) {
            getSessionManager().setLoginToken(loginResponse.getData().getToken());
            getSessionManager().setRiderIconUrl(loginResponse.getData().getAddInfo().getPic().get(0).getDimenesions().get200200FullPath());
            new LoginActivityController(this, this).getRiderProfileDetailsApi(getSessionManager().getLoginToken());
        }
    }

    @Override
    public void onFailureLoginApi(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessGetProfileDetailsApi(GetRiderProfileResponse getRiderProfileResponse) {
        ActivityUtils.hideDialog();
        if (getRiderProfileResponse != null) {
            getSessionManager().setRiderProfileDetails(getRiderProfileResponse);
            Intent i = new Intent(getApplicationContext(), NavigationActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(i);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            finish();
        }
    }

    @Override
    public void onFailureGetProfileDetailsApi(String message) {

    }


}
