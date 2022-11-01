package com.hfad.relevent.police;

import android.os.Bundle;
import android.widget.Button;

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
import com.hfad.relevent.red.red_AllAccident;
import com.hfad.relevent.red.red_AllDriverinfo;

import java.util.ArrayList;
import java.util.List;

public class police_AllAccident extends AppCompatActivity {
    RecyclerView recycler_view2;
    police_AllDriverinfo adapter2;
    List<driver> driverList;
    String message, location, id;
    String red;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_accident);

        recycler_view2 = findViewById(R.id.old_recycler_view);
        driverList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference().child("all_alert");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    red = "" + ds.child("police").getValue(String.class);
                    if(red.equals("true")) {
                        message = "" + ds.child("message").getValue(String.class);
                        location = "" + ds.child("url").getValue(String.class);
                        id = "" + ds.child("sender").getValue(String.class);
                        driverList.add(new driver(location, message, id));

                    }

                }
                recycler_view2.setHasFixedSize(true);
                recycler_view2.setLayoutManager(new LinearLayoutManager(police_AllAccident.this));
                adapter2 = new police_AllDriverinfo(police_AllAccident.this, driverList);
                recycler_view2.setAdapter(adapter2);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}
