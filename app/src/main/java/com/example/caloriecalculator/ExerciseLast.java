package com.example.caloriecalculator;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListPopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Field;

public class ExerciseLast extends AppCompatActivity {
    Button btnExerciseSel;
    TextView tvExerciseCal, tvCalorieMin;
    int maxCalorie, todayCalorie;
    String[] exercise = {"달리기","축구","배드민턴","골프","핸드볼","롤러스케이트","양궁","테니스","태권도","스노보드","스키","야구"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_last);
        ActionBar ac = getSupportActionBar();
        ac.hide();
        btnExerciseSel = (Button)findViewById(R.id.btnExercisesSel);
        tvExerciseCal = (TextView)findViewById(R.id.tvExerciseCal);
        tvCalorieMin = (TextView)findViewById(R.id.tvCalorieMin);
        Spinner spinLast = (Spinner)findViewById(R.id.spinLast);
        maxCaloriebar();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,exercise);
        spinLast.setAdapter(adapter);
        // 스피너의 높이 조절
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            ListPopupWindow window = (ListPopupWindow)popup.get(spinLast);
            window.setHeight(350); //pixel
        } catch (Exception e) {
            e.printStackTrace();
        }

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