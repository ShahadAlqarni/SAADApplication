package com.hfad.relevent.red;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hfad.relevent.driver;
import com.hfad.SAADApplication.R;
import java.util.ArrayList;
import java.util.List;

public class red_NewAccident extends AppCompatActivity {
    RecyclerView recycler_view;
    red_driverinfo adapter;
    List<driver> driverList;
    String message, location, id;
    String red;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_accident);


        recycler_view = findViewById(R.id.new_recycler_view);

        driverList = new ArrayList<>();


        reference = FirebaseDatabase.getInstance().getReference().child("new_alert");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    red = "" + ds.child("s_red").getValue(String.class);
                    if(red.equals("true")) {
                    message = "" + ds.child("message").getValue(String.class);
                    //link of location
                    location = "" + ds.child("url").getValue(String.class);
                    //location = "Location";
                    id = "" + ds.child("sender").getValue(String.class);
                    driverList.add(new driver(location, message, id));

                    }

                }
                recycler_view.setHasFixedSize(true);
                recycler_view.setLayoutManager(new LinearLayoutManager(red_NewAccident.this));
                adapter = new red_driverinfo(red_NewAccident.this, driverList);
                recycler_view.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
