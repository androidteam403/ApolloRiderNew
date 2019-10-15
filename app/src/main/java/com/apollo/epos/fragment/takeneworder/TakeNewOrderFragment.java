package com.apollo.epos.fragment.takeneworder;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.epos.R;
import com.apollo.epos.activity.NavigationActivity;
import com.apollo.epos.activity.ScannerActivity;
import com.apollo.epos.adapter.SearchedItemAdapter;
import com.apollo.epos.fragment.profile.ProfileViewModel;
import com.apollo.epos.model.OrderItemModel;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.apollo.epos.utils.ActivityUtils.convertSpannableStringSizes;
import static com.apollo.epos.utils.ActivityUtils.decodeImageUri;
import static com.apollo.epos.utils.ActivityUtils.hideDialog;
import static com.apollo.epos.utils.ActivityUtils.hideKeyboardFrom;
import static com.apollo.epos.utils.ActivityUtils.showDialog;
import static com.apollo.epos.utils.ActivityUtils.showSoftKeyboard;

public class TakeNewOrderFragment extends Fragment implements OnItemClickListener {
    private Activity mActivity;
    private static final String TAG = "Take New Order Fragment";

    @BindView(R.id.search_layout)
    protected LinearLayout searchLayout;
    @BindView(R.id.search_product_img)
    protected ImageView searchProductImg;
    @BindView(R.id.custom_search_layout)
    protected LinearLayout customSearchLayout;
    @BindView(R.id.et_search_product)
    protected EditText searchProductEditText;
    @BindView(R.id.close_custom_search_layout)
    protected ImageView closeCustomSearchLayout;
    @BindView(R.id.preview_layout)
    protected LinearLayout previewLayout;
    @BindView(R.id.captured_preview_image)
    protected ImageView capturedPreviewImage;
    @BindView(R.id.scanned_bar_code)
    protected TextView scannedBarCode;
    @BindView(R.id.scanned_product)
    protected TextView scannedProduct;
    @BindView(R.id.result_layout)
    protected LinearLayout resultLayout;

    @BindView(R.id.search_camera_layout)
    protected LinearLayout searchCameraLayout;
    @BindView(R.id.search_barcode_layout)
    protected LinearLayout searchBarcodeLayout;
    @BindView(R.id.search_voice_layout)
    protected LinearLayout searchVoiceLayout;

    @BindView(R.id.btm_search_camera_layout)
    protected LinearLayout btmSearchCameraLayout;
    @BindView(R.id.btm_search_barcode_layout)
    protected LinearLayout btmSearchBarcodeLayout;
    @BindView(R.id.btm_search_voice_layout)
    protected LinearLayout btmSearchVoiceLayout;
    @BindView(R.id.camera_title)
    protected TextView cameraTitle;
    @BindView(R.id.barcode_title)
    protected TextView barcodeTitle;
    @BindView(R.id.voice_search_title)
    protected TextView voiceSearchTitle;
    @BindView(R.id.camera_img)
    protected ImageView cameraImg;
    @BindView(R.id.barcode_img)
    protected ImageView barcodeImg;
    @BindView(R.id.voice_img)
    protected ImageView voiceImg;

    @BindView(R.id.search_product_name)
    protected TextView searchProductName;

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
    private String searchedProductName = "";
    private boolean isTextInput = false;

    @BindView(R.id.searchedItemRecyclerView)
    RecyclerView searchedItemRecyclerView;
    ArrayList<OrderItemModel> medicineList = new ArrayList<>();
    private SearchedItemAdapter searchedItemsAdapter;
    @BindView(R.id.add_to_cart_layout)
    protected RelativeLayout addToCartLayout;

    @BindView(R.id.items_scrollView)
    protected ScrollView itemsScrollView;
    @BindView(R.id.search_progressBar)
    protected ProgressBar searchProgressBar;
    @BindView(R.id.results_header)
    protected TextView resultsHeader;
    @BindView(R.id.search_options_center_layout)
    protected LinearLayout searchOptionsCenterLayout;
    @BindView(R.id.search_options_bottom_layout)
    protected LinearLayout searchOptionsBottomLayout;
    private int itemQty = 1;
    @BindView(R.id.header_layout)
    protected LinearLayout headerLayout;
    @BindView(R.id.cart_items_img)
    protected ImageView cartItemsImg;

