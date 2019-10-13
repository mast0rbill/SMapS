//package com.example.smaps;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.Bundle;
//import android.telephony.SmsManager;
//import android.view.View;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.Toast;
//
//public class selection extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_selection);
//        LinearLayout directions = findViewById(R.id.linlayout);
//        final String[] directionsList = MainActivity.direction.split("`");
//        Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
//
//        //MainActivity.read_direction = false;
//
//        if (cursor.moveToFirst()) { // must check the result to prevent exception
//            do {
//                String msgData = "";
//                for(int idx=0;idx<cursor.getColumnCount();idx++)
//                {
//                    msgData += " " + cursor.getColumnName(idx) + ":" + cursor.getString(idx);
//                }
//                //System.out.println(msgData);
//                // use msgData
//                int indexofbody = msgData.indexOf("body:");
//                int endofbodyindex = msgData.indexOf("locked");
//                int indexaddress = msgData.indexOf("address:");
//                int endindexaddress = msgData.indexOf("person:");
//                //if(msgData.substring(indexaddress, endindexaddress).equals("address:+13074666606 ") && !MainActivity.read_direction) {
//                    //MainActivity.read_direction = true;
//                    MainActivity.direction = msgData.substring(indexofbody+43, endofbodyindex);
//                //}
//            } while (cursor.moveToNext());
//        } else {
//            // empty box, no SMS
//        }
//        for(int i = 0;i < directionsList.length-1; i++){
//            System.out.println(directionsList[i]);
//            LinearLayout layout = new LinearLayout(selection.this);
//            layout.setOrientation(LinearLayout.HORIZONTAL);
//            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//
//            Button bt = new Button(this);
//            bt.setLayoutParams(lparams);
//            final String text = directionsList[i];
//            bt.setText(directionsList[i]);
//            bt.setTextSize(20);
//            bt.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    try{
//                        SmsManager smgr = SmsManager.getDefault();
//                        String textToSend = "d/" + MainActivity.latandlong[0] + "/" + MainActivity.latandlong[1] + "/" + text;
//                        smgr.sendTextMessage(MainActivity.twilioNumber,null, textToSend,null,null);
//
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                long curTime = System.currentTimeMillis();
//                                while(System.currentTimeMillis() - curTime < 20000) {
//                                }
//
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        MainActivity.instance.getLayout();
//                                        startActivity(new Intent(selection.this, DisplayDirections.class));
//                                        MainActivity.instance.read_direction = false;
//                                    }
//                                });
//                            }
//                        }).run();
//                    }
//                    catch (Exception e){
//                        Toast.makeText(selection.this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//            layout.addView(bt);
//            directions.addView(layout);
//        }
//
//    }
//}
