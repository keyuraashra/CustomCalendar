package com.riontech.calendarlibrary.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.riontech.calendarlibrary.CalendarApplication;
import com.riontech.calendarlibrary.R;
import com.riontech.calendarlibrary.dao.CalendarDecoratorDao;
import com.riontech.calendarlibrary.fragment.CalendarFragment;

import java.util.ArrayList;
import java.util.GregorianCalendar;


/**
 * Created By Dhaval Soneji
 */
public class CalendarAdapter extends BaseAdapter {
    private static final String TAG = CalendarAdapter.class.getSimpleName();
    private Context mContext;
    public static int firstDay;

    private ArrayList<CalendarDecoratorDao> mEventList;
    private View mPreviousView;

    private SparseArray<Fragment> registeredFragments = new SparseArray<>();

    public CalendarAdapter(Context c, ArrayList<CalendarDecoratorDao> items, GregorianCalendar month) {
        this.mEventList = items;
        mContext = c;

        firstDay = month.get(GregorianCalendar.DAY_OF_WEEK);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

    public int getCount() {
        return mEventList.size();
    }

    public Object getItem(int position) {
        return mEventList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        TextView dayView;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.calendar_item, null);
            int dimen = mContext.getResources().getDimensionPixelSize(R.dimen.common_40_dp);
            GridView.LayoutParams pParams = new GridView.LayoutParams(dimen, dimen);
            v.setLayoutParams(pParams);
        }

        dayView = (TextView) v.findViewById(R.id.date);

        String[] separatedTime = mEventList.get(position).getDate().split("-");

        String gridvalue = separatedTime[2].replaceFirst("^0*", "");


        v.setVisibility(View.VISIBLE);
        if ((Integer.parseInt(gridvalue) > 1) && (position < firstDay)) {

            v.setVisibility(View.INVISIBLE);

        } else if ((Integer.parseInt(gridvalue) < 7) && (position > 28)) {

            v.setVisibility(View.INVISIBLE);

        } else {

            dayView.setTextColor(Color.BLACK);
        }

        if (mEventList.get(position).getDate().equals(CalendarApplication.getInstance().getCurrentDate())) {
            setSelected(v, CalendarApplication.getInstance().getCurrentDate());
            mPreviousView = v;
        } else {
            v.setBackgroundResource(R.drawable.list_item_background);
            if (mEventList.get(position).getDate().equals(CalendarApplication.getInstance().getTodayDate())) {
                TextView txtTodayDate = (TextView) v.findViewById(R.id.date);
                txtTodayDate.setTextColor(ContextCompat.getColor(v.getContext(), R.color.colorPrimary));
            }
        }

        dayView.setText(gridvalue);

        String date = mEventList.get(position).getDate();

        if (date.length() == 1) {
            date = "0" + date;
        }


        ImageView imgDecorator1 = (ImageView) v.findViewById(R.id.date_icon);
        ImageView imgDecorator2 = (ImageView) v.findViewById(R.id.date_icon2);
        ImageView imgDecorator3 = (ImageView) v.findViewById(R.id.date_icon3);
        if (date.length() > 0 && mEventList != null && mEventList.get(position).getCount() > 0) {
            switch (mEventList.get(position).getCount()) {
                case 1:
                    imgDecorator1.setVisibility(View.VISIBLE);
                    imgDecorator2.setVisibility(View.GONE);
                    imgDecorator3.setVisibility(View.GONE);
                    break;
                case 2:
                    imgDecorator1.setVisibility(View.VISIBLE);
                    imgDecorator2.setVisibility(View.VISIBLE);
                    imgDecorator3.setVisibility(View.GONE);
                    break;
                case 3:
                    imgDecorator1.setVisibility(View.VISIBLE);
                    imgDecorator2.setVisibility(View.VISIBLE);
                    imgDecorator3.setVisibility(View.VISIBLE);
                    break;
                default:
                    imgDecorator1.setVisibility(View.VISIBLE);
                    imgDecorator2.setVisibility(View.VISIBLE);
                    imgDecorator3.setVisibility(View.VISIBLE);
                    break;
            }

        } else {
            imgDecorator1.setVisibility(View.INVISIBLE);
            imgDecorator2.setVisibility(View.INVISIBLE);
            imgDecorator3.setVisibility(View.INVISIBLE);
        }
        return v;
    }

    public View setSelected(View view, String selectedGridDate) {
        if (mPreviousView != null) {
            mPreviousView.findViewById(R.id.llCalendarItem);
            mPreviousView.setBackgroundResource(R.drawable.list_item_background);

            TextView txt = (TextView) mPreviousView.findViewById(R.id.date);
            txt.setTextColor(Color.BLACK);

            ImageView img1 = (ImageView) mPreviousView.findViewById(R.id.date_icon);
            img1.setImageDrawable(ContextCompat.getDrawable(mPreviousView.getContext(), R.drawable.circle_decorator));
            ImageView img2 = (ImageView) mPreviousView.findViewById(R.id.date_icon2);
            img2.setImageDrawable(ContextCompat.getDrawable(mPreviousView.getContext(), R.drawable.circle_decorator));
            ImageView img3 = (ImageView) mPreviousView.findViewById(R.id.date_icon3);
            img3.setImageDrawable(ContextCompat.getDrawable(mPreviousView.getContext(), R.drawable.circle_decorator));
        }

        mPreviousView = view;
        view.setBackgroundResource(R.drawable.circle_shape_selected);

        TextView txt = (TextView) view.findViewById(R.id.date);
        txt.setTextColor(Color.WHITE);

        ImageView img1 = (ImageView) view.findViewById(R.id.date_icon);
        img1.setImageDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.circle_decorator_white));
        ImageView img2 = (ImageView) view.findViewById(R.id.date_icon2);
        img2.setImageDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.circle_decorator_white));
        ImageView img3 = (ImageView) view.findViewById(R.id.date_icon3);
        img3.setImageDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.circle_decorator_white));

        CalendarApplication.getInstance().setCurrentDate(selectedGridDate);

        CalendarFragment.currentDateSelected = selectedGridDate;
        return view;
    }
}