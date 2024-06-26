package com.apollo.epos.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.databinding.DataBindingUtil;

import com.apollo.epos.R;
import com.apollo.epos.activity.signature.SignatureView;
import com.apollo.epos.databinding.ActivityCaptureSignatureBinding;
import com.apollo.epos.service.FloatingTouchService;
import com.apollo.epos.utils.CommonUtils;
import com.novoda.merlin.Merlin;

import java.io.ByteArrayOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CaptureSignatureActivity extends BaseActivity {
    @BindView(R.id.signature_view)
    protected SignatureView signatureView;
    private Bitmap bitMap;
    private ActivityCaptureSignatureBinding captureSignatureBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        captureSignatureBinding = DataBindingUtil.setContentView(this, R.layout.activity_capture_signature);
        ButterKnife.bind(this);
        if (getIntent() != null) {
            String orderNumber = (String) getIntent().getStringExtra("order_number");
            captureSignatureBinding.orderNumber.setText(orderNumber);
            String customerName = (String) getIntent().getStringExtra("customer_name");
            captureSignatureBinding.customerName.setText(customerName);
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setCustomView(R.layout.custom_action_bar);
            View view = getSupportActionBar().getCustomView();
            ImageView backArrow = view.findViewById(R.id.back_arrow);
            TextView activityName = view.findViewById(R.id.activity_title);
            ImageView closeArrow = view.findViewById(R.id.close_arrow);
            LinearLayout notificationLayout = view.findViewById(R.id.notification_layout);
            backArrow.setVisibility(View.GONE);
            closeArrow.setVisibility(View.VISIBLE);
            backArrow.setOnClickListener(v -> {
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            });
            closeArrow.setOnClickListener(v -> {
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            });
            activityName.setText("CAPTURE SIGNATURE");
        }
    }

    @Override
    protected Merlin createMerlin() {
        return new Merlin.Builder()
                .withConnectableCallbacks()
                .withDisconnectableCallbacks()
                .withBindableCallbacks()
                .build(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @OnClick(R.id.back_layout)
    void onBackClick() {
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @OnClick(R.id.clear_layout)
    void onClearClick() {
        signatureView.clearCanvas();
    }

    @OnClick(R.id.save_layout)
    void onSaveClick() {
        if (signatureView.isSigned) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitMap = signatureView.getSignatureBitmap();
//                path = saveImage(bitmap);
            bitMap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            Intent intent = new Intent();
            intent.putExtra("capturedSignature", byteArray);
            setResult(2, intent);
            finish();
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        }else {
            Toast.makeText(this, "Not signed yet", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        CommonUtils.CURRENT_SCREEN = getClass().getSimpleName();
        startService(new Intent(CaptureSignatureActivity.this, FloatingTouchService.class));
    }
}