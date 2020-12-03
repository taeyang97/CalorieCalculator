package com.example.caloriecalculator;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase sqlDB;
    AutoCompleteTextView tvMainAtuoText1, tvMainAtuoText2;
    Button btnMainConfirm, btnMainReset, btnMainExercise;
    TextView tvMainText1, tvMainText2, tvMainCalorieReset;
    String AutoText1;
    int AutoText2;
    ArrayList<String> nameData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar ac = getSupportActionBar();
        ac.hide();
        tvMainAtuoText1 = (AutoCompleteTextView)findViewById(R.id.tvMainAutoText1);
        tvMainAtuoText2 = (AutoCompleteTextView)findViewById(R.id.tvMainAutoText2);
        btnMainConfirm = (Button)findViewById(R.id.btnMainConfirm);
        btnMainReset = (Button)findViewById(R.id.btnMainReset);
        btnMainExercise = (Button)findViewById(R.id.btnMainExercise);
        tvMainText1 = (TextView)findViewById(R.id.tvMainText1);
        tvMainText2 = (TextView)findViewById(R.id.tvMainText2);
        tvMainCalorieReset = (TextView)findViewById(R.id.tvMainCalorieReset);
        nameData = new ArrayList<String>();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,nameData);
        tvMainAtuoText1.setAdapter(adapter);
        getVal();

        // 음식 확인 버튼
        btnMainConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutoText1 = tvMainAtuoText1.getText().toString();
                AutoText2 = Integer.parseInt(tvMainAtuoText2.getText().toString()) * 500;
                tvMainText1.setText(AutoText1);
                tvMainText2.setText(AutoText2 + "kcal");
                if(!TextUtils.isEmpty(tvMainText2.getText().toString())){
                    btnMainReset.setVisibility(View.VISIBLE);
                }
            }
        });
        // 운동을 보여주는 버튼
        btnMainExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(tvMainText2.getText().toString())){
                    Intent intent = new Intent(getApplicationContext(),Exercise.class);
                    startActivity(intent);
                } else {
                    showToast("칼로리 섭취해야 운동을 보여줍니다.");
                }
            }
        });
        // 칼로리 리셋 버튼
        tvMainCalorieReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMainCalorieReset.setTextColor(Color.parseColor("#ff0000"));
                clearState();
                Intent intent = new Intent(getApplicationContext(),MaxCalorie.class);
                startActivity(intent);
            }
        });
    }

    // 저장된 값을 초기화 시키는 메소드
    protected void clearState(){
        SharedPreferences preferences = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    //DB호출하는 메소드
    public void getVal() {

        DataBaseHelper dbHelper = new DataBaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT name FROM foodinformation;",null);
        while (cursor.moveToNext())
        {
            nameData.add(cursor.getString(0));
        }

        cursor.close();
        dbHelper.close();
    }

    //토스트 메소드
    void showToast(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }

}