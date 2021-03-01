package com.example.caloriecalculator;


import android.content.Intent;
import android.os.Bundle;

import com.example.caloriecalculator.option.StatusActivity;

public class LogoPage extends StatusActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Thread.sleep(3000); // 3초 후 화면전환
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(getApplicationContext(), SettingPage.class);
        startActivity(intent);
        finish();
    }
}