package com.riontech.calendarlibrary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.riontech.calendarlibrary.adapter.CalendarDataAdapter;
import com.riontech.calendarlibrary.adapter.ViewPagerAdapter;
import com.riontech.calendarlibrary.dao.DateData;
import com.riontech.calendarlibrary.fragment.CalendarFragment;
import com.riontech.calendarlibrary.utils.CalendarUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Dhaval Soneji on 13/5/16.
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private String mStartMonth;
    private String mEndMonth;
    private ViewPager mViewPager;
    private ViewPagerAdapter mAdapter;
    private int mDiffMonth;
    private int mTotalCount;
    private int mCurrentPosition;
    private GregorianCalendar mMonth;
    private boolean mFlagMaxMin = false;
    private RecyclerView mRvCalendar;
    private LinearLayoutManager mLinearLayoutManager;
    private Calendar calendarNotification = null;
    private TextView mTxtEventMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
            first time setup calendar currentMonth
         */
        CalendarApplication.getInstance().setMonth((GregorianCalendar) GregorianCalendar.getInstance());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        CalendarApplication.getInstance().setCurrentDate(dateFormat.format(Calendar.getInstance().getTime()));
        CalendarApplication.getInstance().setTodayDate(dateFormat.format(Calendar.getInstance().getTime()));

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mTxtEventMessage = (TextView) findViewById(R.id.txtEventMessage);
        mRvCalendar = (RecyclerView) findViewById(R.id.rvCalendar);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRvCalendar.setLayoutManager(mLinearLayoutManager);
        setupCalendar();
    }

    private void setupCalendar() {
        mMonth = CalendarApplication.getInstance().getMonth();

        String endMonth = "01, 2017";
        String temp[] = endMonth.split(",");
        int a = Integer.parseInt(temp[0]);
        String b = temp[1];
        a = a + 1;

        String startMonth = "02, 2016";
        mStartMonth = startMonth;
        mEndMonth = String.valueOf(a) + ", " + b;

        mFlagMaxMin = true;

        setupViewPager();

    }

    private void setupViewPager() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM, yyyy");
        Calendar currentCalendar = Calendar.getInstance();
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();

        Date startDate = null;
        Date endDate = null;

        try {

            startDate = sdf.parse(mStartMonth);
            endDate = sdf.parse(mEndMonth);
            startCalendar.setTime(startDate);
            endCalendar.setTime(endDate);
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
        mDiffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
        mTotalCount = mDiffMonth;

        int diffCurrentYear = currentCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
        int diffCurrentMonth = diffCurrentYear * 12 + currentCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
        mCurrentPosition = diffCurrentMonth;

        mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this, mDiffMonth);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(mAdapter);


        mViewPager.setCurrentItem(diffCurrentMonth);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position <= mTotalCount && position >= 0) {
                    if (position > mCurrentPosition) {
                        CalendarApplication.getInstance().setIsSwipeViewPager(1);

                        ((CalendarFragment) mAdapter.getRegisteredFragment(position)).setNextMonth();
                        ((CalendarFragment) mAdapter.getRegisteredFragment(position)).refreshCalendar(true);
                    } else {
                        CalendarApplication.getInstance().setIsSwipeViewPager(0);

                        ((CalendarFragment) mAdapter.getRegisteredFragment(position)).setPreviousMonth();
                        ((CalendarFragment) mAdapter.getRegisteredFragment(position)).refreshCalendar(true);
                    }
                    mCurrentPosition = position;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setDateSelectionData(ArrayList<DateData> dateData) {

        mRvCalendar.setVisibility(View.VISIBLE);
        mTxtEventMessage.setVisibility(View.GONE);
        mTxtEventMessage.setText("");
        ArrayList<Object> items = new ArrayList<>();
        items.clear();
        if (dateData.size() > 0) {
            for (int i = 0; i < dateData.size(); i++) {
                if (dateData.get(i).getSection() != null && !dateData.get(i).getSection().isEmpty()) {
                    if (dateData.get(i).getSection() instanceof String) {
                        items.add(dateData.get(i).getSection());
                    }
                }
                if (dateData.size() > 0) {
                    for (int j = 0; j < dateData.get(i).getData().size(); j++) {
                        ArrayList<String> list = new ArrayList<>();

                        if (dateData.get(i).getData().get(j).getRemarks() != null)
                            list.add(dateData.get(i).getData().get(j).getRemarks());
                        else
                            list.add("");
                        if (dateData.get(i).getData().get(j).getSubject() != null)
                            list.add(dateData.get(i).getData().get(j).getSubject());
                        else
                            list.add("");
                        if (dateData.get(i).getData().get(j).getSubmissionDate() != null)
                            list.add(dateData.get(i).getData().get(j).getSubmissionDate());
                        else
                            list.add("");
                        if (dateData.get(i).getData().get(j).getTitle() != null)
                            list.add(dateData.get(i).getData().get(j).getTitle());
                        else
                            list.add("");
                        items.add(list);
                    }
                }
            }
        }
        if (items.size() == 0) {
            mRvCalendar.setVisibility(View.GONE);
            try {
                Date dateTemp = CalendarUtils.getCalendarDBFormat().parse(CalendarApplication.getInstance().getCurrentDate());
                mTxtEventMessage.setText(getResources().getString(R.string.no_events) + " (" + CalendarUtils.getCalendarDateFormat().format(dateTemp) + ")");
            } catch (ParseException e) {
                Log.e(TAG, e.getMessage(), e);
            }
            mTxtEventMessage.setVisibility(View.VISIBLE);
        } else {
            mRvCalendar.setLayoutManager(mLinearLayoutManager);
            mRvCalendar.setAdapter(new CalendarDataAdapter(items, this));
        }

    }
}
