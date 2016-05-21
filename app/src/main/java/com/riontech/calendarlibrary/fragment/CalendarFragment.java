package com.riontech.calendarlibrary.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.riontech.calendarlibrary.CalendarApplication;
import com.riontech.calendarlibrary.MainActivity;
import com.riontech.calendarlibrary.R;
import com.riontech.calendarlibrary.adapter.CalendarAdapter;
import com.riontech.calendarlibrary.dao.CalendarDecoratorDao;
import com.riontech.calendarlibrary.dao.CalendarResponse;
import com.riontech.calendarlibrary.dao.DateData;
import com.riontech.calendarlibrary.dao.MonthDate;
import com.riontech.calendarlibrary.dao.dataAboutDate;
import com.riontech.calendarlibrary.utils.CalendarUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Dhaval Soneji on 31/3/16.
 */
public class CalendarFragment extends Fragment {
    private static final String TAG = CalendarFragment.class.getSimpleName();

    private GridView mGridview;
    private LinearLayout mLlDayList;
    private RelativeLayout mRlHeader;
    private ProgressDialog mDialog;

    private GregorianCalendar month;
    private CalendarAdapter mCalendarAdapter;
    private boolean flagMaxMin = false;
    public static String currentDateSelected;
    private Calendar mCalendar;
    private DateFormat mDateFormat;
    private GregorianCalendar mPMonth;
    private int mMonthLength;
    private GregorianCalendar mPMonthMaxSet;

    //calendar List
    private ArrayList<CalendarDecoratorDao> mEventList = new ArrayList<>();

    private String mStartMonth;
    private String mEndMonth;

    private RelativeLayout mNext;
    private RelativeLayout mPrevious;

