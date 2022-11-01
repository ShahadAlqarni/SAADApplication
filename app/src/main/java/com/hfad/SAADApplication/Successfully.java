package com.hfad.SAADApplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class Successfully extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successfully);
        Button Exit = findViewById(R.id.Exit);
        Exit.setOnClickListener(view -> {
            //to close application
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(Successfully.this,startPage.class));
            System.exit(0);                finish();
            finish();

        });
    }
}