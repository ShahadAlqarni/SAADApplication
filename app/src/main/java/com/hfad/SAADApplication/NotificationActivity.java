package com.hfad.SAADApplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class NotificationActivity extends AppCompatActivity {
    private TextView timer;
    String number, id1, message, email;
    String currentLocationLatitude;
    String currentLocationLongitude;
    FirebaseAuth firebaseAuth;
    private long timeLeftInMilliseconds = 300000; // == 5 minute
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        timer = findViewById(R.id.timer);


        Intent n = getIntent();
        email = n.getStringExtra("email");
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = rootRef.child("Drivers");
        id1 = n.getStringExtra("userID");
        currentLocationLatitude = n.getStringExtra("Latitude");
        currentLocationLongitude = n.getStringExtra("Longitude");
        number = n.getStringExtra("R_number");


        firebaseAuth = FirebaseAuth.getInstance();
        Query userQuery = userRef.orderByChild("email").equalTo(email);
        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String driverName = "" + ds.child("driverFullName").getValue();
                    String bloodType = "" + ds.child("driver_BloodType").getValue();
                    String phoneNumber = "" + ds.child("driver_PhoneNumber").getValue();
                    String carColore = "" + ds.child("carColorDriver").getValue();
                    String carCompany = "" + ds.child("carCompanyDriver").getValue();
                    String carId = "" + ds.child("carIDDriver").getValue();
                    String illness = "" + ds.child("driver_Illness").getValue();

                    message = "Driver name is " + driverName + "\n driver blood Type is " + bloodType + "\n driver phone Number is " + phoneNumber
                            + "\n driver car Colore is " + carColore + "\n driver car Company is " + carCompany + "\n driver car Id is " + carId
                            + "\n driver have illness " + illness;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        new CountDownTimer(timeLeftInMilliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMilliseconds = millisUntilFinished;
                StartTimer();
            }

            @Override
            public void onFinish() {
                //when the timer finish
                startActivity( new Intent(NotificationActivity.this, Successfully.class));
                if (TextUtils.isEmpty(message)) {
                    Toast.makeText(NotificationActivity.this, "connot send", Toast.LENGTH_LONG).show();
                } else {
                    String red1 = "true", police1 = "true";
                    String uri = "http://maps.google.com/maps?saddr=" + currentLocationLatitude +","+currentLocationLongitude;
                    send_data(message, red1, police1, uri);
                }
                sendToFamily();
            }
        }.start();
    }
    ///////////////timer
    public void StartTimer() {
        int minutes = (int) timeLeftInMilliseconds / 60000;
        int seconds = (int) timeLeftInMilliseconds % 60000 / 1000;
        String timeLeftText="";
        if(minutes < 10){
            timeLeftText += "0";
        }
        timeLeftText += minutes;
        timeLeftText += ":";
        if (seconds < 10) {
            timeLeftText += "0";
        }
        timeLeftText += seconds;
        timer.setText(timeLeftText);

    }

    public void Automatic_Action(View view) {

        //+send to all

        if (TextUtils.isEmpty(message)) {
            Toast.makeText(NotificationActivity.this, "connot send", Toast.LENGTH_LONG).show();
        } else {
            String red1 = "true", police1 = "true";
            String uri = "http://maps.google.com/maps?saddr=" + currentLocationLatitude +","+currentLocationLongitude;
            send_data(message, red1, police1, uri);
        }
        sendToFamily();
        Intent intent = new Intent(this, Successfully.class);
        startActivity(intent);
    }

    public void Personal_Action(View view) {
        //go to home page
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("R_number", number);
        intent.putExtra("email", email);
        intent.putExtra("userID", id1);
        startActivity(intent);
    }

    private void send_data(String message, String red, String police, String url) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(id1, new db_send(red, message, police, id1, url));
        databaseReference.child("new_alert").updateChildren(hashMap);
        databaseReference.child("all_alert").updateChildren(hashMap);

    }

    // send to Family
    public void sendToFamily() {
        String uri = "http://maps.google.com/maps?saddr=" + currentLocationLatitude +","+currentLocationLongitude;

        SmsManager smsManager = SmsManager.getDefault();
        StringBuffer smsBody = new StringBuffer();
        smsBody.append("I had an accident please help me. \n" +
                "My location is: \n ");
        smsBody.append(Uri.parse(uri));
        smsManager.sendTextMessage("+966-"+number, null, smsBody.toString(), null, null);
    }
    public void Exit_Action(View view) {
        //to close application
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(NotificationActivity.this,startPage.class));
        System.exit(0);
        finish();
    }

}