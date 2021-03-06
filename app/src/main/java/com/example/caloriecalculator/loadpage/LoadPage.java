package com.example.caloriecalculator.loadpage;

import androidx.appcompat.app.ActionBar;

import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.caloriecalculator.DataBaseHelper;
import com.example.caloriecalculator.option.OnSingleClickListener;
import com.example.caloriecalculator.R;
import com.example.caloriecalculator.option.StatusActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;


public class LoadPage extends StatusActivity {
    TabLayout tabs;
    ViewPager vPage1;
    FragmentAdapter adapter;
    ImageButton ibGridView, ibSearchOff, ibSearchOn;
    View searchView;
    int cYear, cMonth, cDay;
    String _id, date, today, max;
    String _ids, dates, memo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loadpage);
        ActionBar ac = getSupportActionBar();
        ac.hide();
        tabs = (TabLayout) findViewById(R.id.tabs);
        vPage1 = (ViewPager) findViewById(R.id.vPage1);
        ibGridView = (ImageButton) findViewById(R.id.ibGridView);
        ibSearchOff = (ImageButton) findViewById(R.id.ibSearchOff);
        ibSearchOn = (ImageButton) findViewById(R.id.ibSearchOn);

        tabs.addTab(tabs.newTab().setText("칼로리 저장 목록"));
        tabs.addTab(tabs.newTab().setText("메모장"));
        tabs.setTabGravity(tabs.GRAVITY_FILL);

        //어댑터 설정
        adapter = new FragmentAdapter(getSupportFragmentManager(), 2);
        vPage1.setAdapter(adapter);

        //리스트뷰와 그리드뷰 실시간 변경
        ibGridView.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (Fragment1.value == true) {
                    Fragment1.value = false;
                    Fragment2.value = false;
                    Fragment1.layoutmanger();
                    Fragment2.layoutmanger();
                    ibGridView.setImageResource(R.drawable.listimage);
                } else {
                    Fragment1.value = true;
                    Fragment2.value = true;
                    Fragment1.layoutmanger();
                    Fragment2.layoutmanger();
                    ibGridView.setImageResource(R.drawable.gridimage);
                }
            }
        });

        // 저장목록 전체보기
        ibSearchOff.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                DataBaseHelper dataBaseHelper = new DataBaseHelper(LoadPage.this);
                dataBaseHelper.updateItems();
                dataBaseHelper.updateItemsMemo();
                Fragment1.rAdapter.notifyDataSetChanged();
                Fragment2.rAdaptermemo.notifyDataSetChanged();
                showToast("전체보기");
            }
        });

        // 저장목록 날짜별 나타내기
        ibSearchOn.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                searchView = (View) View.inflate(LoadPage.this, R.layout.loadpagesearchview, null);
                DatePicker dpsearchview = (DatePicker) searchView.findViewById(R.id.dpsearchview);
                AlertDialog.Builder builder = new AlertDialog.Builder(LoadPage.this);
                if (dpsearchview.getParent() != null)
                    ((ViewGroup) dpsearchview.getParent()).removeView(dpsearchview);
                builder.setView(dpsearchview);

                Calendar cal = Calendar.getInstance();
                cYear = cal.get(Calendar.YEAR);
                cMonth = cal.get(Calendar.MONTH);
                cDay = cal.get(Calendar.DAY_OF_MONTH);

                dpsearchview.init(cYear, cMonth, cDay, new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        cYear = year;
                        cMonth = monthOfYear;
                        cDay = dayOfMonth;
                    }
                });

                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataBaseHelper dbHelper = new DataBaseHelper(LoadPage.this);
                        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();

                        Fragment1.items.clear(); // 초기화
                        Cursor cursor = sqlDB.rawQuery("SELECT * FROM loadCalorie WHERE date='" +
                                cYear + "-" + (cMonth + 1) + "-" + cDay + "'", null);
                        if (cursor.getCount() != 0) {
                            while (cursor.moveToNext()) {
                                _id = cursor.getString(0);
                                date = cursor.getString(1);
                                today = cursor.getString(2);
                                max = cursor.getString(3);

                                Fragment1.items.add(0, new LoadData(_id, date, today, max));
                            }
                        }

                        Fragment2.items.clear(); // 초기화
                        Cursor cursor2 = sqlDB.rawQuery("SELECT * FROM memo WHERE dates='" +
                                cYear + "-" + (cMonth + 1) + "-" + cDay + "'", null);
                        if (cursor2.getCount() != 0) {
                            while (cursor2.moveToNext()) {
                                _ids = cursor2.getString(0);
                                dates = cursor2.getString(1);
                                memo = cursor2.getString(2);

                                Fragment2.items.add(0, new LoadData(_ids, dates, memo));
                            }
                        }

                        cursor.close();
                        cursor2.close();
                        dbHelper.close();
                        showToast(cYear + "년" + (cMonth + 1) + "월" + cDay + "일로 변경되었습니다.");
                        Fragment1.rAdapter.notifyDataSetChanged();
                        Fragment2.rAdaptermemo.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("취소", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        //탭메뉴를 클릭하면 뷰페이저에 해당 프레그먼트 변경 = 싱크화
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(vPage1));

        //뷰페이저를 체인지하면 해당 탭메뉴 변경 = 싱크화
        vPage1.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
    }

    void showToast(String msg) {
        Toast.makeText(LoadPage.this, msg, Toast.LENGTH_LONG).show();
    }

}
