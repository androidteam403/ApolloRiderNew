package com.apollo.epos.service;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;

import com.apollo.epos.R;
import com.apollo.epos.activity.MapViewActivity;
import com.apollo.epos.activity.NavigationActivity;
import com.apollo.epos.activity.NewOrderActivity;
import com.apollo.epos.activity.OrderDeliveryActivity;
import com.apollo.epos.activity.TrackMapActivity;
import com.orhanobut.hawk.Hawk;

import static com.apollo.epos.utils.ActivityUtils.getScreenSize;
import static com.apollo.epos.utils.ActivityUtils.getStatusBarHeight;
import static com.apollo.epos.utils.ActivityUtils.isAppOnForeground;
import static com.apollo.epos.utils.AppConstants.LAST_ACTIVITY;

public class FloatingTouchService extends Service {
    private boolean isMoving;
    private boolean isLogClick = false;
    private float rawX;
    private float rawY;

    private int mScreenWidth;
    private int mScreenHeight;
    private int mStatusBarHeight;

    private int lastAssistiveTouchViewX;
    private int lastAssistiveTouchViewY;

    private View mAssistiveTouchView;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mParams;
    private AlertDialog.Builder mBulider;
    private Dialog mAlertDialog;
    private Handler mHandler;
    public boolean isAssitiveClicked = false;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
        calculateForMyPhone();
        createAssistiveTouchView();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    int delay = 1000; //milliseconds

    private void init() {
        mHandler = new MyHandler();
        mBulider = new AlertDialog.Builder(FloatingTouchService.this, android.R.style.Theme_Black_NoTitleBar);
        mAlertDialog = new Dialog(FloatingTouchService.this, android.R.style.Theme_Black_NoTitleBar);//mBulider.create();
        //   mAlertDialog = new AlertDialog.Builder(this).create();//FloatingTouchService.this, R.style.DialogTheme);//mBulider.create();
        mParams = new WindowManager.LayoutParams();
        mWindowManager = (WindowManager) this.getSystemService(WINDOW_SERVICE);
        LayoutInflater mInflater = LayoutInflater.from(this);
        mAssistiveTouchView = mInflater.inflate(R.layout.assistive_touch_layout, null);
//        mAssistiveTouchView.setVisibility(View.GONE);
        updateAssistiveInvisible();
        Log.e("Assistive", "Creating Service ============ ");
        mHandler.postDelayed(new Runnable() {
            public void run() {
                if (isAppOnForeground(FloatingTouchService.this, getApplicationContext().getPackageName())) {
                    updateAssistiveInvisible();
//                    mAssistiveTouchView.setVisibility(View.GONE);
                    Log.e("AssistiveTouch", "Activity On Foreground = : = : = : = : ");
                } else {
//                    mAssistiveTouchView.setVisibility(View.VISIBLE);
                    if (!isAssitiveClicked) {
                        Log.e("AssistiveTouch", "Activity On Background, Visible = : = : = : = : ");
                        updateAssistiveVisible();
                    }
                }
                mHandler.postDelayed(this, delay);
            }
        }, delay);

        new MyHandler().postDelayed(new Runnable() {
            public void run() {
                isAssitiveClicked = false;
            }
        }, 4000);
    }

    private void calculateForMyPhone() {
        DisplayMetrics displayMetrics = getScreenSize(this);
        mScreenWidth = displayMetrics.widthPixels;
        mScreenHeight = displayMetrics.heightPixels;
        mStatusBarHeight = getStatusBarHeight(this);
    }

    public void createAssistiveTouchView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.x = mScreenWidth;
        mParams.y = mScreenHeight;
        mParams.gravity = Gravity.TOP | Gravity.END;
        mParams.format = PixelFormat.RGBA_8888;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mWindowManager.addView(mAssistiveTouchView, mParams);

        mAssistiveTouchView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isLogClick = true;
                return false;
            }
        });
        mAssistiveTouchView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                rawX = event.getRawX();
                rawY = event.getRawY();
                if (isLogClick) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            isMoving = false;
                            isLogClick = false;
                            break;
                        case MotionEvent.ACTION_UP:
                            setAssitiveTouchViewAlign();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            isMoving = true;
                            mParams.x = (int) (mScreenWidth - rawX);
                            mParams.y = (int) (rawY - mAssistiveTouchView.getMeasuredHeight() / 2 - mStatusBarHeight);
                            mWindowManager.updateViewLayout(mAssistiveTouchView, mParams);
                    }
                }
                return isMoving;
            }
        });
        mAssistiveTouchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAssitiveClicked = true;
