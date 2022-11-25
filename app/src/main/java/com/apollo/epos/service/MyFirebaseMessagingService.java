package com.apollo.epos.service;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.apollo.epos.R;
import com.apollo.epos.activity.navigation.NavigationActivity;
import com.apollo.epos.activity.orderdelivery.OrderDeliveryActivity;
import com.apollo.epos.activity.reports.ReportsActivity;
import com.apollo.epos.activity.trackmap.TrackMapActivity;
import com.apollo.epos.db.SessionManager;
import com.apollo.epos.fragment.dashboard.DashboardFragment;
import com.apollo.epos.utils.CommonUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.List;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.e("newToken", s);
        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fb", s).apply();
    }

    public SessionManager getSessionManager() {
        return new SessionManager(getApplicationContext());
    }


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification() != null) {
            try {
                if (remoteMessage.getData() != null && remoteMessage.getData().get("uid") != null) {
                    CommonUtils.isMyOrdersListApiCall = true;
                    if (remoteMessage.getData().get("notification_type").equals("ORDER_CANCELLED")){
                        if (CommonUtils.CURRENT_SCREEN.equals("OrderDeliveryActivity")) {
                            String orderNumber = remoteMessage.getData().get("uid");
                            Intent intent = new Intent(this, OrderDeliveryActivity.class);
                            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES |
                                    Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
                                    Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.putExtra("order_cancelled", true);
                            intent.putExtra("order_uid", orderNumber);
                            startActivity(intent);
                        } else if (CommonUtils.CURRENT_SCREEN.equals("TrackMapActivity")) {
                            String orderNumber = remoteMessage.getData().get("uid");
                            Intent intent = new Intent(this, TrackMapActivity.class);
                            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES |
                                    Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
                                    Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.putExtra("order_cancelled", true);
                            intent.putExtra("order_uid", orderNumber);
                            startActivity(intent);
                        } else if (CommonUtils.CURRENT_SCREEN.equals("ReportsActivity")) {
                            String orderNumber = remoteMessage.getData().get("uid");
                            Intent intent = new Intent(this, ReportsActivity.class);
                            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES |
                                    Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
                                    Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.putExtra("order_cancelled", true);
                            intent.putExtra("order_uid", orderNumber);
                            startActivity(intent);
                        }
                    }else if (remoteMessage.getData().get("notification_type").equals("ORDER_SHIFTED")){
                        if (CommonUtils.CURRENT_SCREEN.equals("OrderDeliveryActivity")) {
                            String orderNumber = remoteMessage.getData().get("uid");
                            Intent intent = new Intent(this, OrderDeliveryActivity.class);
                            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES |
                                    Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
                                    Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.putExtra("order_shifted", true);
                            intent.putExtra("NOTIFICATION", remoteMessage.getNotification().getBody());
                            intent.putExtra("order_uid", orderNumber);
                            startActivity(intent);
                        } else if (CommonUtils.CURRENT_SCREEN.equals(NavigationActivity.class.getSimpleName())) {
                            String orderNumber = remoteMessage.getData().get("uid");
                            Intent intent = new Intent(this, NavigationActivity.class);
                            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES |
                                    Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
                                    Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.putExtra("order_shifted", true);
                            intent.putExtra("NOTIFICATION", remoteMessage.getNotification().getBody());
                            intent.putExtra("order_uid", orderNumber);
                            startActivity(intent);
                        } else if (CommonUtils.CURRENT_SCREEN.equals("TrackMapActivity")) {
                            String orderNumber = remoteMessage.getData().get("uid");
                            Intent intent = new Intent(this, TrackMapActivity.class);
                            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES |
                                    Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
                                    Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.putExtra("order_shifted", true);
                            intent.putExtra("NOTIFICATION", remoteMessage.getNotification().getBody());
                            intent.putExtra("order_uid", orderNumber);
                            startActivity(intent);
                        } else if (CommonUtils.CURRENT_SCREEN.equals("ReportsActivity")) {
                            String orderNumber = remoteMessage.getData().get("uid");
                            Intent intent = new Intent(this, ReportsActivity.class);
                            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES |
                                    Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
                                    Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.putExtra("order_shifted", true);
                            intent.putExtra("NOTIFICATION", remoteMessage.getNotification().getBody());
                            intent.putExtra("order_uid", orderNumber);
                            startActivity(intent);
                        }
                    }else if (remoteMessage.getData().get("notification_type").equals("ORDER_ASSIGNED")){
                        if (!getSessionManager().getNotificationStatus())
                            CommonUtils.NOTIFICATIONS_COUNT = 0;
                        CommonUtils.NOTIFICATIONS_COUNT = CommonUtils.NOTIFICATIONS_COUNT + 1;

                        if (getSessionManager().getAsignedOrderUid() != null) {
                            List<String> orderUidList = getSessionManager().getAsignedOrderUid();
                            orderUidList.add(remoteMessage.getData().get("uid"));
                            getSessionManager().setAsignedOrderUid(orderUidList);
                        } else {
                            List<String> orderUidList = new ArrayList<>();
                            orderUidList.add(remoteMessage.getData().get("uid"));
                            getSessionManager().setAsignedOrderUid(orderUidList);
                        }
                        if (CommonUtils.CURRENT_SCREEN.equals(NavigationActivity.class.getSimpleName())) {
                            String orderNumber = remoteMessage.getData().get("uid");
                            Intent intent = new Intent(this, NavigationActivity.class);
                            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES |
                                    Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
                                    Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.putExtra("ORDER_ASSIGNED", true);
                            intent.putExtra("NOTIFICATION", remoteMessage.getNotification().getBody());
                            intent.putExtra("order_uid", orderNumber);
                            startActivity(intent);
                        }
                        Handler handler = new Handler(Looper.getMainLooper());

                        handler.postDelayed(() -> {
                            NavigationActivity.notificationDotVisibility(true);
                            ReportsActivity.notificationDotVisibility(true);
                            OrderDeliveryActivity.notificationDotVisibility(true);
                            DashboardFragment.newOrderViewVisibility(true);
                            getSessionManager().setNotificationStatus(true);
                            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.notify_sound); // sound is inside res/raw/mysound
                            mp.start();
                        }, 1000);

                        handler.postDelayed(() -> {
                            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.notify_sound); // sound is inside res/raw/mysound
                            mp.start();
                        }, 2000);

                        handler.postDelayed(() -> {
                            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.notify_sound); // sound is inside res/raw/mysound
                            mp.start();
                        }, 3000);
                    }else if (remoteMessage.getData().get("notification_type").equals("COMPLAINT_RESOLVED")){
                        if (CommonUtils.CURRENT_SCREEN.equals("OrderDeliveryActivity")) {
                            String orderNumber = remoteMessage.getData().get("uid");
                            Intent intent = new Intent(this, OrderDeliveryActivity.class);
                            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES |
                                    Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
                                    Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.putExtra("COMPLAINT_RESOLVED", true);
                            intent.putExtra("NOTIFICATION", remoteMessage.getNotification().getBody());
                            intent.putExtra("order_uid", orderNumber);
                            startActivity(intent);
                        } else if (CommonUtils.CURRENT_SCREEN.equals(NavigationActivity.class.getSimpleName())) {
                            String orderNumber = remoteMessage.getData().get("uid");
                            Intent intent = new Intent(this, NavigationActivity.class);
                            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES |
                                    Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
                                    Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.putExtra("COMPLAINT_RESOLVED", true);
                            intent.putExtra("NOTIFICATION", remoteMessage.getNotification().getBody());
                            intent.putExtra("order_uid", orderNumber);
                            startActivity(intent);
                        } else if (CommonUtils.CURRENT_SCREEN.equals("TrackMapActivity")) {
                            String orderNumber = remoteMessage.getData().get("uid");
                            Intent intent = new Intent(this, TrackMapActivity.class);
                            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES |
                                    Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
                                    Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.putExtra("COMPLAINT_RESOLVED", true);
                            intent.putExtra("NOTIFICATION", remoteMessage.getNotification().getBody());
                            intent.putExtra("order_uid", orderNumber);
                            startActivity(intent);
                        } else if (CommonUtils.CURRENT_SCREEN.equals("ReportsActivity")) {
                            String orderNumber = remoteMessage.getData().get("uid");
                            Intent intent = new Intent(this, ReportsActivity.class);
                            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES |
                                    Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
                                    Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.putExtra("COMPLAINT_RESOLVED", true);
                            intent.putExtra("NOTIFICATION", remoteMessage.getNotification().getBody());
                            intent.putExtra("order_uid", orderNumber);
                            startActivity(intent);
                        }
                    }

















