package com.apollo.epos.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apollo.epos.R;
import com.apollo.epos.listeners.DialogMangerCallback;


/**
 * Created by prathima.p on 21-11-2016.
 */
public class DialogManager {

    public static Dialog mDialog;
    private static Button mDialogBtn, mDialogCancelBtn;
    private static TextView mDialogAlertTxt;
    private static TextView mDialogTitleTxt;

    public static void showToast(Context context, String message) {
        Toast mToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        TextView mToastTxt = mToast.getView().findViewById(
                android.R.id.message);
        mToast.show();
    }



    public static void showSingleBtnPopup(final Context mContext,
                                          final DialogMangerCallback mDialoginterface, String mTitle,
                                          String mMessage, String mBtnTxt) {

        mDialog = getDialog(mContext, R.layout.popup_single_btn);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);

        ViewGroup root = mDialog.findViewById(R.id.parent_view_for_font);

        mDialogAlertTxt = mDialog.findViewById(R.id.alert_text);
        mDialogTitleTxt = mDialog.findViewById(R.id.header_txt);

        mDialogBtn = mDialog.findViewById(R.id.process_btn);
        mDialogCancelBtn = mDialog.findViewById(R.id.cancel_btn);
        mDialogCancelBtn.setVisibility(View.GONE);
        mDialogTitleTxt.setText(mTitle);
        mDialogAlertTxt.setText(getMessage(mMessage));
        mDialogBtn.setText(mBtnTxt);

        mDialogBtn.setOnClickListener(v -> {
            mDialog.dismiss();
            if(mDialoginterface != null)
                mDialoginterface.onOkClick(v);
        });

        mDialog.show();
    }

    public static Dialog getDialog(Context mContext, int mLayout) {
        Dialog mDialog = new Dialog(mContext);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(mLayout);
        mDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        mDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        mDialog.getWindow().setGravity(Gravity.CENTER);

        mDialog.setCancelable(true);

        mDialog.setCanceledOnTouchOutside(false);

        return mDialog;
    }

    private static String getMessage(String mMessage) {
        if(!isValidStr(mMessage))
            return "";
        return (mMessage.trim().charAt(mMessage.trim().length() - 1) == '.' || mMessage.charAt(mMessage.length() - 1) == '?') ? mMessage : mMessage + ".";
    }

    public static boolean isValidStr(String string) {
        return string != null && !string.trim().isEmpty();
    }

}
