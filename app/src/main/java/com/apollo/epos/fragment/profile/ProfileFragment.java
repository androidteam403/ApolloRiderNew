package com.apollo.epos.fragment.profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.apollo.epos.R;
import com.apollo.epos.activity.NavigationActivity;
import com.apollo.epos.adapter.CustomReasonAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONObject;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileFragment extends Fragment {
    private Activity mActivity;
    @BindView(R.id.user_image)
    ImageView userImage;
    String[] complaintReasons = {"Wrong phone number", "Vehicle registration number is wrong", "Update RC", "Wrong bank account number"};

    private ProfileViewModel profileViewModel;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        Glide.with(mActivity)
                .load(getResources().getDrawable(R.drawable.naveeb_circle))
                .apply(RequestOptions.circleCropTransform())
                .into(userImage);


//        ImageView userImage =
//        sharedPref = new SharedPreferenceUtils(mActivity);
//        networkCall = new NetworkCall(mActivity);
//        appIcon.setVisibility(View.GONE);
//        bottomLayout.setVisibility(View.VISIBLE);
//        mReceiver = new MyResultReceiver(new Handler());
//        mReceiver.setReceiver(ChangePasswordFragment.this);
//        if (Constants.IS_REMOTE_SUPPORT_REQUIRED) {
//            if (Boolean.parseBoolean(sharedPref.getKeyValue(Constants.IS_MANUAL_ONLINE_MODE))) {
//                try {
//                    ChatApplication app = (ChatApplication) Objects.requireNonNull(mActivity).getApplication();
//                    mSocket = app.getSocket();
//                    mSocket.on("notification-count-response", onNotificationCountResponse);
//                    JSONObject obj1 = new JSONObject();
//                    obj1.put("userId", Integer.parseInt(sharedPref.getKeyValue(Constants.CHAT_LOGIN_USER_ID)));
//                    mSocket.emit("lastSeenUpdate", obj1);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                mRegistrationBroadcastReceiver = new BroadcastReceiver() {
//                    @SuppressLint("NewApi")
//                    @Override
//                    public void onReceive(Context context, Intent intent) {
//                        if (Objects.equals(intent.getAction(), Constants.PUSH_NOTIFICATION)) {
//                            handleNotificationMethod();
//                        }
//                    }
//                };
//            }
//        }
    }

    @OnClick(R.id.btn_complaint_box)
    void onComplaintClick() {
        View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
        BottomSheetDialog dialog = new BottomSheetDialog(mActivity);
        dialog.setContentView(dialogView);
        dialog.setCancelable(false);
        dialog.show();

        TextView headerText = dialogView.findViewById(R.id.sheet_header);
        headerText.setText(getResources().getString(R.string.label_order_complaint));
        TextView cancelHeaderText = dialogView.findViewById(R.id.cancel_order_header);
        cancelHeaderText.setText(getResources().getString(R.string.label_complaint_reason_header));
        ImageView closeDialog = dialogView.findViewById(R.id.close_icon);
        closeDialog.setOnClickListener(v -> {
            dialog.dismiss();
        });

        Spinner reasonSpinner = dialogView.findViewById(R.id.rejectReasonSpinner);
        CustomReasonAdapter customAdapter = new CustomReasonAdapter(mActivity, complaintReasons);
        reasonSpinner.setAdapter(customAdapter);
        reasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(mActivity," "+reasonSpinner[position])
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}