package com.example.caloriecalculator;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;


public class LoadCalorie extends StatusActivity {
    TabLayout tabs;
    ViewPager vPage1;
    MyPagerAdapter adapter;
    ImageButton ibSearchOff, ibSearchOn;
    View searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loadcalorie);
        ActionBar ac = getSupportActionBar();
        ac.hide();
        tabs=(TabLayout)findViewById(R.id.tabs);
        vPage1=(ViewPager)findViewById(R.id.vPage1);
        ibSearchOff = (ImageButton)findViewById(R.id.ibSearchOff);
        ibSearchOn = (ImageButton)findViewById(R.id.ibSearchOn);

        tabs.addTab(tabs.newTab().setText("칼로리 저장 목록"));
        tabs.addTab(tabs.newTab().setText("메모장"));
        tabs.setTabGravity(tabs.GRAVITY_FILL);

        //어댑터 설정
        adapter=new MyPagerAdapter(getSupportFragmentManager(),2);
        vPage1.setAdapter(adapter);

        ibSearchOff.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {

            }
        });

        ibSearchOn.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                searchView = (View)View.inflate(LoadCalorie.this,R.layout.searchview,null);
                DatePicker dpsearchview = (DatePicker)searchView.findViewById(R.id.dpsearchview);
                AlertDialog.Builder builder = new AlertDialog.Builder(LoadCalorie.this);
                if (dpsearchview.getParent() != null)
                    ((ViewGroup) dpsearchview.getParent()).removeView(dpsearchview);
                builder.setView(dpsearchview);
                builder.setPositiveButton("확인",null);
                builder.setNegativeButton("취소",null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        vPage1.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==1 && position==0){
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //탭메뉴를 클릭하면 뷰페이저에 해당 프레그먼트 변경 = 싱크화
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(vPage1));

        //뷰페이저를 체인지하면 해당 탭메뉴 변경 = 싱크화
        vPage1.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
    }
}
