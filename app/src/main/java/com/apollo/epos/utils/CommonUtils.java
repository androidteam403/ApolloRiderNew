package com.apollo.epos.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;
import android.text.format.DateFormat;

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
                    String time = String.valueOf(DateFormat.format("HH:mm aa", neededTime));
                    String ampm = null;
                    if (time.substring(time.length() - 4).equals("a.m."))
                        ampm = "AM";
                    else
                        ampm = "PM";
                    return "Tomorrow at " + time.substring(0, time.length() - 4) + ampm;

//                    return "Tomorrow at " + DateFormat.format("HH:mm aa", neededTime);

                } else if (nowTime.get(Calendar.DATE) == neededTime.get(Calendar.DATE)) {
                    //here return like "Today at 12:00"
                    String time = String.valueOf(DateFormat.format("HH:mm aa", neededTime));
                    String ampm = null;
                    if (time.substring(time.length() - 4).equals("a.m."))
                        ampm = "AM";
                    else
                        ampm = "PM";
                    return "Today at  " + time.substring(0, time.length() - 4) + ampm;
//                    return "Today at " + DateFormat.format("HH:mm aa", neededTime);

                } else if (nowTime.get(Calendar.DATE) - neededTime.get(Calendar.DATE) == 1) {
                    //here return like "Yesterday at 12:00"
                    String time = String.valueOf(DateFormat.format("HH:mm aa", neededTime));
                    String ampm = null;
                    if (time.substring(time.length() - 4).equals("a.m."))
                        ampm = "AM";
                    else
                        ampm = "PM";
                    return "Yesterday at  " + time.substring(0, time.length() - 4) + ampm;
//                    return "Yesterday at " + DateFormat.format("HH:mm aa", neededTime);

                } else {
                    //here return like "May 31, 12:00"
                    String time = String.valueOf( DateFormat.format("MMMM d, HH:mm aa", neededTime));
                    String ampm = null;
                    if (time.substring(time.length() - 4).equals("a.m."))
                        ampm = "AM";
                    else
                        ampm = "PM";
                    return time.substring(0, time.length() - 4) + ampm;


//                    return DateFormat.format("MMMM d, HH:mm aa", neededTime).toString();
                }

            } else {
                //here return like "May 31, 12:00"
                String time = String.valueOf(DateFormat.format("MMMM d, HH:mm aa", neededTime));
                String ampm = null;
                if (time.substring(time.length() - 4).equals("a.m."))
                    ampm = "AM";
                else
                    ampm = "PM";
                return time.substring(0, time.length() - 4) + ampm;
//                return DateFormat.format("MMMM d, HH:mm aa", neededTime).toString();
            }

        } else {
            //here return like "May 31 2010, 12:00" - it's a different year we need to show it
            String time = String.valueOf(DateFormat.format("MMMM dd yyyy, HH:mm aa", neededTime));
            String ampm = null;
            if (time.substring(time.length() - 4).equals("a.m."))
                ampm = "AM";
            else
                ampm = "PM";
            return time.substring(0, time.length() - 4) + ampm;
//            return DateFormat.format("MMMM dd yyyy, HH:mm aa", neededTime).toString();
        }
    }

}
