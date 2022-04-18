package com.apollo.epos.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/*
 * Created on : oct 29, 2021.
 * Author : NAVEEN.M
 */
public class CommonUtils {
    public static String ORDER_DETAILS_RESPONSE = "ORDER_DETAILS_RESPONSE";
    public static String CURRENT_SCREEN = "";
    public static int NOTIFICATIONS_COUNT = 0;
    public static boolean is_order_delivery_screen = false;
    public static boolean isIs_order_delivery_or_track_map_screen = false;
    public static final int ONLINE_PAYMENT_ACTIVITY = 151;
    public static String selectedTab = "";
    public static boolean isMyOrdersListApiCall = false;

    public static String getCurrentTimeDate() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    public static String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

    public static String getBeforeSevenDaysDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date().getTime() - 518400000L);//604800000L
    }
    public static String getBeforeThirtyOneDaysDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date().getTime() - 2592000000L);
    }
    public static String getfromDate() {
        return new SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(new Date());
    }

    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getDeviceName() {
        return android.os.Build.MODEL;
    }

    public static String getDateFormatForSummaryEditText(long c) {
        Calendar neededTime = Calendar.getInstance();
        neededTime.setTimeInMillis(c);
        return new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(neededTime.getTime());
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

    public static String getSqlDateFormat(Calendar c) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(c.getTime());
    }

    public static boolean isFromDateBeforeToDate(String fromDate, String toDate) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date dateFrom = formatter.parse(fromDate);
            Date dateTo = formatter.parse(toDate);
            if (dateFrom.compareTo(dateTo) < 0 || dateFrom.equals(dateTo)) {
                System.out.println("date2 is Greater than my date1");
                return true;
            } else {
                return false;
            }
        } catch (ParseException e1) {
            e1.printStackTrace();
            return false;
        }
    }

    public static String getCurrentDateTimeMSUnique() {
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("mmss");
        String datetime = ft.format(dNow);
        return datetime;
    }

    public static String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }
}
