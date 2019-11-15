package com.autodokta.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 5/28/2017.
 */

/**
 * Class that stores fragments for tabs
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = "SectionsPagerAdapter";

    private final List<Fragment> mFragmentList = new ArrayList<>();


    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch(position){
            case 0:
                Offers offers = new Offers();
                return offers;

            case 1:
                EngineDriveTrain train = new EngineDriveTrain();
                return train;

            case 2:
                HeadLights lights = new HeadLights();
                return lights;

            case 3:
                BrakeSuspension brakeSuspension = new BrakeSuspension();
                return brakeSuspension;

            case 4:
                InteriorAccessories interiorAccessories = new InteriorAccessories();
                return interiorAccessories;

            case 5:
                ExteriorAccessories exteriorAccessories = new ExteriorAccessories();
                return exteriorAccessories;

            case 6:
                ToolsandGarage toolsandGarage = new ToolsandGarage();
                return toolsandGarage;

            case 7:
                Wheels wheels = new Wheels();
                return wheels;

                default:
                    return  mFragmentList.get(position);
        }
//        return mFragmentList.get(position);
    }


    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment){
        mFragmentList.add(fragment);
    }

}
