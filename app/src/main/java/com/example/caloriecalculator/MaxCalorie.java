package com.example.caloriecalculator;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MaxCalorie extends AppCompatActivity {
    EditText etMaxCalorie1, etMaxCalorie2;
    double average;
    int maxCalorie;
    TextView tvMaxCalorie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_max_calorie);
        ActionBar ac = getSupportActionBar();
        ac.hide();
        etMaxCalorie1 = (EditText)findViewById(R.id.etMaxCalorie1);
        etMaxCalorie2 = (EditText)findViewById(R.id.etMaxCalorie2);
        Button btnMaxCalorie = (Button)findViewById(R.id.btnMaxCalorie);
        tvMaxCalorie = (TextView)findViewById(R.id.tvMaxCalorie);


        etMaxCalorie2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                average = (Double.parseDouble(etMaxCalorie1.getText().toString())-100)*0.9;
                maxCalorie = (int)(average * Integer.parseInt(etMaxCalorie2.getText().toString()));
                tvMaxCalorie.setText("나의 Max 칼로리 = "+maxCalorie);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnMaxCalorie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences preferences = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Max",tvMaxCalorie.getText().toString());
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        if((preferences != null) && (preferences.contains("Max"))){
            tvMaxCalorie.setText(preferences.getString("Max",""));
        }
    }
}