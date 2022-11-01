package com.hfad.SAADApplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;

import com.hfad.relevent.loginRelevent;

public class startPage extends Activity {
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);
        //Get a reference to the Spinner
        Spinner spinner = (Spinner) findViewById(R.id.UserType);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,R.array.UserTypeSpinner, R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    //Called when the Next button gets clicked
    public void onClickFindUserType(View view) {
        spinner = (Spinner) findViewById(R.id.UserType);
        //Get the selected user
        String userTypeSelected = String.valueOf(spinner.getSelectedItem());
        if (userTypeSelected.equals("Driver")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(startPage.this);
            builder.setTitle("Terms and Conditions for use (SAAD): ");
            builder.setMessage("1- If you want to change the email and password, you must contact customer service via the About page. \n"  +
                    "\n 2- When the alert appears, you must interact with it and decide who to send it to," +
                    "Otherwise when the five minutes are finished, it will be sent directly to the relevant authorities" +
                    "(Traffic Accident Police, Saudi Red Crescent) and send your location to your relative's number. \n"+
                    "\n 3- You agree to send your information and location to relevant authorities (Traffic Accident Police, Saudi Red Crescent)\n"+
                    "\n 4- You agree to send your location to your relative's number");
            builder.setPositiveButton("Yes I agree!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent DriverPage = new Intent(startPage.this,LogINPage.class);
                    startActivity(DriverPage);
                }
            });
            builder.create().show();
        } else if (userTypeSelected.equals("Relevant authorities")) {
            Intent releventPage = new Intent(this, loginRelevent.class);
            startActivity(releventPage);
        }
    }

}