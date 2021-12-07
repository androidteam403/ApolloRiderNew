package com.apollo.epos.service;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.apollo.epos.activity.NewOrderActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.e("newToken", s);
        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fb", s).apply();
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification() != null) {
            try {
                if (remoteMessage.getData() != null && remoteMessage.getData().get("uid") != null) {
//                    String orderNumber = remoteMessage.getData().get("uid");
//                    Intent intent = new Intent(this, NewOrderActivity.class);
//                    intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES |
//                            Intent.FLAG_ACTIVITY_NEW_TASK |
//                            Intent.FLAG_ACTIVITY_SINGLE_TOP |
//                            Intent.FLAG_ACTIVITY_NO_HISTORY);
//                    intent.putExtra("order_number", orderNumber);
//                    startActivity(intent);
                }
            } catch (Exception e) {
                System.out.println("MyFirebaseMessagingSerice:::::::::" + e.getMessage());
            }
        }
    }
}
