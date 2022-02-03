package com.apollo.epos.fragment.profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.apollo.epos.utils.CommonUtils;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;

public class ProfileFragment extends BaseFragment implements ProfileFragmentCallback {
    private Activity mActivity;
    private FragmentProfileBinding profileBinding;
    String[] complaintReasons = {"Wrong phone number", "Vehicle registration number is wrong", "Update RC", "Wrong bank account number"};
    List<ComplaintReasonsListResponse.Row> complaintReasonsList = new ArrayList<>();
    private String complaintReason;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        profileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        return profileBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setup();
    }

    private void setup() {
        profileBinding.setCallback(this);
        getController().getComplaintReasonsListApiCall();
        if (getSessionManager().getRiderProfileResponse() != null) {
            onSuccessGetProfileDetailsApi(getSessionManager().getRiderProfileResponse());
        } else {
            getController().getRiderProfileDetailsApi();
        }
    }

    @Override
    public void onClickComplaint() {
        BottomSheetDialog dialog = new BottomSheetDialog(mActivity);
        BottomSheetBinding bottomSheetBinding = DataBindingUtil.inflate(LayoutInflater.from(mActivity), R.layout.bottom_sheet, null, false);
        dialog.setContentView(bottomSheetBinding.getRoot());
        dialog.setCancelable(false);
        dialog.show();

        bottomSheetBinding.sheetHeader.setText(getResources().getString(R.string.label_order_complaint));
        bottomSheetBinding.cancelOrderHeader.setText(getResources().getString(R.string.label_complaint_reason_header));
        bottomSheetBinding.closeIcon.setOnClickListener(v -> dialog.dismiss());
        bottomSheetBinding.cancelOrderSendBtn.setOnClickListener(v -> {
            if (complaintReasonsList != null && complaintReasonsList.size() > 0) {
                for (ComplaintReasonsListResponse.Row row : complaintReasonsList) {
                    if (row.getName().equals(complaintReason)) {
                        getController().riderComplaintSaveUpdateApiCall(row.getUid(), bottomSheetBinding.comment.getText().toString().trim());
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onSuccessGetProfileDetailsApi(GetRiderProfileResponse getRiderProfileResponse) {
        try {
            if (getRiderProfileResponse != null && getRiderProfileResponse.getData() != null) {
                if (getRiderProfileResponse.getData().getPic() != null && getRiderProfileResponse.getData().getPic().size() > 0)
                    if (getContext() != null)
                        Glide.with(getContext()).load(getSessionManager().getrRiderIconUrl()).circleCrop().error(R.drawable.apollo_app_logo).into(profileBinding.userImage);
                profileBinding.employeeId.setText(getRiderProfileResponse.getData().getLoginUnique());
                profileBinding.riderName.setText(getRiderProfileResponse.getData().getFirstName() + " " + getRiderProfileResponse.getData().getLastName());
                profileBinding.riderPhoneNumber.setText("+91 " + getRiderProfileResponse.getData().getPhone());

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String orderDate = getRiderProfileResponse.getData().getDob();
                Date orderDates = formatter.parse(orderDate);
                long orderDateMills = orderDates.getTime();
                if (CommonUtils.getTimeFormatter(orderDateMills).endsWith("."))
                    profileBinding.riderDob.setText(CommonUtils.getTimeFormatter(orderDateMills).substring(0, CommonUtils.getTimeFormatter(orderDateMills).length() - 10));
                else
                    profileBinding.riderDob.setText(CommonUtils.getTimeFormatter(orderDateMills).substring(0, CommonUtils.getTimeFormatter(orderDateMills).length() - 8));

                String currentAddress = getRiderProfileResponse.getData().getUserAddress().getCurrentAddress() + ", "
                        + getRiderProfileResponse.getData().getUserAddress().getCaCity() + ", "
                        + getRiderProfileResponse.getData().getUserAddress().getCaState().getName() + ", "
                        + getRiderProfileResponse.getData().getUserAddress().getCaPincode() + ", "
                        + getRiderProfileResponse.getData().getUserAddress().getCaCountry().getName();
                profileBinding.currentAddress.setText(currentAddress);
                String permanentAddress = getRiderProfileResponse.getData().getUserAddress().getPermanentAddress() + ", "
                        + getRiderProfileResponse.getData().getUserAddress().getPaCity() + ", "
                        + getRiderProfileResponse.getData().getUserAddress().getPaState().getName() + ", "
                        + getRiderProfileResponse.getData().getUserAddress().getPaPincode() + ", "
                        + getRiderProfileResponse.getData().getUserAddress().getPaCountry().getName();
                profileBinding.permanentAddress.setText(permanentAddress);

                profileBinding.vehicleType.setText(getRiderProfileResponse.getData().getUserAddInfo().getVehicleType().getName());
                profileBinding.vehicleNumber.setText(getRiderProfileResponse.getData().getUserAddInfo().getVehicleNo());
                profileBinding.model.setText(getRiderProfileResponse.getData().getUserAddInfo().getModel());
                profileBinding.capacity.setText(String.valueOf(getRiderProfileResponse.getData().getUserAddInfo().getCapacityUnits()));
                profileBinding.manufacturer.setText(getRiderProfileResponse.getData().getUserAddInfo().getManufacturer());

                if (getRiderProfileResponse.getData().getUserAddInfo() != null
                        && getRiderProfileResponse.getData().getUserAddInfo().getIdentificationProofs() != null
                        && getRiderProfileResponse.getData().getUserAddInfo().getIdentificationProofs().size() > 0) {
                    profileBinding.documentInfoText.setVisibility(View.VISIBLE);
                    IdentityProofsAdapter identityProofsAdapter = new IdentityProofsAdapter(getContext(), getRiderProfileResponse.getData().getUserAddInfo().getIdentificationProofs(), this);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                    profileBinding.identityProofsList.setLayoutManager(mLayoutManager);
                    profileBinding.identityProofsList.setItemAnimator(new DefaultItemAnimator());
                    if (getContext() != null)
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
        } catch (Exception e) {
            System.out.println("Rider Profile details:::::::::::::::::::::::::::::::::" + e.getMessage());
        }
    }

    @Override
    public void onFailureGetProfileDetailsApi(String message) {
        Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onItemClickIdentityProof(GetRiderProfileResponse.IdentificationProof identificationProof) {
        if (getContext() != null) {
            Dialog dialog = new Dialog(getContext(), R.style.fadeinandoutcustomDialog);
            DialogZoomImageBinding dialogZoomImageBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                    R.layout.dialog_zoom_image, null, false);
            dialog.setContentView(dialogZoomImageBinding.getRoot());
            dialogZoomImageBinding.headerNameText.setText(identificationProof.getDocType().getName().substring(0, 1).toUpperCase() + identificationProof.getDocType().getName().substring(1).toLowerCase());
            Glide.with(getContext())
                    .load(identificationProof.getDoc().get(0).getFullPath())
                    .error(R.drawable.drivinglicense)
                    .into(dialogZoomImageBinding.zoomingImg);
            dialogZoomImageBinding.backArrow.setOnClickListener(v -> dialog.dismiss());
            dialog.show();
        }
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

    private ProfileFragmentController getController() {
        return new ProfileFragmentController(getContext(), this);
    }
}