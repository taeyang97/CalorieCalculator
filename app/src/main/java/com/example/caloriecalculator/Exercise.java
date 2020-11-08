package com.example.caloriecalculator;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class Exercise extends AppCompatActivity {
    ImageView Exercise[] = new ImageView[12];
    int Exercises[] = {R.id.ivExerciseRun, R.id.ivExerciseSoc, R.id.ivExerciseBad,
            R.id.ivExerciseGol, R.id.ivExerciseHan, R.id.ivExerciseRol,
            R.id.ivExerciseYan, R.id.ivExerciseTen, R.id.ivExerciseTak,
            R.id.ivExerciseSno, R.id.ivExerciseSki, R.id.ivExerciseBas};
    Point p;
    int x,y;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        ActionBar ac = getSupportActionBar();
        ac.hide();
        p =getScreenSize(this);
        x=0;
        y=0;

        for(int i=0; i<Exercise.length; i++){
             Exercise[i] = (ImageView)findViewById(Exercises[i]);
            int index;
            index = i;
            Exercise[index].setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            x=Exercise[index].getWidth()+20;
                            y=Exercise[index].getHeight()+20;
                            Exercise[index].getLayoutParams().width=x;
                            Exercise[index].getLayoutParams().height=y;
                            Exercise[index].requestLayout();
                            break;
                        case MotionEvent.ACTION_UP:
                            x=-20;
                            y=-20;
                            Exercise[index].getLayoutParams().width=x;
                            Exercise[index].getLayoutParams().height=y;
                            Exercise[index].requestLayout();
                            Intent intent = new Intent(getApplicationContext(),ExerciseLast.class);
                            startActivity(intent);
                            break;
                    }
                    return true;
                }
            });
        }
    }

    public Point getScreenSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return  size;
    }
}