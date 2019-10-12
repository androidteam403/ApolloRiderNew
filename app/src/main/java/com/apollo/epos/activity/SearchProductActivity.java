package com.apollo.epos.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.epos.R;
import com.apollo.epos.adapter.SearchedItemAdapter;
import com.apollo.epos.fragment.profile.ProfileViewModel;
import com.apollo.epos.fragment.takeneworder.OnItemClickListener;
import com.apollo.epos.model.OrderItemModel;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.apollo.epos.utils.ActivityUtils.decodeImageUri;

public class SearchProductActivity extends AppCompatActivity implements OnItemClickListener {
    @BindView(R.id.searched_product_name)
    protected TextView searchedProductName;
    @BindView(R.id.searchedItemRecyclerView)
    RecyclerView searchedItemRecyclerView;
    ArrayList<OrderItemModel> medicineList = new ArrayList<>();
    private SearchedItemAdapter searchedItemsAdapter;
    @BindView(R.id.add_to_cart_text)
    protected TextView addToCartText;
    private TextView cartCount;

    @BindView(R.id.search_camera_layout)
    protected LinearLayout searchCameraLayout;
    @BindView(R.id.search_barcode_layout)
    protected LinearLayout searchBarcodeLayout;
    @BindView(R.id.search_voice_layout)
    protected LinearLayout searchVoiceLayout;

    public static final int CAM_REQUEST = 1;
    public static final int GAL_REQUEST = 2;
    private int SIGNATURE_REQUEST_CODE = 3;
    private final int MY_PERMISSIONS_RECORD_AUDIO = 5;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    Intent camera_intent, gal_intent;
    private File file;
    private boolean imageStatus = false;
    private Bitmap bp;
    private ProfileViewModel takeNewOrderViewModel;
    private boolean isTextInput = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);
        ButterKnife.bind(this);


        Intent intent = getIntent();
        if (intent != null) {
            String productName = Objects.requireNonNull(intent.getExtras()).getString("SEARCHED_PRODUCT");
            searchedProductName.setText("Searched Product: " + productName);
        }

