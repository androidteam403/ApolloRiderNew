package com.apollo.epos.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.apollo.epos.R;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.kyanogen.signatureview.SignatureView;

import java.io.ByteArrayOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CaptureSignatureActivity extends AppCompatActivity {
    @BindView(R.id.signature_view)
    protected SignatureView signatureView;
    private Bitmap bitMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_signature);
        ButterKnife.bind(this);
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
    }
}