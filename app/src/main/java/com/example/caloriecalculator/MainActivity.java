package com.example.caloriecalculator;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    AutoCompleteTextView tvMainAtuoText1, tvMainAtuoText2;
    Button btnMainConfirm, btnMainReset, btnMainExercise;
    TextView tvMainText1, tvMainText2, tvMainCalorieReset;
    String AutoText1;
    int AutoText2;
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
        btnMainExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(tvMainText2.getText().toString())){
                    Intent intent = new Intent(getApplicationContext(),Exercise.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(),"칼로리 섭취해야 운동을 보여줍니다.",Toast.LENGTH_SHORT).show();
                }
            }
        });
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
    protected void clearState(){
        SharedPreferences preferences = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }
}