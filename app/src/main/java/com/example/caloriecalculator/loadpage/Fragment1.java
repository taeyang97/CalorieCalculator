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


public class Fragment1 extends Fragment {
    public static ArrayList<LoadData> items = new ArrayList<>();
    public static RecyclerView rView;
    public static RecyclerAdapter rAdapter;
    public static Context context;
    static boolean value = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragView = inflater.inflate(R.layout.fragment_1, container, false);

        DataBaseHelper dataBaseHelper = new DataBaseHelper(fragView.getContext());
        dataBaseHelper.updateItems();

        context = fragView.getContext();
        rView = (RecyclerView) fragView.findViewById(R.id.rview1);
        rView.setHasFixedSize(true); //  리사이클러뷰 안 아이템들의 크기를 가변적으로 바꿀지 아니면 일정한 크기를 사용할지를 지정

        layoutmanger();

        rAdapter = new RecyclerAdapter(context, items);
        rView.setAdapter(rAdapter);
        return fragView;
    }

    // 레이아웃 변경을 동적으로 하기 위해 만든 메소드
    public static void layoutmanger() {

        if (value == true) {

            LinearLayoutManager layoutManager = new LinearLayoutManager(context,
                    LinearLayoutManager.VERTICAL, false);
            rView.setLayoutManager(layoutManager);

        } else if (value == false) {

            GridLayoutManager layoutManager = new GridLayoutManager(context,
                    2);
            rView.setLayoutManager(layoutManager);

        }
        ;
    }
}