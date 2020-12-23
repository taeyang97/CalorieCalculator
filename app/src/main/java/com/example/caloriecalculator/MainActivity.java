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
import android.widget.ListAdapter;
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
import java.util.Calendar;

public class MainActivity extends StatusActivity {
    ImageButton ibMainCalorieReset, ibMainFoodName;
    AutoCompleteTextView tvMainAtuoText1;
    Button btnMainConfirm, btnMainReset, btnMainExercise, btnload;
    TextView tvMainText1, tvMainText2, tvMainText3, tvMainCalorieBar1, tvMainCalorieBar2;
    EditText etMainText, etFoodName, etFoodCal, etFoodCar, etFoodPro, etFoodFat;
    ProgressBar pbMainBar;
    String autoText1,date,today, dates;
    double autoText2, calorie, carbohydrate, protein, fat, car, pro, fa;
    int position, maxCalorie, progress=0, cYear, cMonth, cDay;
    ArrayList<String> nameData;
    ArrayAdapter<String> adapter;
    String num[] = {"0.2", "0.4", "0.6", "0.8", "1", "1.5", "2", "2.5", "3"};
    DataBaseHelper dataBaseHelper;
    SQLiteDatabase sqlDB;
    View foodView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar ac = getSupportActionBar();
        ac.hide();

        ibMainCalorieReset = (ImageButton)findViewById(R.id.ibMainCalorieReset);
        ibMainFoodName = (ImageButton)findViewById(R.id.ibMainFoodName);
        tvMainAtuoText1 = (AutoCompleteTextView)findViewById(R.id.tvMainAutoText1);
        btnMainConfirm = (Button)findViewById(R.id.btnMainConfirm);
        btnMainReset = (Button)findViewById(R.id.btnMainReset);
        btnMainExercise = (Button)findViewById(R.id.btnMainExercise);
        btnload = (Button)findViewById(R.id.btnload);
        etMainText = (EditText) findViewById(R.id.etMainText);
        pbMainBar = (ProgressBar)findViewById(R.id.pbMainbar);
        tvMainText1 = (TextView)findViewById(R.id.tvMainText1);
        tvMainText2 = (TextView)findViewById(R.id.tvMainText2);
        tvMainText3 = (TextView)findViewById(R.id.tvMainText3);
        tvMainCalorieBar1 = (TextView)findViewById(R.id.tvMainCalorieBar1);
        tvMainCalorieBar2 = (TextView)findViewById(R.id.tvMainCalorieBar2);

