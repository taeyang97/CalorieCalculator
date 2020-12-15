package com.example.caloriecalculator;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import java.lang.reflect.Field;

public class ExerciseLast extends AppCompatActivity {
    Button btnExerciseSel;
    TextView tvExerciseCal, tvCalorieMin;
    WebView webView;
    int maxCalorie, todayCalorie;
    String[] exercise = {"걷기","계단","등산","수영","요가","복싱","줄넘기","자전거","달리기","스쿼트","사이클","스쿼시",
                    "훌라후프","런닝머신","에어로빅","윗몸일으키기"};
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
        btnExerciseSel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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