package com.example.caloriecalculator;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ExerciseLast extends AppCompatActivity {
    Button btnExerciseSel;
    TextView tvExerciseCal, tvCalorieMin;
    int maxCalorie, todayCalorie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_last);
        ActionBar ac = getSupportActionBar();
        ac.hide();
        btnExerciseSel = (Button)findViewById(R.id.btnExercisesSel);
        tvExerciseCal = (TextView)findViewById(R.id.tvExerciseCal);
        tvCalorieMin = (TextView)findViewById(R.id.tvCalorieMin);
        maxCaloriebar();
        btnExerciseSel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void maxCaloriebar(){
        /*maxCalorie = DataBaseHelper.getmaxCalorie(this,maxCalorie);
        todayCalorie = DataBaseHelper.gettodayCalorie(this,todayCalorie);
        tvExerciseCal.setText(todayCalorie + "kal : " + String.valueOf(maxCalorie) + "kal");*/

        Intent gIntent = getIntent();
        maxCalorie = gIntent.getIntExtra("MaxCalorie",0);
        todayCalorie = gIntent.getIntExtra("TodayCalorie",0);
        tvExerciseCal.setText(todayCalorie + "kal : " + maxCalorie + "kal");
        tvCalorieMin.setText("+" + (maxCalorie-todayCalorie) + "kal");
    }
}