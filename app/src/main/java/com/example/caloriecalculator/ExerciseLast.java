package com.example.caloriecalculator;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListPopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.Calendar;

public class ExerciseLast extends AppCompatActivity {
    Button btnExerciseSel;
    TextView tvExerciseCal, tvCalorieMin;
    WebView webView;
    int maxCalorie, todayCalorie;
    String[] exercise = {"걷기","계단","등산","수영","요가","복싱","줄넘기","자전거","달리기","스쿼트","사이클","스쿼시",
                    "훌라후프","런닝머신","에어로빅","윗몸일으키기"};
    String date,today,max;
    int _id,cYear, cMonth, cDay;

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
        webview();
        maxCaloriebar();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,exercise);
        spinLast.setPrompt("어떤 운동을 하시나요?");
        spinLast.setAdapter(adapter);

        // 스피너 선택 시 메소드
        spinLast.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String search = exercise[position];
                webView.loadUrl("https://search.naver.com/search.naver?where=nexearch&sm=tab_etc&mra=bkc3&query=" +
                        exercise[position] + "칼로리");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 저장하는 메소드
        btnExerciseSel.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {

                Calendar cal = Calendar.getInstance(); // 핸드폰의 날짜와 시간을 가져와 시간을 넣어준다.
                cYear = cal.get(Calendar.YEAR);
                cMonth = cal.get(Calendar.MONTH);
                cDay = cal.get(Calendar.DAY_OF_MONTH); // 그 달의 일수

                date = cYear + "-" + (cMonth + 1) + "-" + cDay;
                today = String.valueOf(todayCalorie);
                max = String.valueOf(maxCalorie);

                DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext());
                boolean isInserted = dataBaseHelper.insertData(date,today,max);
                if(isInserted == true){
                    Toast.makeText(getApplicationContext(),"저장되었습니다.",Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),"다시 저장해주세요",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    // 칼로리량을 받아온 메소드
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
    // 웹사이트 호출해주는 메소드
    public void webview(){
        webView = (WebView)findViewById(R.id.webView);
        webView.setWebViewClient(new MyWebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setBuiltInZoomControls(true);
        webView.getSettings().setJavaScriptEnabled(true);
    }

    class MyWebViewClient extends WebViewClient { // 내부클래스 생성
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) { // String에게 전달받아 Webview에 전달한다.
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

}