    public static TakeNewOrderFragment newInstance() {
        return new TakeNewOrderFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_take_order, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

        final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(mActivity);

        final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                //getting all the matches
                ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                //displaying the first match
                if (matches != null) {
                    previewLayout.setVisibility(View.VISIBLE);
                    capturedPreviewImage.setVisibility(View.GONE);
                    scannedProduct.setVisibility(View.GONE);
                    scannedBarCode.setText("Searched Product Name: " + matches.get(0));
                    searchedProductName = matches.get(0);
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        searchProductEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 1) {
                    searchProgressBar.setVisibility(View.VISIBLE);
                    searchOptionsCenterLayout.setVisibility(View.GONE);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            searchProgressBar.setVisibility(View.INVISIBLE);
                            previewLayout.setVisibility(View.GONE);
                            itemsScrollView.setVisibility(View.VISIBLE);
                            resultLayout.setVisibility(View.VISIBLE);
                            searchOptionsBottomLayout.setVisibility(View.VISIBLE);
                            resultsHeader.setText(convertSpannableStringSizes(mActivity, "Found ", String.valueOf(medicineList.size()),
                                    " results for ", searchProductEditText.getText().toString(), 1.0f, 0.9f, R.font.roboto_bold));
//                            resultsHeader.setText(mActivity.getResources().getString(R.string.label_search_results, medicineList.size(), searchProductEditText.getText().toString()));

                            setSearchedItemsList();

                            btmSearchCameraLayout.setBackground(mActivity.getResources().getDrawable(R.drawable.take_order_item_bg));
                            btmSearchBarcodeLayout.setBackground(mActivity.getResources().getDrawable(R.drawable.take_order_item_bg));
                            btmSearchVoiceLayout.setBackground(mActivity.getResources().getDrawable(R.drawable.take_order_item_bg));
                        }
                    }, 1000);
                } else {
                    itemsScrollView.setVisibility(View.GONE);
                }
            }
        });

        setSearchedItemsList();
    }

    @OnClick(R.id.search_layout)
    void onSearchLayoutClick() {
        headerLayout.setBackground(mActivity.getResources().getDrawable(R.drawable.dashboard_order_item_selected_bg));
        searchLayout.setVisibility(View.GONE);
        customSearchLayout.setVisibility(View.VISIBLE);
        showSoftKeyboard(mActivity, searchProductEditText);
    }

    @OnClick(R.id.close_custom_search_layout)
    void onCloseCustomSearchClick() {
        searchProductEditText.setText("");
        customSearchLayout.setVisibility(View.GONE);
        searchLayout.setVisibility(View.VISIBLE);
        headerLayout.setBackground(mActivity.getResources().getDrawable(R.drawable.dashboard_order_item_unselected_bg));
        hideKeyboardFrom(mActivity, searchProductEditText);
    }

    @OnClick(R.id.search_camera_layout)
    void onCaptureImageClick() {
        isTextInput = false;
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        cameraPermissionSetting();
    }

    @OnClick(R.id.btm_search_camera_layout)
    void onBtmCameraClick() {
        isTextInput = false;
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        cameraPermissionSetting();

//        btmSearchCameraLayout.setBackgroundColor(mActivity.getResources().getColor(R.color.colorPrimary));
//        cameraTitle.setTextColor(mActivity.getResources().getColor(R.color.colorWhite));
//        cameraImg.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.icon_camera_unselect));
//        btmSearchBarcodeLayout.setBackgroundColor(mActivity.getResources().getColor(R.color.colorWhite));
//        barcodeTitle.setTextColor(mActivity.getResources().getColor(R.color.colorBlack));
//        barcodeImg.setImageDrawable(mActivity.getDrawable(R.drawable.icon_sacnner_unselect));
//        btmSearchVoiceLayout.setBackgroundColor(mActivity.getResources().getColor(R.color.colorWhite));
//        voiceSearchTitle.setTextColor(mActivity.getResources().getColor(R.color.colorBlack));
//        voiceImg.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.icon_voice_unselect));
    }

    @OnClick(R.id.search_barcode_layout)
    void scanBarCodeClick() {
        isTextInput = false;
        new IntentIntegrator(mActivity).setCaptureActivity(ScannerActivity.class).initiateScan();
        mActivity.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    @OnClick(R.id.btm_search_barcode_layout)
    void onBtmScanBarClick() {
        isTextInput = false;
        new IntentIntegrator(mActivity).setCaptureActivity(ScannerActivity.class).initiateScan();
        mActivity.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

//        btmSearchCameraLayout.setBackgroundColor(mActivity.getResources().getColor(R.color.colorWhite));
//        cameraTitle.setTextColor(mActivity.getResources().getColor(R.color.colorBlack));
//        cameraImg.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.icon_camera_unselect));
//        btmSearchBarcodeLayout.setBackgroundColor(mActivity.getResources().getColor(R.color.colorPrimary));
//        barcodeTitle.setTextColor(mActivity.getResources().getColor(R.color.colorWhite));
//        barcodeImg.setImageDrawable(mActivity.getDrawable(R.drawable.icon_scanner_select));
//        btmSearchVoiceLayout.setBackgroundColor(mActivity.getResources().getColor(R.color.colorWhite));
//        voiceSearchTitle.setTextColor(mActivity.getResources().getColor(R.color.colorBlack));
//        voiceImg.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.icon_voice_unselect));
    }

    @OnClick(R.id.search_voice_layout)
    void voiceSearchLayoutClick() {
        isTextInput = false;
        requestAudioPermissions();
    }

    @OnClick(R.id.btm_search_voice_layout)
    void onBtmVoiceSearchClick() {
        isTextInput = false;
        requestAudioPermissions();

//        btmSearchCameraLayout.setBackgroundColor(mActivity.getResources().getColor(R.color.colorWhite));
//        cameraTitle.setTextColor(mActivity.getResources().getColor(R.color.colorBlack));
//        cameraImg.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.icon_camera_unselect));
//        btmSearchBarcodeLayout.setBackgroundColor(mActivity.getResources().getColor(R.color.colorWhite));
//        barcodeTitle.setTextColor(mActivity.getResources().getColor(R.color.colorBlack));
//        barcodeImg.setImageDrawable(mActivity.getDrawable(R.drawable.icon_sacnner_unselect));
//        btmSearchVoiceLayout.setBackgroundColor(mActivity.getResources().getColor(R.color.colorPrimary));
//        voiceSearchTitle.setTextColor(mActivity.getResources().getColor(R.color.colorWhite));
//        voiceImg.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.icon_voice_select));
    }


//    @OnClick(R.id.search_product_btn)
//    void onSearchProductClick() {
//        Intent i = new Intent(mActivity, SearchProductActivity.class);
//        if (isTextInput) {
//            searchedProductName = searchProductEditText.getText().toString();
//        }
//        i.putExtra("SEARCHED_PRODUCT", searchedProductName);
//        startActivity(i);
//        mActivity.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
//    }

    private void cameraPermissionSetting() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                Log.d("Permissions_Status", "Permissions not granted");
               /* if (ContextCompat.checkSelfPermission(CameraFunctionality.this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED)*/
                // ask for permission
                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.CAMERA}, 1);
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
//        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
//        builder.setItems(new CharSequence[]
//                        {"Camera", "Select from Gallery"},
//                (dialog, which) -> {
//                    switch (which) {
//                        case 0:
//
//                            break;
//                        case 1:
//                            gal_intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                            startActivityForResult(Intent.createChooser(gal_intent, "Select Image from Gallery"), GAL_REQUEST);
//                    }
//                });
//        builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
//            imageStatus = false;
//            dialogInterface.dismiss();
//        });
//        builder.create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAM_REQUEST && resultCode == RESULT_OK) {
            customSearchLayout.setVisibility(View.GONE);
            searchLayout.setVisibility(View.VISIBLE);

            showDialog(mActivity, mActivity.getResources().getString(R.string.label_fetching_product));
            searchOptionsCenterLayout.setVisibility(View.GONE);
            itemsScrollView.setVisibility(View.GONE);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideDialog();
                    imageStatus = true;
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = bundle.getParcelable("data");
                    itemsScrollView.setVisibility(View.VISIBLE);
                    previewLayout.setVisibility(View.VISIBLE);
                    capturedPreviewImage.setVisibility(View.VISIBLE);
                    capturedPreviewImage.setImageDrawable(mActivity.getDrawable(R.drawable.icon_camera_unselect));
                    scannedProduct.setVisibility(View.GONE);
