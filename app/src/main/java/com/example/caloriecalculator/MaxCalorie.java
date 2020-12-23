package com.example.caloriecalculator;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import static com.example.caloriecalculator.DataBaseHelper.DB_PATH;

public class MaxCalorie extends StatusActivity {
    double average;
    int maxCalorie;
    EditText etMaxCalorie;
    TextView tvMaxCalorie;
    Button btnMaxCalorie;
    String[] select = {"", String.valueOf(25), String.valueOf(30), String.valueOf(35), String.valueOf(40)};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_max_calorie);
        ActionBar ac = getSupportActionBar();
        ac.hide();

        etMaxCalorie = (EditText)findViewById(R.id.etMaxCalorie);
        Spinner spinMaxCalorie = (Spinner) findViewById(R.id.spinMaxCalorie);
        btnMaxCalorie = (Button)findViewById(R.id.btnMaxCalorie);
        tvMaxCalorie = (TextView)findViewById(R.id.tvMaxCalorie);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,select);
        spinMaxCalorie.setAdapter(adapter);

        spinMaxCalorie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if (!TextUtils.isEmpty(etMaxCalorie.getText().toString())) {
                        average = (Double.parseDouble(etMaxCalorie.getText().toString()) - 100) * 0.9;
                        maxCalorie = (int)(average * Integer.parseInt(select[position]));
                        tvMaxCalorie.setText("나의 Max 칼로리 값   " + maxCalorie);
                        btnMaxCalorie.setEnabled(true);
                    }
                } catch (NumberFormatException e){
                    Toast.makeText(getApplicationContext(),"값을 올바르게 입력해 주세요",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //메인 페이지로 넘어가는 버튼
        btnMaxCalorie.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Intent mintent = new Intent(getApplicationContext(),MainActivity.class);
                mintent.putExtra("MaxCalorie",maxCalorie);
                Toast.makeText(getApplicationContext(),"max값을 정하였습니다.",Toast.LENGTH_SHORT).show();
                startActivity(mintent);
            }
        });
    }

    //Max 칼로리 값을 한 번 저장하는 메소드
    @Override
    protected void onPause() { // 하나만 저장할 수 있는 저장소가 있다.
        super.onPause();
        SharedPreferences preferences = getSharedPreferences("pref", Activity.MODE_PRIVATE); // 데이터를 임시로 저장하는 클래스
        SharedPreferences.Editor editor = preferences.edit();// 데이터를 저장할 수 있는 변수
        editor.putString("vkey", String.valueOf(maxCalorie));
        editor.commit(); // commit() 메소드를 사용해야 실제로 저장이 된다.
    }

    // 저장한 칼로리 값을 가져오는 메소드
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences("pref", Activity.MODE_PRIVATE); // 저장소의 있는 값을 받아온다.
        if((preferences != null) && (preferences.contains("vkey"))) { // 아무 값이 없지 않거나, 값이 존재할 경우 실행
            tvMaxCalorie.setText("나의 Max 칼로리 값   " + Integer.parseInt(preferences.getString("vkey","")));
            maxCalorie = Integer.parseInt(preferences.getString("vkey",""));
            if(maxCalorie!=0) {
                Intent mintent = new Intent(getApplicationContext(), MainActivity.class);
                mintent.putExtra("MaxCalorie", maxCalorie);
                startActivity(mintent);
            }
        }
    }
}