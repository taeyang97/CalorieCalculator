package com.example.caloriecalculator;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
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
    ImageButton ibMainCalorieReset;
    AutoCompleteTextView tvMainAtuoText1;
    Button btnMainConfirm, btnMainReset, btnMainExercise;
    TextView tvMainText1, tvMainText2, tvMainText3, tvMainCalorieBar1, tvMainCalorieBar2;
    EditText etMainText;
    ProgressBar pbMainBar;
    String autoText1;
    double autoText2, calorie, carbohydrate, protein, fat, car, pro, fa;
    int position, maxCalorie, progress;
    ArrayList<String> nameData;
    String num[] = {"0.2", "0.4", "0.6", "0.8", "1", "1.5", "2", "2.5", "3"};
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar ac = getSupportActionBar();
        ac.hide();
        ibMainCalorieReset = (ImageButton)findViewById(R.id.ibMainCalorieReset);
        tvMainAtuoText1 = (AutoCompleteTextView)findViewById(R.id.tvMainAutoText1);
        btnMainConfirm = (Button)findViewById(R.id.btnMainConfirm);
        btnMainReset = (Button)findViewById(R.id.btnMainReset);
        btnMainExercise = (Button)findViewById(R.id.btnMainExercise);
        etMainText = (EditText) findViewById(R.id.etMainText);
        pbMainBar = (ProgressBar)findViewById(R.id.pbMainbar);
        tvMainText1 = (TextView)findViewById(R.id.tvMainText1);
        tvMainText2 = (TextView)findViewById(R.id.tvMainText2);
        tvMainText3 = (TextView)findViewById(R.id.tvMainText3);
        tvMainCalorieBar1 = (TextView)findViewById(R.id.tvMainCalorieBar1);
        tvMainCalorieBar2 = (TextView)findViewById(R.id.tvMainCalorieBar2);
        dataBaseHelper = new DataBaseHelper(this);

        nameData = new ArrayList<String>();
        maxCaloriebar();
        getVal();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,nameData);
        tvMainAtuoText1.setAdapter(adapter);
        tvMainAtuoText1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getVal2();
            }
        });

        //음식 섭취량 대화상자 호출
        etMainText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("음식 섭취량")
                        .setSingleChoiceItems(num, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                position = which;
                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                etMainText.setText(num[position]);
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        // 음식 확인 버튼
        btnMainConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoText1 = tvMainAtuoText1.getText().toString();
                autoText2 = Double.parseDouble(etMainText.getText().toString()) * calorie;
                car += Double.parseDouble(etMainText.getText().toString()) * carbohydrate;
                pro += Double.parseDouble(etMainText.getText().toString()) * protein;
                fa += Double.parseDouble(etMainText.getText().toString()) * fat;
                div(car);
                div(pro);
                div(fa);
                tvMainText1.setText(autoText1 + etMainText.getText().toString() + "인분");
                tvMainText2.setText((int)autoText2 + " kcal");
                tvMainText3.setText("\n탄수화물 : " + car +
                "\n단백질 : " + pro + "\n지방 : " + fa);
                progress += (int)autoText2;
                pbMainBar.setProgress(progress);
                tvMainCalorieBar1.setText(String.valueOf(progress));
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
        ibMainCalorieReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
                dataBaseHelper.onUpgrade(db,1,2);
                db.close();
                Intent intent = new Intent(getApplicationContext(),MaxCalorie.class);
                startActivity(intent);
            }
        });
        btnMainReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress=0;
                pbMainBar.setProgress(progress);
                tvMainCalorieBar1.setText(String.valueOf(progress));
                car=0;
                pro=0;
                fa=0;
                tvMainText1.setText("");
                tvMainText2.setText("");
                tvMainText3.setText("");
            }
        });
    }
    // maxCalroe값 호출해 오는 메소드
    public void maxCaloriebar(){
        maxCalorie = DataBaseHelper.getmaxCalorie(this,maxCalorie);
        tvMainCalorieBar2.setText(String.valueOf(maxCalorie));
        pbMainBar.setMax(maxCalorie);
    }

    //DB 음식 이름 호출하는 메소드
    public void getVal() {
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM foodinformation;",null);
        while (cursor.moveToNext())
        {
            nameData.add(cursor.getString(0));
        }
        cursor.close();
        dataBaseHelper.close();
    }

    //DB 음식 칼로리 호출하는 메소드
    public void getVal2() {
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT calorie, carbohydrate, protein, fat FROM foodinformation WHERE name='" +
                tvMainAtuoText1.getText().toString() + "';",null);
        while (cursor.moveToNext())
        {
            calorie=cursor.getDouble(0);
            carbohydrate=cursor.getDouble(1);
            protein=cursor.getDouble(2);
            fat=cursor.getDouble(3);
        }
        cursor.close();
        dataBaseHelper.close();
    }

    //토스트 메소드
    void showToast(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }
    // 소수점 첫째자리까지 구하는 메소드
    double div(double divide){
        divide = (int)(divide*10)/10;
        return divide;
    }
}