package com.example.caloriecalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;


public class LoadCalorie extends AppCompatActivity {
    TabLayout tabs;
    ViewPager vPage1;
    MyPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loadcalorie);
        tabs=(TabLayout)findViewById(R.id.tabs);
        vPage1=(ViewPager)findViewById(R.id.vPage1);

        tabs.addTab(tabs.newTab().setText("칼로리 저장 목록"));
        tabs.addTab(tabs.newTab().setText("임시 페이지"));
        tabs.setTabGravity(tabs.GRAVITY_FILL);

        //어댑터 설정
        adapter=new MyPagerAdapter(getSupportFragmentManager(),2);
        vPage1.setAdapter(adapter);
        //탭메뉴를 클릭하면 뷰페이저에 해당 프레그먼트 변경 = 싱크화
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(vPage1));

        //뷰페이저를 체인지하면 해당 탭메뉴 변경 = 싱크화
        vPage1.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
    }
}
