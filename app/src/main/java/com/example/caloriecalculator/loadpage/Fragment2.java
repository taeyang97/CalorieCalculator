package com.example.caloriecalculator.loadpage;

import android.content.Context;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.caloriecalculator.DataBaseHelper;
import com.example.caloriecalculator.R;

import java.util.ArrayList;

public class Fragment2 extends Fragment {
    public static ArrayList<LoadData> items = new ArrayList<>();
    public static RecyclerView rViewmemo;
    public static RecyclerAdapterMemo rAdaptermemo;
    public static Context context;
    static boolean value = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragView = inflater.inflate(R.layout.fragment_2, container, false);

        DataBaseHelper dataBaseHelper = new DataBaseHelper(fragView.getContext());
        dataBaseHelper.updateItemsMemo();

        context = fragView.getContext();
        rViewmemo = (RecyclerView) fragView.findViewById(R.id.rview2);
        rViewmemo.setHasFixedSize(true); //  리사이클러뷰 안 아이템들의 크기를 가변적으로 바꿀지 아니면 일정한 크기를 사용할지를 지정

        layoutmanger();

        rAdaptermemo = new RecyclerAdapterMemo(context, items);
        rViewmemo.setAdapter(rAdaptermemo);
        return fragView;
    }

    // 레이아웃 변경을 동적으로 하기 위해 만든 메소드
    public static void layoutmanger() {

        if (value == true) {

            LinearLayoutManager layoutManager = new LinearLayoutManager(context,
                    LinearLayoutManager.VERTICAL, false);
            rViewmemo.setLayoutManager(layoutManager);

        } else {

            GridLayoutManager layoutManager = new GridLayoutManager(context,
                    2);
            rViewmemo.setLayoutManager(layoutManager);

        }
    }
}