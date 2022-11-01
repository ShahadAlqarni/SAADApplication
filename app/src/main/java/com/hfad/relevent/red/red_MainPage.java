package com.hfad.relevent.red;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.hfad.SAADApplication.R;

public class red_MainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.red_main_page);

    }

    public void onClickall(View view){
        Intent intent = new Intent(red_MainPage.this, red_AllAccident.class);
        startActivity(intent);
    }

    public void onClicknew(View view){
        Intent intent = new Intent(red_MainPage.this, red_NewAccident.class);
        startActivity(intent);
    }
}
