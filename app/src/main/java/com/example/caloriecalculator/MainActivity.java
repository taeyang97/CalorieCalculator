package com.example.caloriecalculator;


import android.app.Activity;
import android.app.AlertDialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.os.SystemClock;
import android.text.TextUtils;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.ProgressBar;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends StatusActivity {
    ImageButton ibMainCalorieReset, ibMainFoodName, ibMainExercise, ibload;
    AutoCompleteTextView tvMainAtuoText1;
    Button btnMainConfirm, btnMainReset, btnNo, btnYes, btnCancel, btnChoose, btnFoodNum, btnFoodPassive;
    TextView tvMainText1, tvMainText2, tvMainText3, tvMainCalorieBar1, tvMainCalorieBar2;
    EditText etMainText, etFoodName, etFoodCal, etFoodCar, etFoodPro, etFoodFat, etFoodNum;
    RadioButton[] rbBtnNum = new RadioButton[8];
    Integer rbBtnNumID[] = {R.id.rbBtnNum1, R.id.rbBtnNum2, R.id.rbBtnNum3, R.id.rbBtnNum4,
            R.id.rbBtnNum5, R.id.rbBtnNum6, R.id.rbBtnNum7, R.id.rbBtnNum8};
    ProgressBar pbMainBar;
    String autoText1,date,today, dates, position;
    double autoText2, calorie, carbohydrate, protein, fat, car, pro, fa;
    int maxCalorie, progress=0, cYear, cMonth, cDay;
    ArrayList<String> nameData;
    ArrayAdapter<String> adapter;
    DataBaseHelper dataBaseHelper;
    SQLiteDatabase sqlDB;
    Dialog resetdialog, fooddialog, foodNumDialog;

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
        ibMainExercise = (ImageButton) findViewById(R.id.ibMainExercise);
        ibload = (ImageButton)findViewById(R.id.ibload);
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

        if(progress>maxCalorie/3){
            pbMainBar.setProgressDrawable(getDrawable(R.drawable.progressbar2));
        }
        if(progress>maxCalorie/2){
            pbMainBar.setProgressDrawable(getDrawable(R.drawable.progressbar3));
        }
        if(progress>maxCalorie/1.2){
            pbMainBar.setProgressDrawable(getDrawable(R.drawable.progressbar4));
        }

        // 자동완성 텍스트
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,nameData);
        tvMainAtuoText1.setAdapter(adapter);
        tvMainAtuoText1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getVal2();
            }
        });

        // 칼로리 저장 목록 버튼
        ibload.setOnClickListener(new OnSingleClickListener() {
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

                foodNumDialog = new Dialog(MainActivity.this);
                foodNumDialog.setContentView(R.layout.foodnumdialog);

                etFoodNum = (EditText)foodNumDialog.findViewById(R.id.etFoodNum);
                btnFoodPassive = (Button)foodNumDialog.findViewById(R.id.btnFoodPassive);
                btnFoodNum = (Button)foodNumDialog.findViewById(R.id.btnFoodNum);
                for(int i=0; i<rbBtnNumID.length; i++){
                    rbBtnNum[i] = (RadioButton)foodNumDialog.findViewById(rbBtnNumID[i]);
                }

                foodNumDialog.show();
                foodNumDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                for(int i=0; i<rbBtnNumID.length; i++){
                    final int index;
                    index = i;
                    rbBtnNum[index].setOnClickListener(new OnSingleClickListener() {
                        @Override
                        public void onSingleClick(View v) {
                            position = rbBtnNum[index].getText().toString();
                        }
                    });
                }

                btnFoodPassive.setOnClickListener(new OnSingleClickListener() {
                    @Override
                    public void onSingleClick(View v) {
                        try{
                            etMainText.setText(etFoodNum.getText().toString());
                            foodNumDialog.dismiss();
                        } catch (NumberFormatException e){
                            showToast("숫자를 입력해주세요.");
                        }
                    }
                });
                btnFoodNum.setOnClickListener(new OnSingleClickListener() {
                    @Override
                    public void onSingleClick(View v) {
                        etMainText.setText(position);
                        foodNumDialog.dismiss();
                    }
                });
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

                    car = (int)(car*10)/10;
                    pro = (int)(pro*10)/10;
                    fa = (int)(fa*10)/10;

                    tvMainText1.setText(autoText1 + etMainText.getText().toString() + "인분");
                    tvMainText2.setText((int)autoText2 + " kcal");
                    tvMainText3.setText("\n탄수화물 : " + (int)car + " + " + carbohydrate +
                            "\n단백질 : " + (int)pro + " + " + protein + "\n지방 : " + (int)fa + " + " + fat);
                    progress += (int)autoText2;

                    if(progress>maxCalorie/3){
                        pbMainBar.setProgressDrawable(getDrawable(R.drawable.progressbar2));
                    }
                    if(progress>maxCalorie/2){
                        pbMainBar.setProgressDrawable(getDrawable(R.drawable.progressbar3));
                    }
                    if(progress>maxCalorie/1.2){
                        pbMainBar.setProgressDrawable(getDrawable(R.drawable.progressbar4));
                    }
                    new Thread(){
                        @Override
                        public void run() {
                            for(int i=1; i<=(int)autoText2; i++){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        pbMainBar.incrementProgressBy(1);
                                    }
                                });
                                SystemClock.sleep(5);
                            }
                        }
                    }.start();

                    // setProgess는 view를 다시만들어야 하지만 이건 아니다. 결론 : 더 빠르다.
                    tvMainCalorieBar1.setText(progress + "kcal");

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
        ibMainExercise.setOnClickListener(new OnSingleClickListener() {
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
                resetdialog = new Dialog(MainActivity.this);
                resetdialog.setContentView(R.layout.resetdialog);

                btnNo = (Button)resetdialog.findViewById(R.id.btnNo);
                btnYes = (Button)resetdialog.findViewById(R.id.btnYes);

                resetdialog.show();
                resetdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                btnYes.setOnClickListener(new OnSingleClickListener() {
                    @Override
                    public void onSingleClick(View v) {
                        clearState();
                        if(progress!=0){
                            dataBaseHelper.deleteDateTodayCalorie();
                        }
                        Intent intent = new Intent(getApplicationContext(),MaxCalorie.class);
                        startActivity(intent);
                        showToast("Max값을 다시 정해주세요.");
                    }
                });
                btnNo.setOnClickListener(new OnSingleClickListener() {
                    @Override
                    public void onSingleClick(View v) {
                        resetdialog.dismiss();
                    }
                });
            }
        });

        // db에 음식 추가 버튼
        ibMainFoodName.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                fooddialog = new Dialog(MainActivity.this);
                fooddialog.setContentView(R.layout.foodname);

                etFoodName = (EditText)fooddialog.findViewById(R.id.etFoodName);
                etFoodCal = (EditText)fooddialog.findViewById(R.id.etFoodCal);
                etFoodCar = (EditText)fooddialog.findViewById(R.id.etFoodCar);
                etFoodPro = (EditText)fooddialog.findViewById(R.id.etFoodPro);
                etFoodFat = (EditText)fooddialog.findViewById(R.id.etFoodFat);
                btnCancel = (Button)fooddialog.findViewById(R.id.btnCancel);
                btnChoose = (Button)fooddialog.findViewById(R.id.btnChoose);

                fooddialog.show();
                fooddialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                btnChoose.setOnClickListener(new OnSingleClickListener() {
                    @Override
                    public void onSingleClick(View v) {
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
                            adapter.clear();
                            getVal();
                            adapter.addAll(nameData);
                            adapter.notifyDataSetChanged();
                            showToast("음식이 저장되었습니다.");
                        }
                    }
                });
                btnCancel.setOnClickListener(new OnSingleClickListener() {
                    @Override
                    public void onSingleClick(View v) {
                        fooddialog.dismiss();
                    }
                });
            }
        });

        // reset 버튼 (누적 값 초기화)
        btnMainReset.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                progress=0;
                pbMainBar.setProgress(progress);
                pbMainBar.setProgressDrawable(getDrawable(R.drawable.progressbar));
                tvMainCalorieBar1.setText(progress + "kcal");
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
        tvMainCalorieBar2.setText(maxCalorie + "kcal");
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
                tvMainCalorieBar1.setText(today + "kcal");
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

    // 운동 후 칼로리 소모 하고 다시 메인 화면으로 왔을 때 갱신 하기 위해 쓰는 메소드
    @Override
    protected void onResume() {
        super.onResume();
        todayCalorieBar();
    }

    // 초기화 할 수 있는 메소드
    protected void clearState(){
        // 전에 저장되었던 데이터를 초기화 할 때 사용한다.
        SharedPreferences preferences = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    //뒤로가기 누를 시 전의 액티비티로 가지 않고 종료
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityCompat.finishAffinity(this);
    }
}