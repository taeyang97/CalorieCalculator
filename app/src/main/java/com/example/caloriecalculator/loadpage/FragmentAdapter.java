package com.example.caloriecalculator.loadpage;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.caloriecalculator.loadpage.Fragment1;
import com.example.caloriecalculator.loadpage.Fragment2;

public class FragmentAdapter extends FragmentPagerAdapter {
    int numOfTabs; // 탭의 갯수를 받는 변수

    public FragmentAdapter(@NonNull FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    // position값에 따라 다른 프레그먼트를 보여준다.
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Fragment1 frag1 = new Fragment1();
                return frag1;
            case 1:
                Fragment2 frag2 = new Fragment2();
                return frag2;
            default:
                return null;
        }
    }

    // 프레그먼트 갯수
    @Override
    public int getCount() {
        return numOfTabs;
    }
}

