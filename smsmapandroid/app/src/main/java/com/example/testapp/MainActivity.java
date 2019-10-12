package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.w3c.dom.Text;

import java.io.Console;

public class MainActivity extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView test = findViewById(R.id.test);
        Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);

        if (cursor.moveToFirst()) { // must check the result to prevent exception
            do {
                String msgData = "";
                for(int idx=0;idx<cursor.getColumnCount();idx++)
                {
                    msgData += " " + cursor.getColumnName(idx) + ":" + cursor.getString(idx);
                }
                //System.out.println(msgData);
                // use msgData
                int indexofbody = msgData.indexOf("body:");
                int endofbodyindex = msgData.indexOf("locked");
                int indexaddress = msgData.indexOf("address:");
                int endindexaddress = msgData.indexOf("person:");
                if(msgData.substring(indexaddress, endindexaddress).equals("address:+18053004654 ")) {
//                System.out.println(msgData.substring(indexaddress, endindexaddress));
//                if(msgData.substring(indexaddress, endindexaddress).equals("address:1511 ")) {
//                    System.out.println("REACHED");
//                    System.out.println(msgData.substring(indexaddress, endindexaddress));
//                }
                    test.setText(msgData.substring(indexofbody, endofbodyindex) + msgData.substring(indexaddress, endindexaddress));
                }
            } while (cursor.moveToNext());
        } else {
            // empty box, no SMS
        }
        final EditText txtMobile;
        final EditText txtMessage;
        Button btnSms;
        btnSms = (Button)findViewById(R.id.btnSend);
        txtMobile = (EditText)findViewById(R.id.mblTxt);
        txtMessage = (EditText)findViewById(R.id.msgTxt);
        btnSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    SmsManager smgr = SmsManager.getDefault();
                    smgr.sendTextMessage(txtMobile.getText().toString(),null, txtMessage.getText().toString(),null,null);
                    Toast.makeText(MainActivity.this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Toast.makeText(MainActivity.this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
        final TextView test2 = findViewById(R.id.test2);
        Button getLocation = findViewById(R.id.getloc);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            double latitude = location.getLatitude();
                            double longtitude = location.getLongitude();
                            System.out.println(Double.toString(latitude) + Double.toString(longtitude));
                        }
                    }
                });
    }

}