    private ViewGroup rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(
                R.layout.fragement_calendarview, container, false);
        init();
            /*
            * This called when calendar first time loads
            * */
        if (CalendarApplication.getInstance().getIsSwipeViewPager() == 2)
            refreshDays(false);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * initialize calendar
     */
    private void init() {

        mLlDayList = (LinearLayout) rootView.findViewById(R.id.llDayList);
        mRlHeader = (RelativeLayout) rootView.findViewById(R.id.header);

        month = CalendarApplication.getInstance().getMonth();

        mCalendarAdapter = new CalendarAdapter(getActivity(), mEventList, month);

        mCalendar = month;
        mCalendar.set(Calendar.DAY_OF_MONTH, 1);
        mCalendar.setFirstDayOfWeek(Calendar.SUNDAY);
        mDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        mGridview = (GridView) rootView.findViewById(R.id.gridview);
        mGridview.setAdapter(mCalendarAdapter);

        TextView title = (TextView) rootView.findViewById(R.id.title);
        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));

        mGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                String selectedGridDate = mEventList.get(position).getDate();
                Log.d(TAG, mEventList.get(position).getDate());
                ((CalendarAdapter) parent.getAdapter()).setSelected(v, selectedGridDate);

                fetchSelectedDateData();

            }
        });
    }

    public void fetchSelectedDateData() {
        CalendarResponse calendarResponse = new CalendarResponse();
        ArrayList<DateData> datas = new ArrayList();
        DateData dateData = new DateData();

        ArrayList<dataAboutDate> dataAboutDates = new ArrayList();
        dataAboutDate dataAboutDate = new dataAboutDate();
        dataAboutDate.setRemarks("Remarks");
        dataAboutDate.setSubject("SUbject");
        dataAboutDate.setSubmissionDate(CalendarApplication.getInstance().getCurrentDate());
        dataAboutDate.setTitle("Title");
        dataAboutDates.set(0, dataAboutDate);

        dateData.setData(dataAboutDates);
        datas.add(dateData);
        calendarResponse.setCurrentDateData(datas);

        ArrayList<DateData> dateData1 = datas;
        ((MainActivity) getActivity()).setDateSelectionData(dateData1);
    }

    /**
     * refresh current month
     */
    public void refreshCalendar(boolean flag) {
        TextView title = (TextView) rootView.findViewById(R.id.title);
        refreshDays(flag);
        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
    }

    /**
     * refresh current month days
     */
    public void refreshDays(boolean flag) {

        //clear List
        mEventList.clear();
        //create clone
        mPMonth = (GregorianCalendar) mCalendar.clone();

        CalendarAdapter.firstDay = mCalendar.get(GregorianCalendar.DAY_OF_WEEK);

        int mMaxWeekNumber = mCalendar.getActualMaximum(Calendar.WEEK_OF_MONTH);

        mMonthLength = mMaxWeekNumber * 7;
        int mMaxP = getmMaxP();
        int mCalMaxP = mMaxP - (CalendarAdapter.firstDay - 1);

        mPMonthMaxSet = (GregorianCalendar) mPMonth.clone();

        mPMonthMaxSet.set(GregorianCalendar.DAY_OF_MONTH, mCalMaxP + 1);
        String str = android.text.format.DateFormat.format("M-yyyy", month).toString();
        String tempArray[] = str.split("-");
        String paramMonth = tempArray[0];
        String paramYear = tempArray[1];

        setData(false, getCalendarData());

    }

    private CalendarResponse getCalendarData() {
        CalendarResponse calendarResponse = new CalendarResponse();
        calendarResponse.setStartmonth("02, 2016");
        calendarResponse.setEndmonth("01, 2017");
        ArrayList<MonthDate> monthDateArrayList = new ArrayList();

        Calendar mCalendar = Calendar.getInstance();
        int daysInMonth = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        ArrayList<String> allDays = new ArrayList();
        SimpleDateFormat mFormat = CalendarUtils.getCalendarDBFormat();
        for (int i = 0; i < daysInMonth; i++) {
            // Add day to list
            allDays.add(mFormat.format(mCalendar.getTime()));
            // Move next day
            mCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        int j = 0;
        for (int i = 0; i < allDays.size(); i++) {
            if (i % 5 == 0) {
                MonthDate date = new MonthDate();
                date.setCount("1");
                date.setDate(allDays.get(i));
                monthDateArrayList.add(j, date);
                j++;
            }
        }
        calendarResponse.setMonthdata(monthDateArrayList);
        return calendarResponse;
    }

    private void setData(boolean flag, CalendarResponse calendarResponse) {

        mLlDayList.setVisibility(View.VISIBLE);
        mRlHeader.setVisibility(View.VISIBLE);
        mGridview.setVisibility(View.VISIBLE);

        ArrayList<DateData> dateDataArrayList = new ArrayList();
        DateData dateData1 = new DateData();

        ArrayList<dataAboutDate> dataAboutDates = new ArrayList();
        dataAboutDate dataAboutDate = new dataAboutDate();
        dataAboutDate.setRemarks("Remarks");
        dataAboutDate.setSubject("SUbject");
        dataAboutDate.setSubmissionDate(CalendarApplication.getInstance().getCurrentDate());
        dataAboutDate.setTitle("Title");
        dataAboutDates.add(dataAboutDate);

        dateData1.setData(dataAboutDates);
        dateDataArrayList.add(dateData1);
        calendarResponse.setCurrentDateData(dateDataArrayList);

        if (calendarResponse.getMonthdata() != null) {

            ArrayList<MonthDate> monthdata = calendarResponse.getMonthdata();
            int m = 0;

            for (int n = 0; n < mMonthLength; n++) {
                String mItemValue = mDateFormat.format(mPMonthMaxSet.getTime());
                mPMonthMaxSet.add(GregorianCalendar.DATE, 1);

                if (m < monthdata.size()) {
                    if (mItemValue.equalsIgnoreCase(monthdata.get(m).getDate())) {
                        CalendarDecoratorDao eventDao = new CalendarDecoratorDao(
                                monthdata.get(m).getDate(),
                                Integer.parseInt(monthdata.get(m).getCount()));
                        mEventList.add(eventDao);
                        m++;
                    } else {
                        CalendarDecoratorDao eventDao = new CalendarDecoratorDao(mItemValue, 0);
                        mEventList.add(eventDao);
                    }
                } else {
                    CalendarDecoratorDao eventDao = new CalendarDecoratorDao(mItemValue, 0);
                    mEventList.add(eventDao);
                }
            }
            ((MainActivity) getActivity()).setDateSelectionData(dateDataArrayList);

            mCalendarAdapter.notifyDataSetChanged();

            if (!flagMaxMin) {

                mStartMonth = calendarResponse.getStartmonth();
                mEndMonth = calendarResponse.getEndmonth();

                flagMaxMin = true;
            }

        }

    }

    /**
     * setup next month & check for calendar range
     */
    public void setNextMonth() {
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMaximum(GregorianCalendar.MONTH)) {
            month.set((month.get(GregorianCalendar.YEAR) + 1),
                    month.getActualMinimum(GregorianCalendar.MONTH), 1);
            CalendarApplication.getInstance().setMonth(month);
        } else {
            month.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) + 1);
            CalendarApplication.getInstance().setMonth(month);
        }
    }

    /**
     * setup previous month & check for calendar range
     */
    public void setPreviousMonth() {
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            month.set((month.get(GregorianCalendar.YEAR) - 1),
                    month.getActualMaximum(GregorianCalendar.MONTH), 1);
            CalendarApplication.getInstance().setMonth(month);
        } else {
            month.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) - 1);
            CalendarApplication.getInstance().setMonth(month);
        }
    }

    /**
     * @return
     */
    private int getmMaxP() {
        int maxP;
        if (mCalendar.get(GregorianCalendar.MONTH) == mCalendar
                .getActualMinimum(GregorianCalendar.MONTH)) {
            mPMonth.set((mCalendar.get(GregorianCalendar.YEAR) - 1),
                    mCalendar.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            mPMonth.set(GregorianCalendar.MONTH,
                    mCalendar.get(GregorianCalendar.MONTH) - 1);
        }
        maxP = mPMonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

        return maxP;
    }

}