//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//            getSupportActionBar().setDisplayShowCustomEnabled(true);
//            getSupportActionBar().setElevation(0);
//        }

        setSearchedItemsList();
    }

    private void setSearchedItemsList() {
        getMedicineList();
        searchedItemsAdapter = new SearchedItemAdapter(this, medicineList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        searchedItemRecyclerView.setLayoutManager(mLayoutManager);
        searchedItemRecyclerView.setItemAnimator(new DefaultItemAnimator());
        searchedItemRecyclerView.setAdapter(searchedItemsAdapter);
    }

    private void getMedicineList() {
        medicineList.clear();
        OrderItemModel item = new OrderItemModel();
        item.setMedicineName("Dolamide Tab");
        item.setMedicineDesc("10 Tablet(s) in a strip");
        item.setQty("2");
        item.setItemTotalPrice("108.80");
        item.setSelected(false);
        medicineList.add(item);

        item = new OrderItemModel();
        item.setMedicineName("Atripla");
        item.setMedicineDesc("10 Tablet(s) in a strip");
        item.setQty("1");
        item.setItemTotalPrice("72.80");
        item.setSelected(false);
        medicineList.add(item);
    }


    boolean isItemSelected = false;
    private int selectedCnt = 0;

    @Override
    public void onItemClick(int position, boolean isSelected) {
        medicineList.get(position).setSelected(isSelected);
        searchedItemsAdapter.notifyDataSetChanged();
        ArrayList<OrderItemModel> cancelledMedicineList = new ArrayList<>();
        for (OrderItemModel item : medicineList) {
            if (item.isSelected()) {
                cancelledMedicineList.add(item);
            }
        }
        for (OrderItemModel item : medicineList) {
            if (item.isSelected()) {
                isItemSelected = true;
                break;
            } else {
                isItemSelected = false;
            }
        }
        if (isItemSelected) {
            addToCartText.setBackground(getDrawable(R.drawable.add_cart_bg));
        } else {
            cartCount.setText("0");
            addToCartText.setBackground(getDrawable(R.drawable.add_cart_unselect_bg));
        }
    }

    @Override
    public void onDecrementClick(int position) {

    }

    @Override
    public void onIncrementClick(int position) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @OnClick(R.id.add_to_cart_text)
    void onAddToCartClick() {
        selectedCnt = 0;
        for (OrderItemModel item : medicineList) {
            if (item.isSelected()) {
                selectedCnt++;
            }
        }
        cartCount.setText(String.valueOf(selectedCnt));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_cart, menu);

        final MenuItem menuNotificationItem = menu.findItem(R.id.action_cart_icon);
        View actionCartView = MenuItemCompat.getActionView(menuNotificationItem);
        TextView notificationText = actionCartView.findViewById(R.id.notification_text);
        ImageView notifyImage = actionCartView.findViewById(R.id.notify_image);
        cartCount = actionCartView.findViewById(R.id.cart_count_text);
        actionCartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuNotificationItem);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                return true;
            case R.id.action_cart_icon:
                Intent i = new Intent(this, CartActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.search_camera_layout)
    void onCaptureImageClick() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        cameraPermissionSetting();
    }

    private void cameraPermissionSetting() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                Log.d("Permissions_Status", "Permissions not granted");
               /* if (ContextCompat.checkSelfPermission(CameraFunctionality.this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED)*/
                // ask for permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
            } else {
                Log.d("Permissions_Status", "We have a permission");
                // we have a permission
                imagePickerAction();
            }
        } else {
            imagePickerAction();
        }
    }

    private void imagePickerAction() {
        camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = new File(Environment.getExternalStorageDirectory(), "file" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        startActivityForResult(camera_intent, CAM_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == CAM_REQUEST && resultCode == RESULT_OK) {
//            imageStatus = true;
//            Bundle bundle = data.getExtras();
//            Bitmap bitmap = bundle.getParcelable("data");
//            searchProductBtn.setVisibility(View.VISIBLE);
//            capturedPreviewImage.setVisibility(View.VISIBLE);
//            capturedPreviewImage.setImageBitmap(bitmap);
//            bp = (Bitmap) data.getExtras().get("data");
//            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//            bp.compress(Bitmap.CompressFormat.JPEG, 80, bytes);
//        } else if (requestCode == GAL_REQUEST) {
//            if (data != null) {
//                imageStatus = true;
//                Uri choosenImage = data.getData();
//                if (choosenImage != null) {
//                    searchProductBtn.setVisibility(View.VISIBLE);
//                    capturedPreviewImage.setVisibility(View.VISIBLE);
//                    bp = decodeImageUri(mActivity, choosenImage, 80);
//                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//                    bp.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
//                    capturedPreviewImage.setImageBitmap(bp);
//                }
//            } else if (requestCode == 3) {
//                if (data == null) {
//                    imageStatus = true;
//                }
//            }
//        } else if (requestCode == REQ_CODE_SPEECH_INPUT) {
//            if(data != null){
//                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//                searchProductBtn.setVisibility(View.VISIBLE);
//                scannedBarCode.setVisibility(View.VISIBLE);
//                searchedProductName = result.get(0);
//                scannedBarCode.setText("Product Name: " + result.get(0));
//            }
//        } else {
//            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//            if (result != null) {
//                if (result.getContents() == null) {
////                Toast.makeText(this, "Scan Cancelled", Toast.LENGTH_LONG).show();
//                } else {
//                    searchProductBtn.setVisibility(View.VISIBLE);
//                    scannedBarCode.setVisibility(View.VISIBLE);
//                    searchedProductName = result.getContents();
//                    scannedBarCode.setText("Scanned Bar Code: " + result.getContents());
//                }
//            } else {
//                super.onActivityResult(requestCode, resultCode, data);
//            }
//        }
    }
}
