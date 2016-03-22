package com.reddevil.loadshedding.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.reddevil.loadshedding.Groups;
import com.reddevil.loadshedding.helper.Routine;

/**
 * Created by mansubh on 12/20/15.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    int numberOfPage;            //store the number of the tab
    Bundle bundle;
    Routine mRoutine;

    public ViewPagerAdapter(FragmentManager fm,int mNumbOfTabs,Routine routine){
        super(fm);
        this.numberOfPage=mNumbOfTabs;
        bundle = new Bundle();
        this.mRoutine = routine;
    }

    //returns the fragment for every postion in the view pager
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Groups tab1 = new Groups();
                bundle.putInt("GROUP",1);
                tab1.setArguments(bundle);
                return tab1;
            case 1:
                Groups tab2 = new Groups();
                bundle.putInt("GROUP",2);
                tab2.setArguments(bundle);
                return tab2;
            case 2:
                Groups tab3 = new Groups();
                bundle.putInt("GROUP",3);
                tab3.setArguments(bundle);
                return tab3;
            case 3:
                Groups tab4 = new Groups();
                bundle.putInt("GROUP",4);
                tab4.setArguments(bundle);
                return tab4;
            case 4:
                Groups tab5 = new Groups();
                bundle.putInt("GROUP",5);
                tab5.setArguments(bundle);
                return tab5;
            case 5:
                Groups tab6 = new Groups();
                bundle.putInt("GROUP",6);
                tab6.setArguments(bundle);
                return tab6;
            case 6:
                Groups tab7 = new Groups();
                bundle.putInt("GROUP",7);
                tab7.setArguments(bundle);
                return tab7;
        }
        return null;
    }


    @Override
    public int getCount() {
        return numberOfPage;
    }


}
