package com.apollo.epos.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/*
 * Created on : oct 29, 2021.
 * Author : NAVEEN.M
 */
public class CommonUtils {
    public static String ORDER_DETAILS_RESPONSE = "ORDER_DETAILS_RESPONSE";
    public static String CURRENT_SCREEN = null;
    public static int NOTIFICATIONS_COUNT = 0;

    public static String getCurrentTimeDate() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getDeviceName() {
        return android.os.Build.MODEL;
    }

    public static String getTimeFormatter(long neededTimeMilis) {
        Calendar nowTime = Calendar.getInstance();
        Calendar neededTime = Calendar.getInstance();
        neededTime.setTimeInMillis(neededTimeMilis);

        if ((neededTime.get(Calendar.YEAR) == nowTime.get(Calendar.YEAR))) {
            if ((neededTime.get(Calendar.MONTH) == nowTime.get(Calendar.MONTH))) {
                if (neededTime.get(Calendar.DATE) - nowTime.get(Calendar.DATE) == 1) {
                    //here return like "Tomorrow at 12:00"
                    return new SimpleDateFormat("MMM dd, yyyy hh:mm aa", Locale.getDefault()).format(neededTime.getTime());//new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(neededTime.getTime());
                } else if (nowTime.get(Calendar.DATE) == neededTime.get(Calendar.DATE)) {
                    //here return like "Today at 12:00"
                    String time = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(neededTime.getTime());
                    String ampm;
                    String times = null;
                    if (time.endsWith("a.m.") || time.endsWith("am")) {
                        ampm = "AM";
                        if (time.endsWith("am"))
                            times = time.substring(0, time.length() - 2) + ampm;
                        else
                            times = time.substring(0, time.length() - 4) + ampm;
                    } else {
                        ampm = "PM";
                        if (time.endsWith("pm"))
                            times = time.substring(0, time.length() - 2) + ampm;
                        else
                            times = time.substring(0, time.length() - 4) + ampm;
                    }
                    return "Today at " + new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(neededTime.getTime());//times;
                } else if (nowTime.get(Calendar.DATE) - neededTime.get(Calendar.DATE) == 1) {
                    //here return like "Yesterday at 12:00"
                    return new SimpleDateFormat("MMM dd, yyyy hh:mm aa", Locale.getDefault()).format(neededTime.getTime()); //new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(neededTime.getTime());
                } else {
                    //here return like "May 31, 12:00"
                    return new SimpleDateFormat("MMM dd, yyyy hh:mm aa", Locale.getDefault()).format(neededTime.getTime());// new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(neededTime.getTime());
                }
            } else {
                //here return like "May 31, 12:00"
                return new SimpleDateFormat("MMM dd, yyyy hh:mm aa", Locale.getDefault()).format(neededTime.getTime());//new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(neededTime.getTime());
            }
        } else {
            //here return like "May 31 2010, 12:00" - it's a different year we need to show it
            return new SimpleDateFormat("MMM dd, yyyy hh:mm aa", Locale.getDefault()).format(neededTime.getTime());//new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(neededTime.getTime());
        }
    }
}
