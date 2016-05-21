package com.riontech.calendarlibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Dhaval Soneji on 26/1/16.
 */
public class CalendarUtils {
    private static final String TAG = CalendarUtils.class.getSimpleName();
    private static final String DB_FORMAT = "yyyy-MM-dd hh:mm:ss";
    private static final String DB_FORMAT_OTHER = "MMM dd yyyy, hh:mm a";
    private static final String DETAIL_DATE_FORMAT = "MMMM dd, yyyy hh 'Hour' mm 'Minutes'";
    private static final String MEETING_DATE_FORMAT = "MMMM dd, yyyy";
    private static final String MEETING_TIME_FORMAT = "hh:mm a";
    private static final String CALENDAR_DB_FORMAT = "yyyy-MM-dd";
    private static final String CALENDAR_DATE_FORMAT = "MMM dd yyyy";
    private static final String MESSANGER_DATE_FORMAT = "dd/MM/yyyy";
    private static final String MESSANGER_TIME_FORMAT = "hh:mm a";

    public static SimpleDateFormat getMessangerTimeFormat() {
        return new SimpleDateFormat(MESSANGER_TIME_FORMAT);
    }

    public static SimpleDateFormat getMessangerDateFormat() {
        return new SimpleDateFormat(MESSANGER_DATE_FORMAT);
    }

    /**
     * Check Internet Available or Not
     */
    public static boolean checkInternetConnection(final Context context) {
        final ConnectivityManager conMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param email
     * @return
     */
    //Email Validation
    public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * @param pass
     * @return
     */
    // validating password with retype password
    public static boolean isValidPassword(String pass) {
        if (pass != null && pass.length() > 8) {
            return true;
        }
        return false;
    }

    /**
     * get IMEI number
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        TelephonyManager mngr = (TelephonyManager)
                context.getSystemService(context.TELEPHONY_SERVICE);
        String imei = mngr.getDeviceId();
        return imei;

    }

    public static SimpleDateFormat getCalendarDBFormat() {
        return new SimpleDateFormat(CALENDAR_DB_FORMAT);
    }

    public static SimpleDateFormat getDBFormat() {
        return new SimpleDateFormat(DB_FORMAT);
    }

    public static SimpleDateFormat getOtherDBFormat() {
        return new SimpleDateFormat(DB_FORMAT_OTHER);
    }

    public static SimpleDateFormat getDetailDateFormat() {
        return new SimpleDateFormat(DETAIL_DATE_FORMAT);
    }

    public static SimpleDateFormat getCalendarDateFormat() {
        return new SimpleDateFormat(CALENDAR_DATE_FORMAT);
    }

    public static SimpleDateFormat getMeetingDateFormat() {
        return new SimpleDateFormat(MEETING_DATE_FORMAT);
    }

    public static SimpleDateFormat getMeetingTimeFormat() {
        return new SimpleDateFormat(MEETING_TIME_FORMAT);
    }

    public static String getDisplayDateTimeStringTwoDates(String dbDate) {
        Log.d(TAG, "MMM dd yyyy");
        String displayDate = "";
        SimpleDateFormat dbDateFormat = new SimpleDateFormat("MMM dd yyyy");
        Date date = null;
        try {
            date = dbDateFormat.parse(dbDate);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        displayDate = simpleDateFormat.format(date);
        return displayDate;
    }

    public static String getDisplayDateTimeStringTwoTimes(String dbDate) {
        //Log.d(TAG, "EEE dd yyyy, hh:mm aa");
        String displayDate = "";
        SimpleDateFormat dbDateFormat = new SimpleDateFormat("hh:mm a");
        Date date = null;
        try {
            date = dbDateFormat.parse(dbDate);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh 'Hour' mm 'Minutes'");
        displayDate = simpleDateFormat.format(date);
        return displayDate;
    }

    public static void hideKeyboard(Activity activity, EditText editText) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static boolean appInstalledOrNot(String uri, Context context) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }
}
