package com.apollo.epos.fragment.help;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.apollo.epos.BuildConfig;
import com.apollo.epos.R;
import com.apollo.epos.activity.navigation.NavigationActivity;
import com.apollo.epos.base.BaseFragment;
import com.apollo.epos.databinding.FragmentHelpBinding;
import com.apollo.epos.db.SessionManager;
import com.apollo.epos.utils.ActivityUtils;

@SuppressLint("SetJavaScriptEnabled")
public class HelpFragment extends BaseFragment {
    private FragmentHelpBinding helpBinding;

    public static HelpFragment newInstance() {
        return new HelpFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        helpBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_help, container, false);
        return helpBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavigationActivity.getInstance().setTitle(R.string.menu_help);
        ActivityUtils.showDialog(getActivity(), "Please Wait");
        helpBinding.webView.getSettings().setJavaScriptEnabled(true);
        helpBinding.webView.getSettings().setDomStorageEnabled(true);
        helpBinding.webView.loadUrl(BuildConfig.HELP_BASE_URL + getSessionManager().getRiderProfileResponse().getData().getUid());
        helpBinding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                ActivityUtils.showDialog(getActivity(), "Please Wait");
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                return super.shouldOverrideUrlLoading(view, url);
                if (url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                } else if (url.startsWith("mailto:")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;

                }
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                ActivityUtils.hideDialog();
            }
        });
    }

    public SessionManager getSessionManager() {
        return new SessionManager(getContext());
    }
}