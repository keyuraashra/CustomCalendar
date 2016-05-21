package com.riontech.calendarlibrary.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.riontech.calendarlibrary.fragment.CalendarFragment;

import java.util.GregorianCalendar;

/**
 * Created by Dhaval Soneji on 31/3/16.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    private SparseArray<Fragment> registeredFragments = new SparseArray<>();
    private int mCount;
    private static final String TAG = ViewPagerAdapter.class.getSimpleName();
    private Context mContext;
    private GregorianCalendar month;


    public ViewPagerAdapter(FragmentManager fm, Context ctx, int count) {
        super(fm);
        this.mContext = ctx;
        this.mCount = count;
        this.month = (GregorianCalendar) GregorianCalendar.getInstance();
    }

    @Override
    public Fragment getItem(int position) {
        return new CalendarFragment();
    }

    @Override
    public int getCount() {
        return this.mCount;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}
