package com.apollo.epos.fragment.profile;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.epos.R;
import com.apollo.epos.adapter.CustomReasonAdapter;
import com.apollo.epos.base.BaseFragment;
import com.apollo.epos.databinding.BottomSheetBinding;
import com.apollo.epos.databinding.DialogZoomImageBinding;
import com.apollo.epos.databinding.FragmentProfileBinding;
import com.apollo.epos.fragment.profile.adapter.IdentityProofsAdapter;
import com.apollo.epos.fragment.profile.model.ComplaintReasonsListResponse;
import com.apollo.epos.model.GetRiderProfileResponse;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileFragment extends BaseFragment implements ProfileFragmentCallback {
    private Activity mActivity;
    private FragmentProfileBinding profileBinding;
    @BindView(R.id.user_image)
    ImageView userImage;
    String[] complaintReasons = {"Wrong phone number", "Vehicle registration number is wrong", "Update RC", "Wrong bank account number"};
    List<ComplaintReasonsListResponse.Row> complaintReasonsList = new ArrayList<>();

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
        profileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        return profileBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setup();
    }

    private void setup() {
        new ProfileFragmentController(mActivity, this).getComplaintReasonsListApiCall();
        if (getSessionManager().getRiderProfileResponse() != null) {
            onSuccessGetProfileDetailsApi(getSessionManager().getRiderProfileResponse());
        } else {
            new ProfileFragmentController(mActivity, this).getRiderProfileDetailsApi();
        }
    }

    private String complaintReason;

    @OnClick(R.id.btn_complaint_box)
    void onComplaintClick() {
        BottomSheetDialog dialog = new BottomSheetDialog(mActivity);
        BottomSheetBinding bottomSheetBinding = DataBindingUtil.inflate(LayoutInflater.from(mActivity), R.layout.bottom_sheet, null, false);
        dialog.setContentView(bottomSheetBinding.getRoot());
        dialog.setCancelable(false);
        dialog.show();

        bottomSheetBinding.sheetHeader.setText(getResources().getString(R.string.label_order_complaint));
        bottomSheetBinding.cancelOrderHeader.setText(getResources().getString(R.string.label_complaint_reason_header));
        bottomSheetBinding.closeIcon.setOnClickListener(v -> {
            dialog.dismiss();
        });
        bottomSheetBinding.cancelOrderSendBtn.setOnClickListener(v -> {
            if (complaintReasonsList != null && complaintReasonsList.size() > 0) {
                for (ComplaintReasonsListResponse.Row row : complaintReasonsList) {
                    if (row.getName().equals(complaintReason)) {
                        new ProfileFragmentController(mActivity, ProfileFragment.this).riderComplaintSaveUpdateApiCall(row.getUid(), bottomSheetBinding.comment.getText().toString().trim());
                        dialog.dismiss();
                        break;
                    }
                }
            }

        });
        CustomReasonAdapter customAdapter = new CustomReasonAdapter(mActivity, complaintReasons, null);
        bottomSheetBinding.rejectReasonSpinner.setAdapter(customAdapter);
        bottomSheetBinding.rejectReasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                complaintReason = complaintReasons[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onFialureMessage(String message) {
        Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessGetProfileDetailsApi(GetRiderProfileResponse getRiderProfileResponse) {
        if (getRiderProfileResponse != null) {
            if (getRiderProfileResponse != null && getRiderProfileResponse.getData() != null && getRiderProfileResponse.getData().getPic() != null && getRiderProfileResponse.getData().getPic().size() > 0)
                Glide.with(getContext()).load(getSessionManager().getrRiderIconUrl()).circleCrop().error(R.drawable.apollo_app_logo).into(profileBinding.userImage);
            profileBinding.employeeId.setText(getRiderProfileResponse.getData().getLoginUnique());
            profileBinding.riderName.setText(getRiderProfileResponse.getData().getFirstName() + " " + getRiderProfileResponse.getData().getLastName());
            profileBinding.riderPhoneNumber.setText("+91 " + getRiderProfileResponse.getData().getPhone());
            profileBinding.riderDob.setText(getRiderProfileResponse.getData().getDob());
//            profileBinding.areaofOperation.setText(getRiderProfileResponse.getData().getUserAddInfo().getAreaOfOp());
            profileBinding.address.setText(getRiderProfileResponse.getData().getUserAddress().getCurrentAddress());
            profileBinding.vehicleModel.setText(getRiderProfileResponse.getData().getUserAddInfo().getManufacturer());
            profileBinding.vehicleMakingYear.setText(getRiderProfileResponse.getData().getUserAddInfo().getModel());
            profileBinding.vehicleRegistrationNumber.setText(getRiderProfileResponse.getData().getUserAddInfo().getVehicleNo());

            if (getRiderProfileResponse.getData().getUserAddInfo() != null
                    && getRiderProfileResponse.getData().getUserAddInfo().getIdentificationProofs() != null
                    && getRiderProfileResponse.getData().getUserAddInfo().getIdentificationProofs().size() > 0) {
                profileBinding.documentInfoText.setVisibility(View.VISIBLE);
                IdentityProofsAdapter identityProofsAdapter = new IdentityProofsAdapter(getContext(), getRiderProfileResponse.getData().getUserAddInfo().getIdentificationProofs(), this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                profileBinding.identityProofsList.setLayoutManager(mLayoutManager);
                profileBinding.identityProofsList.setItemAnimator(new DefaultItemAnimator());
                profileBinding.identityProofsList.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
                profileBinding.identityProofsList.setItemAnimator(new DefaultItemAnimator());
                profileBinding.identityProofsList.setAdapter(identityProofsAdapter);

            } else {
                profileBinding.documentInfoText.setVisibility(View.GONE);
            }
            StringBuilder language = new StringBuilder();
            if (getRiderProfileResponse.getData().getUserAddInfo() != null && getRiderProfileResponse.getData().getUserAddInfo().getLanguage() != null)
                for (GetRiderProfileResponse.Language lng : getRiderProfileResponse.getData().getUserAddInfo().getLanguage()) {
                    language.append(lng.getName()).append(", ");
                }
            profileBinding.languages.setText((language.length() == 0) ? "--" : new StringBuilder(language.substring(0, language.length() - 2)).append("."));
        }
    }

    @Override
    public void onFailureGetProfileDetailsApi(String message) {
        Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClickIdentityProof(GetRiderProfileResponse.IdentificationProof identificationProof) {
        Dialog dialog = new Dialog(getContext(), R.style.fadeinandoutcustomDialog);
        DialogZoomImageBinding dialogZoomImageBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.dialog_zoom_image, null, false);
        dialog.setContentView(dialogZoomImageBinding.getRoot());
        dialogZoomImageBinding.headerNameText.setText(identificationProof.getDocType().getName().substring(0, 1).toUpperCase() + identificationProof.getDocType().getName().substring(1, identificationProof.getDocType().getName().length()).toLowerCase());
        Glide.with(getContext())
                .load(identificationProof.getDoc().get(0).getFullPath())
                .error(R.drawable.drivinglicense)
                .into(dialogZoomImageBinding.zoomingImg);
        dialogZoomImageBinding.backArrow.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    public void onSuccessComplaintSaveUpdate(String message) {
        Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessComplaintReasonsListApiCall(ComplaintReasonsListResponse complaintReasonsListResponse) {
        try {
            complaintReasons = new String[complaintReasonsListResponse.getData().getListData().getRows().size()];
            this.complaintReasonsList = complaintReasonsListResponse.getData().getListData().getRows();
            for (ComplaintReasonsListResponse.Row row : complaintReasonsListResponse.getData().getListData().getRows())
                complaintReasons[complaintReasonsListResponse.getData().getListData().getRows().indexOf(row)] = row.getName();

        } catch (Exception e) {
            System.out.println("onSuccessDeliveryReasonApiCall:::::::::::::::::::::::::::::::" + e.getMessage());
        }
    }
}