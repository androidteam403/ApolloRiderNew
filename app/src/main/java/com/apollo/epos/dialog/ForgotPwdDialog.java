package com.apollo.epos.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.apollo.epos.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgotPwdDialog extends Dialog {
    private Activity activity;
    @BindView(R.id.forgot_details_layout)
    RelativeLayout forgotDetailsLayout;
    @BindView(R.id.forgot_details_email)
    EditText forgotMailEditText;
    private ProgressDialog barProgressDialog = null;

    public ForgotPwdDialog(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_forgot_pwd);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.close_button)
    void onClickCloseDialog() {
        dismiss();
    }

    @OnClick(R.id.forgot_details_reset_button)
    void onResetBtnClicked() {
        dismiss();
    }
}
