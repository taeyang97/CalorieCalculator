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

import static com.example.caloriecalculator.DataBaseHelper.DB_PATH;

public class MaxCalorie extends AppCompatActivity {
    double average;
    int maxCalorie;
    TextView tvMaxCalorie;
    DataBaseHelper dataBaseHelper;
    SQLiteDatabase sqlDB;
    String[] select = {"", String.valueOf(25), String.valueOf(30), String.valueOf(35), String.valueOf(40)};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_max_calorie);
        ActionBar ac = getSupportActionBar();
        ac.hide();
        /*File folder = new File(DB_PATH);
        if (folder.exists()) {
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }*/
        dataBaseHelper = new DataBaseHelper(this);
        EditText etMaxCalorie = (EditText)findViewById(R.id.etMaxCalorie);
        Spinner spinMaxCalorie = (Spinner) findViewById(R.id.spinMaxCalorie);
        Button btnMaxCalorie = (Button)findViewById(R.id.btnMaxCalorie);
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
                    }
                    if (!TextUtils.isEmpty(etMaxCalorie.getText().toString())) {
                        tvMaxCalorie.setText("나의 Max 칼로리 = " + maxCalorie);
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
        /*etMaxCalorie2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                average = (Double.parseDouble(etMaxCalorie.getText().toString())-100)*0.9;
                maxCalorie = (int)(average * Integer.parseInt(etMaxCalorie2.getText().toString()));
                tvMaxCalorie.setText("나의 Max 칼로리 = "+maxCalorie);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/

        btnMaxCalorie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlDB = dataBaseHelper.getWritableDatabase();
                sqlDB.execSQL("INSERT INTO maxCalorie VALUES(" + maxCalorie + ");");
                sqlDB.close();
                Toast.makeText(getApplicationContext(),"max값을 정하였습니다.",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }

}