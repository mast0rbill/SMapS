package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class selection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        LinearLayout directions = findViewById(R.id.linlayout);
        final String[] directionsList = MainActivity.direction.split("`");
        for(int i = 0;i < directionsList.length-1; i++){
            System.out.println(directionsList[i]);
            LinearLayout layout = new LinearLayout(selection.this);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            Button bt = new Button(this);
            bt.setLayoutParams(lparams);
            final String text = directionsList[i];
            bt.setText(directionsList[i]);
            bt.setTextSize(20);
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        SmsManager smgr = SmsManager.getDefault();
                        String textToSend = MainActivity.latandlong[0] + "/" + MainActivity.latandlong[1] + "/" + text;
                        smgr.sendTextMessage(MainActivity.twilioNumber,null, textToSend,null,null);
                        //SystemClock.sleep(7000);
                        startActivity(new Intent(selection.this, DisplayDirections.class));
                    }
                    catch (Exception e){
                        Toast.makeText(selection.this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            layout.addView(bt);
            directions.addView(layout);
        }

    }
}
