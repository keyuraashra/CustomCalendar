package com.riontech.calendarlibrary.dao;

/**
 * Created by Dhaval Soneji on 28/3/16.
 */
public class CalendarDecoratorDao {
    private String date;
    private int count;

    public CalendarDecoratorDao(String date, int count) {
        this.date = date;
        this.count = count;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
