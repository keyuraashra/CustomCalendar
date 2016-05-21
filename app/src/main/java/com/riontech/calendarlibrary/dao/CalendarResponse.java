package com.riontech.calendarlibrary.dao;

import java.util.ArrayList;

/**
 * Created by Dhaval Soneji on 13/5/16.
 */
public class CalendarResponse{
    private String startmon;
    private String endmon;
    private ArrayList<MonthDate> monthdata;
    private ArrayList<DateData> currentDateData;

    public ArrayList<DateData> getCurrentDateData() {
        return currentDateData;
    }

    public void setCurrentDateData(ArrayList<DateData> currentDateData) {
        this.currentDateData = currentDateData;
    }

    public ArrayList<MonthDate> getMonthdata() {
        return monthdata;
    }

    public void setMonthdata(ArrayList<MonthDate> monthdata) {
        this.monthdata = monthdata;
    }

    public String getStartmonth() {
        return startmon;
    }

    public void setStartmonth(String startmonth) {
        this.startmon = startmonth;
    }

    public String getEndmonth() {
        return endmon;
    }

    public void setEndmonth(String endmonth) {
        this.endmon = endmonth;
    }

}
