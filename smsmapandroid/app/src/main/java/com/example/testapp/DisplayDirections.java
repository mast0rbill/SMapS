package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DisplayDirections extends AppCompatActivity {
    //LinearLayout directions =  findViewById(R.id.directions);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_directions);

        LinearLayout directions = findViewById(R.id.directions);
        String[] directionsList = MainActivity.direction.split("|");
        for(int i = 0;i < directionsList.length; i++){
            System.out.println(directionsList[i]);
            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            TextView tv=new TextView(this);
            tv.setLayoutParams(lparams);
            tv.setText(directionsList[i]);
            tv.setTextSize(20);
            directions.addView(tv);
        }
    }
}
