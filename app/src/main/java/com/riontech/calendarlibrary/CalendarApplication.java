package com.riontech.calendarlibrary;

import android.app.Application;
import android.content.Context;

import java.util.GregorianCalendar;

/**
 * Created by Dhaval Soneji on 13/5/16.
 */
public class CalendarApplication extends Application {
    private static Context sContext;
    private GregorianCalendar mMonth;
    private String mCurrentDate;
    private String mTodayDate;
    private int mIsSwipeViewPager = 2;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
    }

    public static Context getContext() {
        return sContext;
    }

    public static CalendarApplication getInstance() {
        return (CalendarApplication) sContext;
    }

    public GregorianCalendar getMonth() {
        return mMonth;
    }

    public void setMonth(GregorianCalendar month) {
        this.mMonth = month;
    }

    public String getCurrentDate() {
        return mCurrentDate;
    }

    public void setCurrentDate(String mCurrentDate) {
        this.mCurrentDate = mCurrentDate;
    }

    public String getTodayDate() {
        return mTodayDate;
    }

    public void setTodayDate(String mTodayDate) {
        this.mTodayDate = mTodayDate;
    }

    public int getIsSwipeViewPager() {
        return mIsSwipeViewPager;
    }

    public void setIsSwipeViewPager(int isSwipeViewPager) {
        this.mIsSwipeViewPager = isSwipeViewPager;
    }
}
