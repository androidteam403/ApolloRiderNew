package com.apollo.epos.fragment.help;

import android.content.Context;

import com.apollo.epos.db.SessionManager;
import com.apollo.epos.fragment.help.model.RiderBasicDetailsforHelpResponse;
import com.apollo.epos.network.ApiClient;
import com.apollo.epos.network.ApiInterface;
import com.apollo.epos.service.NetworkUtils;
import com.apollo.epos.utils.ActivityUtils;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HelpFragmentController {
    private Context context;
    private HelpFragmentCallback mListener;

    public HelpFragmentController(Context context, HelpFragmentCallback mListener) {
        this.context = context;
        this.mListener = mListener;
    }

}
