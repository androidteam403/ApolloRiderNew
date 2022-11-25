package com.apollo.epos.fragment.complaints;

import com.apollo.epos.fragment.complaints.model.ComplaintsResponse;
import com.apollo.epos.fragment.profile.model.ComplaintReasonsListResponse;

public interface ComplaintsFragmentCallback {
    void onSuccessGetComplaintsListApiCall(ComplaintsResponse complaintsResponse);

    void onFailureMessage(String message);

    void onLogout();

    void onClickComplaint();

    void onSuccessComplaintSaveUpdate(String message);

    void onSuccessComplaintReasonsListApiCall(ComplaintReasonsListResponse complaintReasonsListResponse);

    void complaintResolvedCallback();
}
