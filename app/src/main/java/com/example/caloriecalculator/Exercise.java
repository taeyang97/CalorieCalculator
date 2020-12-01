package com.example.caloriecalculator;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class Exercise extends AppCompatActivity {
    GridView gridView;
    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        ActionBar ac = getSupportActionBar();
        ac.hide();
        gridView = (GridView)findViewById(R.id.gridView);
        myAdapter = new MyAdapter(this);
        gridView.setAdapter(myAdapter);

    }
    public class MyAdapter extends BaseAdapter {
        int Exercises[] = {R.drawable.exerciserun, R.drawable.exercisesoc, R.drawable.exercisebad,
                R.drawable.exercisegol, R.drawable.exercisehan, R.drawable.exerciserol,
                R.drawable.exerciseyan, R.drawable.exerciseten, R.drawable.exercisetak,
                R.drawable.exercisesno, R.drawable.exerciseski, R.drawable.exercisebas};
        Context context;
        public MyAdapter(Context context) {
            this.context=context;
        }

        @Override
        public int getCount() {
            return Exercises.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView = new ImageView(context);
            Display display = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
            int x = display.getWidth();
            imageView.setLayoutParams(new ViewGroup.LayoutParams(x/3,(int)(x/4*1.5))); // 뷰 항목마다 크기 선언
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER); // 이미지를 크기안에 중간에 오게 //FIT_XY는 크기 안에 이미지를 꽉차게 하는 것
            imageView.setPadding(30,30,30,30);
            imageView.setImageResource(Exercises[position]);
            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            imageView.setPadding(15,15,15,15);
                            break;
                        case MotionEvent.ACTION_UP:
                            imageView.setPadding(30,30,30,30);
                            Intent intent = new Intent(getApplicationContext(),ExerciseLast.class);
                            startActivity(intent);
                            break;
                    }
                    return true;
                }
            });
            return imageView;
        }
    }
}