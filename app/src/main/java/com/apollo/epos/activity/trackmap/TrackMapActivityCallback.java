package com.apollo.epos.activity.trackmap;

import com.apollo.epos.activity.trackmap.model.OrderEndJourneyUpdateResponse;
import com.apollo.epos.activity.trackmap.model.OrderStartJourneyUpdateResponse;

public interface TrackMapActivityCallback {

    void onFialureMessage(String message);

    void onSuccessOrderStartJourneyUpdateApiCall(OrderStartJourneyUpdateResponse orderStartJourneyUpdateResponse);

    void onFailureOrderStartJourneyUpdateApiCall(String message);

    void onSuccessOrderEndJourneyUpdateApiCall(OrderEndJourneyUpdateResponse orderEndJourneyUpdateResponse);

    void onFailureOrderEndJourneyUpdateApiCall(String message);
}
