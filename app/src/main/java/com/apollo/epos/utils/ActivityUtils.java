package com.apollo.epos.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.apollo.epos.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import dmax.dialog.SpotsDialog;

public class ActivityUtils {
    static SpotsDialog spotsDialog;

    /**
     * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     */
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, int frameId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }


    public static void startActivity(Activity context, Class<?> targetActivity, Bundle bundle) {
        Intent intent = new Intent(context, targetActivity);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.slide_in_from_rigth, R.anim.slide_in_from_left);
    }

    public static void startActivityForResult(Activity context, Class<?> targetActivity, int requestCode, Bundle bundle) {
        Intent intent = new Intent(context, targetActivity);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivityForResult(intent, requestCode);
        context.overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_in_from_rigth);
    }

    public static void setFullscreen(Activity activity) {
        if (Build.VERSION.SDK_INT > 10) {
            int flags = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_FULLSCREEN;

            if (isImmersiveAvailable()) {
                flags |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }

            activity.getWindow().getDecorView().setSystemUiVisibility(flags);
        } else {
            activity.getWindow()
                    .setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    public static boolean isImmersiveAvailable() {
        return android.os.Build.VERSION.SDK_INT >= 19;
    }

    public static SpannableString convertSpannableStrings(String header, String description, float headerSize, float descSize, int headerColor, int descColor) {
        SpannableString stringHeader = new SpannableString(header);
        stringHeader.setSpan(new RelativeSizeSpan(headerSize), 0, stringHeader.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringHeader.setSpan(new ForegroundColorSpan(headerColor), 0, stringHeader.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableString StringDesc = new SpannableString(description);
        StringDesc.setSpan(new RelativeSizeSpan(descSize), 0, StringDesc.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        StringDesc.setSpan(new ForegroundColorSpan(descColor), 0, StringDesc.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return new SpannableString(TextUtils.concat(stringHeader, "\n", StringDesc));
    }

    public static SpannableString convertSpannableStringSizes(Context context, String defaultStr, String resultCnt, String resultsTxt, String inputSearchTxt,
                                                              float headerSize, float descSize, int font) {
        Typeface typeface = ResourcesCompat.getFont(context, font);
        SpannableString stringDefault = new SpannableString(defaultStr);
        stringDefault.setSpan(new RelativeSizeSpan(descSize), 0, stringDefault.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableString stringResultCnt = new SpannableString(resultCnt);
        stringResultCnt.setSpan(new RelativeSizeSpan(headerSize), 0, stringResultCnt.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringResultCnt.setSpan(new StyleSpan(typeface.getStyle()), 0, stringResultCnt.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableString stringResultsTxt = new SpannableString(resultsTxt);
        stringResultsTxt.setSpan(new RelativeSizeSpan(descSize), 0, stringResultsTxt.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableString stringSearchTxt = new SpannableString(inputSearchTxt);
        stringSearchTxt.setSpan(new RelativeSizeSpan(headerSize), 0, stringSearchTxt.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringSearchTxt.setSpan(new StyleSpan(typeface.getStyle()), 0, stringSearchTxt.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return new SpannableString(TextUtils.concat(stringDefault, stringResultCnt, stringResultsTxt, stringSearchTxt));
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context) {
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context) {
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static void showLayoutDownAnimation(int targetId, LinearLayout parentLayout, LinearLayout childLayout) {
        Transition transition = new Slide(Gravity.TOP);
        transition.setDuration(1000);
        transition.addTarget(targetId);
        TransitionManager.beginDelayedTransition(parentLayout, transition);
        childLayout.setVisibility(View.VISIBLE);
    }

    public static void showTextDownAnimation(int targetId, LinearLayout parentLayout, TextView childText) {
        Transition transition = new Slide(Gravity.TOP);
        transition.setDuration(1000);
        transition.addTarget(targetId);
        TransitionManager.beginDelayedTransition(parentLayout, transition);
        childText.setVisibility(View.VISIBLE);
    }

    public static void showBottomAnim(View target, AnimatorSet mAnimatorSet) {
        int distance = target.getTop() + target.getHeight();
        mAnimatorSet.playTogether(
                ObjectAnimator.ofFloat(target, "alpha", 0, 1),
                ObjectAnimator.ofFloat(target, "translationY", -distance, 0)
        );
    }

    public static void footerAnimation(Context context, LinearLayout linearLayout, TextView childText) {
        Animation hide = AnimationUtils.loadAnimation(context, R.anim.header_move_bottom);
        linearLayout.startAnimation(hide);
        childText.setVisibility(View.VISIBLE);
    }

    public static void headerAnimation(Context context, LinearLayout linearLayout) {
        Animation hide = AnimationUtils.loadAnimation(context, R.anim.header_move_top);
        linearLayout.startAnimation(hide);
    }

    public static Bitmap decodeImageUri(Activity mActivity, Uri selectedImage, int REQUIRED_SIZE) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(mActivity.getContentResolver().openInputStream(selectedImage), null, o);
            // The new size we want to scale to
            // final int REQUIRED_SIZE =  size;
            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }
            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(mActivity.getContentResolver().openInputStream(selectedImage), null, o2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void showSoftKeyboard(Context context, View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public static void showDialog(Context mContext, String strMessage) {
        try {
            if (spotsDialog != null) {
                if (spotsDialog.isShowing()) {
                    spotsDialog.dismiss();
                }
            }
            spotsDialog = new SpotsDialog(mContext, strMessage, R.style.Custom, false, dialog -> {

            });
            Objects.requireNonNull(spotsDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            spotsDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideDialog() {
        try {
            if (spotsDialog.isShowing())
                spotsDialog.dismiss();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String getCurrentTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        return dateFormat.format(new Date()).toString();
    }
}