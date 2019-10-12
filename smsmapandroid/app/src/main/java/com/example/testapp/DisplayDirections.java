package com.example.testapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DisplayDirections extends AppCompatActivity {
    //LinearLayout directions =  findViewById(R.id.directions);
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_directions);

        LinearLayout directions = findViewById(R.id.directions);
        String[] directionsList = MainActivity.direction.split("`");
        for(int i = 0;i < directionsList.length; i++){
            System.out.println(directionsList[i]);
            LinearLayout layout = new LinearLayout(DisplayDirections.this);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            final ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(160, 160)); // value is in pixels
            imageView.setImageResource(R.drawable.rightarrow);
            layout.addView(imageView);
            
            TextView tv=new TextView(this);
            tv.setLayoutParams(lparams);
            tv.setText(directionsList[i]);
            tv.setTextSize(20);
            layout.addView(tv);

            directions.addView(layout);

        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }
}
