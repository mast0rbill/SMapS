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

        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView tv=new TextView(this);
        tv.setLayoutParams(lparams);
        tv.setText("test");
        directions.addView(tv);
    }
}