        dataBaseHelper = new DataBaseHelper(this);
        nameData = new ArrayList<String>();
        maxCalorieBar();
        todayCalorieBar();
        getVal();
        getVal2();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,nameData);
        tvMainAtuoText1.setAdapter(adapter);
        tvMainAtuoText1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getVal2();
            }
        });
        // 칼로리 저장 목록 버튼
        btnload.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoadCalorie.class);
                startActivity(intent);
            }
        });
        //음식 섭취량 대화상자 호출
        etMainText.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
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
                builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        // 음식 확인 버튼
        btnMainConfirm.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if(progress!=0){
                    dataBaseHelper.deleteDateTodayCalorie();
                }
                try{
                    autoText1 = tvMainAtuoText1.getText().toString();
                    getVal2();
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

                    /*sqlDB = dataBaseHelper.getWritableDatabase();
                    sqlDB.execSQL("INSERT INTO todayCalorie VALUES(" + autoText2 + ");");
                    sqlDB.close();*/ // db로 today칼로리 넘겨주는 코딩

                    dataBaseHelper.insertDataTodayCalorie(dates,String.valueOf(progress));

                    if(!TextUtils.isEmpty(tvMainText2.getText().toString())){
                        btnMainReset.setVisibility(View.VISIBLE);
                    }
                }catch (NumberFormatException e){
                    showToast("숫자를 입력해주세요.");
                }
            }
        });
        // 운동을 보여주는 버튼
        btnMainExercise.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if(progress != 0){
                    Intent mintent = new Intent(getApplicationContext(),ExerciseLast.class);
                    mintent.putExtra("MaxCalorie",maxCalorie);
                    mintent.putExtra("TodayCalorie",progress);
                    startActivity(mintent);
                } else {
                    showToast("칼로리 섭취해야 운동을 보여줍니다.");
                }
            }
        });
        // 칼로리 리셋 버튼
        ibMainCalorieReset.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                clearState();
                if(progress!=0){
                    dataBaseHelper.deleteDateTodayCalorie();
                }
                /*SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
                dataBaseHelper.onUpgrade(db,1,2);
                db.close();*/
                Intent intent = new Intent(getApplicationContext(),MaxCalorie.class);
                startActivity(intent);
            }
        });
        // db에 음식 추가 버튼
        ibMainFoodName.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                foodView = (View)View.inflate(MainActivity.this,R.layout.foodname,null);
                etFoodName = (EditText)foodView.findViewById(R.id.etFoodName);
                etFoodCal = (EditText)foodView.findViewById(R.id.etFoodCal);
                etFoodCar = (EditText)foodView.findViewById(R.id.etFoodCar);
                etFoodPro = (EditText)foodView.findViewById(R.id.etFoodPro);
                etFoodFat = (EditText)foodView.findViewById(R.id.etFoodFat);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("추가할 음식");
                builder.setView(foodView);
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(TextUtils.isEmpty(etFoodName.getText().toString())){
                            showToast("이름을 정확히 입력 해주세요.");
                        }
                        else{
                            sqlDB= dataBaseHelper.getWritableDatabase();
                            sqlDB.execSQL("INSERT INTO foodinformation VALUES('" + etFoodName.getText().toString()
                                    + "','" + etFoodCal.getText().toString()
                                    + "','" + etFoodCar.getText().toString()
                                    + "','" + etFoodPro.getText().toString()
                                    + "','" + etFoodFat.getText().toString() + "');");
                            sqlDB.close();
                            getVal();
                            adapter.clear();
                            adapter.addAll(nameData);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
                builder.setNegativeButton("취소",null);
                builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.show();
                getVal();
                getVal2();
            }
        });
        // reset 버튼 (누적 값 초기화)
        btnMainReset.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                progress=0;
                pbMainBar.setProgress(progress);
                tvMainCalorieBar1.setText(String.valueOf(progress));
                car=0;
                pro=0;
                fa=0;
                tvMainText1.setText("");
                tvMainText2.setText("");
                tvMainText3.setText("");
                btnMainReset.setVisibility(View.INVISIBLE);
                dataBaseHelper.deleteDateTodayCalorie();
                showToast("초기화 되었습니다");
            }
        });
    }
    // maxCalorie값 호출해 오는 메소드
    public void maxCalorieBar(){
        /*maxCalorie = DataBaseHelper.getmaxCalorie(this,maxCalorie);
        tvMainCalorieBar2.setText(String.valueOf(maxCalorie));
        pbMainBar.setMax(maxCalorie);*/
        Intent gIntent = getIntent();
        maxCalorie = gIntent.getIntExtra("MaxCalorie",0);
        tvMainCalorieBar2.setText(String.valueOf(maxCalorie));
        pbMainBar.setMax(maxCalorie);
    }

    // todayCalorie값 호출해 오는 메소드
    public void todayCalorieBar(){
        sqlDB = dataBaseHelper.getWritableDatabase();
        Cursor cursor = sqlDB.rawQuery("SELECT * FROM todayCalorie;",null);
        if(cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                date=cursor.getString(0);
                today=cursor.getString(1);
            }
        }
        cursor.close();
        dataBaseHelper.close();

        Calendar cal = Calendar.getInstance(); // 핸드폰의 날짜와 시간을 가져와 시간을 넣어준다.
        cYear = cal.get(Calendar.YEAR);
        cMonth = cal.get(Calendar.MONTH);
        cDay = cal.get(Calendar.DAY_OF_MONTH); // 그 달의 일수

        dates = cYear + "-" + (cMonth + 1) + "-" + cDay;
        if(!TextUtils.isEmpty(date)){
            if(date.equals(dates)){
                progress = Integer.parseInt(today);
                tvMainCalorieBar1.setText(today);
                pbMainBar.setProgress(progress);
                btnMainReset.setVisibility(View.VISIBLE);
            }
        }
    }

    //DB 음식 이름 호출하는 메소드
    public void getVal() {
        sqlDB = dataBaseHelper.getWritableDatabase();
        Cursor cursor = sqlDB.rawQuery("SELECT name FROM foodinformation;",null);
        while (cursor.moveToNext())
        {
            nameData.add(cursor.getString(0));
        }
        cursor.close();
        dataBaseHelper.close();
    }

    //DB 음식 칼로리 호출하는 메소드
    public void getVal2() {
        sqlDB = dataBaseHelper.getWritableDatabase();
        Cursor cursor = sqlDB.rawQuery("SELECT calorie, carbohydrate, protein, fat FROM foodinformation WHERE name='" +
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
        divide = Double.parseDouble(String.format("%.1f",divide));

        return divide;
    }
    protected void clearState(){ // 초기화 할 수 있는 메소드를 새로 만들어 주었다.
        // 전에 저장되었던 데이터를 초기화 할 때 사용한다.
        SharedPreferences preferences = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }
}