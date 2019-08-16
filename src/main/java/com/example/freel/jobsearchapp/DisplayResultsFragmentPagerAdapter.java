package com.example.freel.jobsearchapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.HashMap;

public class DisplayResultsFragmentPagerAdapter extends FragmentPagerAdapter {
    private int mCurrentPosition = -1;
    private Context mContext;
    String joblisting;
    String URLtext;
    private HashMap<Integer, Fragment> fragmentHashMap = new HashMap<>();

    public DisplayResultsFragmentPagerAdapter(Context context, FragmentManager fm, String jl, String contactinfo) {
        super(fm);
        mContext = context;
        joblisting = jl;
        URLtext = contactinfo;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {

            Bundle args = new Bundle();
            args.putString("joblisting", joblisting);
            RequirementsTextFragment rtf = new RequirementsTextFragment();
            rtf.setArguments(args);
            if (fragmentHashMap.get(position) != null) {
                return fragmentHashMap.get(position);
            }

            fragmentHashMap.put(position, rtf);

            return rtf;

        } else  {

            Bundle contactInfo = new Bundle();
            contactInfo.putString("URL", URLtext);
            URLTextFragment utf = new URLTextFragment();
            utf.setArguments(contactInfo);
            return utf;

        }

    }


    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);

        if (position != mCurrentPosition && container instanceof CustomPager) {
            Fragment fragment = (Fragment) object;
            CustomPager pager = (CustomPager) container;

            if (fragment != null && fragment.getView() != null) {
                mCurrentPosition = position;
                pager.measureCurrentView(fragment.getView());
            }
        }
    }
    // This determines the number of tabs
    @Override
    public int getCount() {
        return 2;
    }

    public void addFragment(Fragment fragment, String title)
    {


    }

    Drawable myDrawable;
    String title;

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                myDrawable = mContext.getResources().getDrawable(R.drawable.ic_bulletin_list_icon);
                title = "req";
                break;
            case 1:
                myDrawable = mContext.getResources().getDrawable(R.drawable.ic_phone_receiver);
                title = "contact";
                break;

            default:
                break;
        }

        return title;
    }

}
