package com.apollo.epos.fragment.profile;

import com.apollo.epos.model.GetRiderProfileResponse;

public interface ProfileFragmentCallback {
    void onSuccessGetProfileDetailsApi(GetRiderProfileResponse getRiderProfileResponse);

    void onFailureGetProfileDetailsApi(String message);

    void onItemClickIdentityProof(GetRiderProfileResponse.IdentificationProof identificationProof);
}
