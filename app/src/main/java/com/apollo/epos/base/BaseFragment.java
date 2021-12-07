package com.apollo.epos.base;
/*
 * Created on : oct 29, 2021.
 * Author : NAVEEN.M
 */

import androidx.fragment.app.Fragment;

import com.apollo.epos.db.SessionManager;
import com.google.android.gms.common.util.JsonUtils;

public abstract class BaseFragment extends Fragment {

    public SessionManager getSessionManager() {
        return new SessionManager(getContext());
    }

}