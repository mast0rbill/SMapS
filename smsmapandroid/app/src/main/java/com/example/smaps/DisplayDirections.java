package com.example.smaps;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
        System.out.println(directionsList.length);
        if(directionsList.length == 1){
            System.out.println("REACHED");
            LinearLayout layout = new LinearLayout(DisplayDirections.this);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            TextView tv=new TextView(this);
            tv.setLayoutParams(lparams);
            tv.setText("There were no matching places in range");
            tv.setTextSize(20);
            layout.addView(tv);
            directions.addView(layout);
        }
        for(int i = 0;i < directionsList.length-1; i++){
            System.out.println(directionsList[i]);
            LinearLayout layout = new LinearLayout(DisplayDirections.this);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            final ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(160, 160)); // value is in pixels
            int turnIndex = directionsList[i].indexOf("turn");
            if(directionsList[i].substring(turnIndex+5, turnIndex+9).equals("left")) {
                imageView.setImageResource(R.drawable.leftarrow);
                layout.addView(imageView);
            }
            else if(directionsList[i].substring(turnIndex+5, turnIndex+10).equals("right")){
                imageView.setImageResource(R.drawable.rightarrow);
                layout.addView(imageView);
            }


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