//                updateAssistiveInvisible();
                lastAssistiveTouchViewX = mParams.x;
                lastAssistiveTouchViewY = mParams.y;

                String resumeActivity = Hawk.get(LAST_ACTIVITY, "");
                Log.e("AssistiveTouch", "Activity fetched from shared pref : == : == : == : == :");
                if (resumeActivity.equalsIgnoreCase(NavigationActivity.class.getSimpleName())) {
                    Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
//                    startActivity(new Intent(getApplicationContext(), NavigationActivity.class));
                } else if (resumeActivity.equalsIgnoreCase(NewOrderActivity.class.getSimpleName())) {
                    Intent intent = new Intent(getApplicationContext(), NewOrderActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                } else if (resumeActivity.equalsIgnoreCase(MapViewActivity.class.getSimpleName())) {
                    Intent intent = new Intent(getApplicationContext(), MapViewActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                } else if (resumeActivity.equalsIgnoreCase(OrderDeliveryActivity.class.getSimpleName())) {
                    Intent intent = new Intent(getApplicationContext(), OrderDeliveryActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                } else if (resumeActivity.equalsIgnoreCase(TrackMapActivity.class.getSimpleName())) {
                    Intent intent = new Intent(getApplicationContext(), TrackMapActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                }
                stopSelf();
            }
        });
    }

    private ValueAnimator myAssitiveTouchAnimator(final int fromx, final int tox, int fromy, final int toy) {
        PropertyValuesHolder p1 = PropertyValuesHolder.ofInt("X", fromx, tox);
        PropertyValuesHolder p2 = PropertyValuesHolder.ofInt("Y", fromy, toy);
        ValueAnimator v1 = ValueAnimator.ofPropertyValuesHolder(p1, p2);
        v1.setDuration(100L);
        v1.setInterpolator(new DecelerateInterpolator());
        v1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer x = (Integer) animation.getAnimatedValue("X");
                Integer y = (Integer) animation.getAnimatedValue("Y");
                mParams.x = x;
                mParams.y = y;
                mWindowManager.updateViewLayout(mAssistiveTouchView, mParams);
            }
        });
        v1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
        return v1;
    }

    private void setAssitiveTouchViewAlign() {
        int mAssistiveTouchViewWidth = mAssistiveTouchView.getMeasuredWidth();
        int mAssistiveTouchViewHeight = mAssistiveTouchView.getMeasuredHeight();
        int top = mParams.y + mAssistiveTouchViewWidth;
        int left = mParams.x + mAssistiveTouchViewHeight;
        int right = mScreenWidth - mParams.x - mAssistiveTouchViewWidth;
        int bottom = mScreenHeight - mParams.y - mAssistiveTouchViewHeight;
        int lor = Math.min(left, right);
        int tob = Math.min(top, bottom);
        int min = Math.min(lor, tob);
        lastAssistiveTouchViewX = mParams.x;
        lastAssistiveTouchViewY = mParams.y;
        if (min == top) mParams.y = 0;
        if (min == left) mParams.x = 0;
        if (min == right) mParams.x = mScreenWidth - mAssistiveTouchViewWidth;
        if (min == bottom) mParams.y = mScreenHeight - mAssistiveTouchViewHeight;
        myAssitiveTouchAnimator(lastAssistiveTouchViewX, mParams.x, lastAssistiveTouchViewY, mParams.y).start();
    }

    @SuppressLint("HandlerLeak")
    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //  showScreenshot((String) msg.obj);
                    break;
                case 2:
                    mAlertDialog.dismiss();
                default:
                    mAlertDialog.dismiss();
                    break;
            }
            super.handleMessage(msg);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAssistiveTouchView != null)
            mWindowManager.removeView(mAssistiveTouchView);
    }

    private void updateAssistiveVisible() {
        mAssistiveTouchView.setAlpha(1);
    }

    private void updateAssistiveInvisible() {
        mAssistiveTouchView.setAlpha(0);
    }
}
