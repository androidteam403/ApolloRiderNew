package com.apollo.epos.fragment.changepassword;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.apollo.epos.R;
import com.apollo.epos.activity.login.LoginActivity;
import com.apollo.epos.activity.navigation.NavigationActivity;
import com.apollo.epos.base.BaseFragment;
import com.apollo.epos.databinding.FragmentChangePasswordBinding;
import com.apollo.epos.db.SessionManager;
import com.apollo.epos.fragment.changepassword.model.ChangePasswordResponse;

import java.util.Objects;

public class ChangePasswordFragment extends BaseFragment implements ChangePasswordFragmentCallback {
    private Activity mActivity;

    private FragmentChangePasswordBinding changePasswordBinding;

    public static ChangePasswordFragment newInstance() {
        return new ChangePasswordFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        changePasswordBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_change_password, container, false);
        return changePasswordBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        changePasswordBinding.setCallback(this);
    }

    private boolean validate() {
        if (Objects.requireNonNull(changePasswordBinding.etOldPassword.getText()).toString().trim().isEmpty()) {
            changePasswordBinding.etOldPassword.setError("Old password should not be empty");
            changePasswordBinding.etOldPassword.requestFocus();
            return false;
        } else if (Objects.requireNonNull(changePasswordBinding.etNewPassword.getText()).toString().trim().isEmpty()) {
            changePasswordBinding.etNewPassword.setError("New password should not be empty");
            changePasswordBinding.etNewPassword.requestFocus();
            return false;
        } else if (Objects.requireNonNull(changePasswordBinding.etConfirmPassword.getText()).toString().trim().isEmpty()) {
            changePasswordBinding.etConfirmPassword.setError("Confirm password should not be empty");
            changePasswordBinding.etConfirmPassword.requestFocus();
            return false;
        } else if (!changePasswordBinding.etNewPassword.getText().toString().trim().equals(changePasswordBinding.etConfirmPassword.getText().toString().trim())) {
            Toast.makeText(getContext(), "New password and Confirm password did not match", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onFailureMessage(String message) {
        Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickUpdate() {
        if (validate()) {
            getController().changePasswordApiCall(Objects.requireNonNull(changePasswordBinding.etOldPassword.getText()).toString().trim(),
                    Objects.requireNonNull(changePasswordBinding.etNewPassword.getText()).toString().trim(),
                    Objects.requireNonNull(changePasswordBinding.etConfirmPassword.getText()).toString().trim());
        }
    }

    @Override
    public void onSuccessChangePasswordApiCall(ChangePasswordResponse changePasswordResponse) {
        getController().riderUpdateStauts(getSessionManager().getLoginToken(), "Offline");
        Toast.makeText(mActivity, changePasswordResponse.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailureChangePasswordApiCall(String message) {
        Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessRiderUpdateStatusApiCall() {
        new SessionManager(getContext()).clearAllSharedPreferences();
        NavigationActivity.getInstance().stopBatteryLevelLocationService();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        NavigationActivity.getInstance().finish();
        getActivity().overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    public void onFailureRiderUpdateStatusApiCall(String message) {

    }

    @Override
    public void onLogout() {
        getSessionManager().clearAllSharedPreferences();
        NavigationActivity.getInstance().stopBatteryLevelLocationService();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
        getActivity().overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    public ChangePasswordFragmentController getController() {
        return new ChangePasswordFragmentController(getContext(), this);
    }
}