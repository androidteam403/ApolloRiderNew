package com.apollo.epos.fragment.profile;

import com.apollo.epos.fragment.profile.model.ComplaintReasonsListResponse;
import com.apollo.epos.model.GetRiderProfileResponse;

public interface ProfileFragmentCallback {
    void onFialureMessage(String message);

    void onSuccessGetProfileDetailsApi(GetRiderProfileResponse getRiderProfileResponse);

    void onFailureGetProfileDetailsApi(String message);

    void onItemClickIdentityProof(GetRiderProfileResponse.IdentificationProof identificationProof);

    void onSuccessComplaintSaveUpdate(String message);

    void onSuccessComplaintReasonsListApiCall(ComplaintReasonsListResponse complaintReasonsListResponse);

}
