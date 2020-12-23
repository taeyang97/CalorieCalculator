package com.example.caloriecalculator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Dictionary;

public class Fragment1 extends Fragment {
    public static ArrayList<ItemData> items = new ArrayList<>();
    RecyclerView rView;
    public static RecyclerAdapter rAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragView = inflater.inflate(R.layout.fragment_1, container, false);

        DataBaseHelper dataBaseHelper = new DataBaseHelper(fragView.getContext());
        dataBaseHelper.updateItems();

        Context context = fragView.getContext();
        rView = (RecyclerView)fragView.findViewById(R.id.rview1);
        rView.setHasFixedSize(true); //  리사이클러뷰 안 아이템들의 크기를 가변적으로 바꿀지 아니면 일정한 크기를 사용할지를 지정

        LinearLayoutManager layoutManager = new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL,false);
        rView.setLayoutManager(layoutManager);

        rAdapter = new RecyclerAdapter(context,items);
        rView.setAdapter(rAdapter);
        return fragView;
    }

    void showToast(String msg){
        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
    }
}