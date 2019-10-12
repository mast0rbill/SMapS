package com.example.smaps;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationClient;
    public static String twilioNumber = "13074666606";
    public static String[] latandlong = new String[2];
    public static String direction;

    public boolean read_direction = false;

    public static MainActivity instance;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getLatLong();


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECEIVE_SMS)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECEIVE_SMS},
                        0);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }

        Button PrevDirections = findViewById(R.id.olddirection);
        PrevDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    getLayout();
                    startActivity(new Intent(MainActivity.this, DisplayDirections.class));
                }
                catch (Exception e){
                    Toast.makeText(MainActivity.this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Window window = this.getWindow();

    // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

    // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

    // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.common_google_signin_btn_text_light));


        final EditText txtMobile;
        final EditText txtMessage;
        Button btnSms;
        btnSms = (Button)findViewById(R.id.btnSend);
        txtMessage = (EditText)findViewById(R.id.msgTxt);
        btnSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    SmsManager smgr = SmsManager.getDefault();
                    String textToSend = "s/" + latandlong[0] + "/" + latandlong[1] + "/" + txtMessage.getText().toString();
                    smgr.sendTextMessage(twilioNumber,null, textToSend,null,null);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            long curTime = System.currentTimeMillis();
                            while(System.currentTimeMillis() - curTime < 20000) {
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    getLayout();
                                    startActivity(new Intent(MainActivity.this, DisplayDirections.class));
                                    read_direction = false;
                                }
                            });
                        }
                    }).run();
                }
                catch (Exception e){
                    Toast.makeText(MainActivity.this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

        txtMessage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }

    protected void getLatLong(){
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
                            latandlong[0] = Double.toString(latitude);
                            latandlong[1] = Double.toString(longtitude);
                            System.out.println("REACHED LAT" + latandlong[0]);
                        }
                    }
                });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void getLayout(){
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
//                if(msgData.contains("We could not find")){
//                    msgData = "There are no matching restaurants in range`";
//                }
                int indexofbody = msgData.indexOf("body:");
                int endofbodyindex = msgData.indexOf("locked");
                int indexaddress = msgData.indexOf("address:");
                int endindexaddress = msgData.indexOf("person:");
                if(msgData.substring(indexaddress, endindexaddress).equals("address:+13074666606 ") && !read_direction) {
                    read_direction = true;
                    direction = msgData.substring(indexofbody+43, endofbodyindex);
                }
            } while (cursor.moveToNext());
        } else {
            // empty box, no SMS
        }
        cursor.close();
    }
}
