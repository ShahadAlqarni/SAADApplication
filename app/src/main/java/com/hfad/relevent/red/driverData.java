package com.hfad.relevent.red;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hfad.SAADApplication.R;
import com.hfad.SAADApplication.db_send;
//import com.hfad.relevent.db_send;
import java.util.ArrayList;

public class driverData extends Activity {
    String data1;
    TextView data;
    FirebaseAuth firebaseAuth;
    String myUid;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference userDBref;
    String message;
    ArrayList<db_send> d_data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_data);

        Intent intent = getIntent();
        message = intent.getStringExtra("massege");
        data = findViewById(R.id.data);
        data.setText(message);


    }
}