//                    capturedPreviewImage.setImageBitmap(bitmap);
                    bp = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bp.compress(Bitmap.CompressFormat.JPEG, 80, bytes);

                    setSearchedItemsList();

                    String inputStr = "Dolo";
                    searchOptionsBottomLayout.setVisibility(View.VISIBLE);
                    scannedBarCode.setText(inputStr);

                    resultLayout.setVisibility(View.VISIBLE);
                    resultsHeader.setText(convertSpannableStringSizes(mActivity, "Found ", String.valueOf(medicineList.size()),
                            " results for ", inputStr, 1.0f, 0.9f, R.font.roboto_bold));

                    btmSearchCameraLayout.setBackground(mActivity.getResources().getDrawable(R.drawable.take_order_selected_item_bg));
                    btmSearchBarcodeLayout.setBackground(mActivity.getResources().getDrawable(R.drawable.take_order_item_bg));
                    btmSearchVoiceLayout.setBackground(mActivity.getResources().getDrawable(R.drawable.take_order_item_bg));
                }
            }, 1000);

        } else if (requestCode == GAL_REQUEST) {
            if (data != null) {
                imageStatus = true;
                Uri choosenImage = data.getData();
                if (choosenImage != null) {
                    capturedPreviewImage.setVisibility(View.VISIBLE);
                    bp = decodeImageUri(mActivity, choosenImage, 80);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bp.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    capturedPreviewImage.setImageBitmap(bp);
                }
            } else if (requestCode == 3) {
                if (data == null) {
                    imageStatus = true;
                }
            }
        } else if (requestCode == REQ_CODE_SPEECH_INPUT) {
            customSearchLayout.setVisibility(View.GONE);
            searchLayout.setVisibility(View.VISIBLE);
            if (data != null) {
                showDialog(mActivity, mActivity.getResources().getString(R.string.label_fetching_product));
                searchOptionsCenterLayout.setVisibility(View.GONE);
                itemsScrollView.setVisibility(View.GONE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideDialog();
                        ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        itemsScrollView.setVisibility(View.VISIBLE);
                        searchOptionsBottomLayout.setVisibility(View.VISIBLE);
                        previewLayout.setVisibility(View.VISIBLE);
                        capturedPreviewImage.setVisibility(View.VISIBLE);
                        capturedPreviewImage.setImageDrawable(mActivity.getDrawable(R.drawable.icon_voice_unselect));
                        scannedProduct.setVisibility(View.GONE);
                        resultLayout.setVisibility(View.VISIBLE);

                        setSearchedItemsList();

                        searchedProductName = result.get(0);
                        scannedBarCode.setText(result.get(0));
                        resultsHeader.setText(convertSpannableStringSizes(mActivity, "Found ", String.valueOf(medicineList.size()),
                                " results for ", result.get(0), 1.0f, 0.9f, R.font.roboto_bold));

                        btmSearchCameraLayout.setBackground(mActivity.getResources().getDrawable(R.drawable.take_order_item_bg));
                        btmSearchBarcodeLayout.setBackground(mActivity.getResources().getDrawable(R.drawable.take_order_item_bg));
                        btmSearchVoiceLayout.setBackground(mActivity.getResources().getDrawable(R.drawable.take_order_selected_item_bg));
                    }
                }, 1000);
            }
        } else {
            customSearchLayout.setVisibility(View.GONE);
            searchLayout.setVisibility(View.VISIBLE);
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() == null) {
//                Toast.makeText(this, "Scan Cancelled", Toast.LENGTH_LONG).show();
                } else {
                    showDialog(mActivity, mActivity.getResources().getString(R.string.label_fetching_product));
                    searchOptionsCenterLayout.setVisibility(View.GONE);
                    itemsScrollView.setVisibility(View.GONE);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            hideDialog();
                            itemsScrollView.setVisibility(View.VISIBLE);
                            searchOptionsBottomLayout.setVisibility(View.VISIBLE);
                            previewLayout.setVisibility(View.VISIBLE);
                            capturedPreviewImage.setVisibility(View.VISIBLE);
                            capturedPreviewImage.setImageDrawable(mActivity.getDrawable(R.drawable.icon_sacnner_unselect));
                            scannedProduct.setVisibility(View.GONE);
                            resultLayout.setVisibility(View.VISIBLE);

                            setSearchedItemsList();

//                            capturedPreviewImage.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.icon_scanned_bar));
                            searchedProductName = result.getContents();
                            scannedBarCode.setText("Dolo");
//                            scannedBarCode.setText("Scanned Bar Code: " + result.getContents());
//                            scannedProduct.setText("Product Name: Dolo");
                            resultsHeader.setText(convertSpannableStringSizes(mActivity, "Found ", String.valueOf(medicineList.size()),
                                    " results for ", "Dolo", 1.0f, 0.9f, R.font.roboto_bold));

                            btmSearchCameraLayout.setBackground(mActivity.getResources().getDrawable(R.drawable.take_order_item_bg));
                            btmSearchBarcodeLayout.setBackground(mActivity.getResources().getDrawable(R.drawable.take_order_selected_item_bg));
                            btmSearchVoiceLayout.setBackground(mActivity.getResources().getDrawable(R.drawable.take_order_item_bg));
                        }
                    }, 1000);
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    //Handling callback
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_RECORD_AUDIO: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                    promptSpeechInput();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    private void requestAudioPermissions() {
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            //When permission is not granted by user, show them message why this permission is needed.
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.RECORD_AUDIO)) {
                //Give user option to still opt-in the permissions
                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_RECORD_AUDIO);
            } else {
                // Show user dialog to grant permission to record audio
                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_RECORD_AUDIO);
            }
        }
        //If permission is granted, then go ahead recording audio
        else if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            //Go ahead with recording audio now
            promptSpeechInput();
        }
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(mActivity, getString(R.string.speech_not_supported), Toast.LENGTH_SHORT).show();
        }
    }

    private void setSearchedItemsList() {
        getMedicineList();
        searchedItemsAdapter = new SearchedItemAdapter(mActivity, medicineList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
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
//            Toast.makeText(mActivity, "Item added to Cart", Toast.LENGTH_SHORT).show();
//            addToCartText.setBackground(mActivity.getDrawable(R.drawable.add_cart_bg));
        } else {
            cartItemsImg.setColorFilter(ContextCompat.getColor(mActivity, R.color.colorBlack));
            NavigationActivity.updateCartCount("0");
//            addToCartText.setBackground(mActivity.getDrawable(R.drawable.add_cart_unselect_bg));
        }
    }

    @Override
    public void onDecrementClick(int position) {
        itemQty = Integer.parseInt(medicineList.get(position).getQty());
        if (itemQty > 1) {
            itemQty = itemQty - 1;
            medicineList.get(position).setQty(String.valueOf(itemQty));
            searchedItemsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onIncrementClick(int position) {
        itemQty = Integer.parseInt(medicineList.get(position).getQty());
        itemQty = itemQty + 1;
        medicineList.get(position).setQty(String.valueOf(itemQty));
        searchedItemsAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.add_to_cart_layout)
    void onAddToCartClick() {
        selectedCnt = 0;
        for (OrderItemModel item : medicineList) {
            if (item.isSelected()) {
                selectedCnt++;
            }
        }
        if (selectedCnt == 0) {
            NavigationActivity.updateCartCount(String.valueOf(selectedCnt));
            Toast.makeText(mActivity, "Add any item to cart", Toast.LENGTH_SHORT).show();
        } else {
            NavigationActivity.updateCartCount(String.valueOf(selectedCnt));
            cartItemsImg.setColorFilter(ContextCompat.getColor(mActivity, R.color.cart_item_color));
            Toast.makeText(mActivity, "Item added to cart", Toast.LENGTH_SHORT).show();
        }
    }
}