package com.apollo.epos.fragment.help;

import com.apollo.epos.fragment.help.model.RiderBasicDetailsforHelpResponse;

public interface HelpFragmentCallback {

    void onFailureMessage(String message);

    void onSuccessRiderBasicDetailsforHelpApiCall(RiderBasicDetailsforHelpResponse riderBasicDetailsforHelpResponse);

    void onFailureRiderBasicDetailsforHelpApiCall(String message);
}