//                    if (remoteMessage.getData().get("order_status") != null) {
//                        if (CommonUtils.CURRENT_SCREEN.equals("OrderDeliveryActivity")) {
//                            String orderNumber = remoteMessage.getData().get("uid");
//                            Intent intent = new Intent(this, OrderDeliveryActivity.class);
//                            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES |
//                                    Intent.FLAG_ACTIVITY_NEW_TASK |
//                                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
//                                    Intent.FLAG_ACTIVITY_NO_HISTORY);
//                            intent.putExtra("order_cancelled", true);
//                            intent.putExtra("order_uid", orderNumber);
//                            startActivity(intent);
//                        } else if (CommonUtils.CURRENT_SCREEN.equals("TrackMapActivity")) {
//                            String orderNumber = remoteMessage.getData().get("uid");
//                            Intent intent = new Intent(this, TrackMapActivity.class);
//                            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES |
//                                    Intent.FLAG_ACTIVITY_NEW_TASK |
//                                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
//                                    Intent.FLAG_ACTIVITY_NO_HISTORY);
//                            intent.putExtra("order_cancelled", true);
//                            intent.putExtra("order_uid", orderNumber);
//                            startActivity(intent);
//                        } else if (CommonUtils.CURRENT_SCREEN.equals("ReportsActivity")) {
//                            String orderNumber = remoteMessage.getData().get("uid");
//                            Intent intent = new Intent(this, ReportsActivity.class);
//                            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES |
//                                    Intent.FLAG_ACTIVITY_NEW_TASK |
//                                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
//                                    Intent.FLAG_ACTIVITY_NO_HISTORY);
//                            intent.putExtra("order_cancelled", true);
//                            intent.putExtra("order_uid", orderNumber);
//                            startActivity(intent);
//                        }
//                    } else if (Objects.requireNonNull(remoteMessage.getNotification().getBody()).contains("assigned to another rider")) {
//                        if (CommonUtils.CURRENT_SCREEN.equals("OrderDeliveryActivity")) {
//                            String orderNumber = remoteMessage.getData().get("uid");
//                            Intent intent = new Intent(this, OrderDeliveryActivity.class);
//                            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES |
//                                    Intent.FLAG_ACTIVITY_NEW_TASK |
//                                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
//                                    Intent.FLAG_ACTIVITY_NO_HISTORY);
//                            intent.putExtra("order_shifted", true);
//                            intent.putExtra("NOTIFICATION", remoteMessage.getNotification().getBody());
//                            intent.putExtra("order_uid", orderNumber);
//                            startActivity(intent);
//                        } else if (CommonUtils.CURRENT_SCREEN.equals(NavigationActivity.class.getSimpleName())) {
//                            String orderNumber = remoteMessage.getData().get("uid");
//                            Intent intent = new Intent(this, NavigationActivity.class);
//                            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES |
//                                    Intent.FLAG_ACTIVITY_NEW_TASK |
//                                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
//                                    Intent.FLAG_ACTIVITY_NO_HISTORY);
//                            intent.putExtra("order_shifted", true);
//                            intent.putExtra("NOTIFICATION", remoteMessage.getNotification().getBody());
//                            intent.putExtra("order_uid", orderNumber);
//                            startActivity(intent);
//                        } else if (CommonUtils.CURRENT_SCREEN.equals("TrackMapActivity")) {
//                            String orderNumber = remoteMessage.getData().get("uid");
//                            Intent intent = new Intent(this, TrackMapActivity.class);
//                            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES |
//                                    Intent.FLAG_ACTIVITY_NEW_TASK |
//                                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
//                                    Intent.FLAG_ACTIVITY_NO_HISTORY);
//                            intent.putExtra("order_shifted", true);
//                            intent.putExtra("NOTIFICATION", remoteMessage.getNotification().getBody());
//                            intent.putExtra("order_uid", orderNumber);
//                            startActivity(intent);
//                        } else if (CommonUtils.CURRENT_SCREEN.equals("ReportsActivity")) {
//                            String orderNumber = remoteMessage.getData().get("uid");
//                            Intent intent = new Intent(this, ReportsActivity.class);
//                            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES |
//                                    Intent.FLAG_ACTIVITY_NEW_TASK |
//                                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
//                                    Intent.FLAG_ACTIVITY_NO_HISTORY);
//                            intent.putExtra("order_shifted", true);
//                            intent.putExtra("NOTIFICATION", remoteMessage.getNotification().getBody());
//                            intent.putExtra("order_uid", orderNumber);
//                            startActivity(intent);
//                        }
//
//
////                        SnackbarLayoutBinding snackbarLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.snackbar_layout, null, false);
////                        Snackbar.make(snackbarLayoutBinding.getRoot(), remoteMessage.getNotification().toString(), Snackbar.LENGTH_LONG).show();
//                    } else {
//                        if (!getSessionManager().getNotificationStatus())
//                            CommonUtils.NOTIFICATIONS_COUNT = 0;
//                        CommonUtils.NOTIFICATIONS_COUNT = CommonUtils.NOTIFICATIONS_COUNT + 1;
//
//                        if (getSessionManager().getAsignedOrderUid() != null) {
//                            List<String> orderUidList = getSessionManager().getAsignedOrderUid();
//                            orderUidList.add(remoteMessage.getData().get("uid"));
//                            getSessionManager().setAsignedOrderUid(orderUidList);
//                        } else {
//                            List<String> orderUidList = new ArrayList<>();
//                            orderUidList.add(remoteMessage.getData().get("uid"));
//                            getSessionManager().setAsignedOrderUid(orderUidList);
//                        }
//                        if (CommonUtils.CURRENT_SCREEN.equals(NavigationActivity.class.getSimpleName())) {
//                            String orderNumber = remoteMessage.getData().get("uid");
//                            Intent intent = new Intent(this, NavigationActivity.class);
//                            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES |
//                                    Intent.FLAG_ACTIVITY_NEW_TASK |
//                                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
//                                    Intent.FLAG_ACTIVITY_NO_HISTORY);
//                            intent.putExtra("ORDER_ASSIGNED", true);
//                            intent.putExtra("NOTIFICATION", remoteMessage.getNotification().getBody());
//                            intent.putExtra("order_uid", orderNumber);
//                            startActivity(intent);
//                        }
//                        Handler handler = new Handler(Looper.getMainLooper());
//
//                        handler.postDelayed(() -> {
//                            NavigationActivity.notificationDotVisibility(true);
//                            ReportsActivity.notificationDotVisibility(true);
//                            OrderDeliveryActivity.notificationDotVisibility(true);
//                            DashboardFragment.newOrderViewVisibility(true);
//                            getSessionManager().setNotificationStatus(true);
//                            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.notify_sound); // sound is inside res/raw/mysound
//                            mp.start();
//                        }, 1000);
//
//                        handler.postDelayed(() -> {
//                            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.notify_sound); // sound is inside res/raw/mysound
//                            mp.start();
//                        }, 2000);
//
//                        handler.postDelayed(() -> {
//                            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.notify_sound); // sound is inside res/raw/mysound
//                            mp.start();
//                        }, 3000);
//                    }
                }
            } catch (Exception e) {
                System.out.println("MyFirebaseMessagingSerice:::::::::" + e.getMessage());
            }
        }
    }
}
