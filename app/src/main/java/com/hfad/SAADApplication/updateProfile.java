package com.hfad.SAADApplication;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class updateProfile extends AppCompatActivity {
    EditText name, PhoneNumberProfile, RelativeNumberProfile, chronicIllnessUpdate,
            CarID, CarCompany, CarColorProfile;
    int selectedId1 , selectedId2;
    Button bt;
    Map<String, Object> DriverUpdated;
    String id, email,driverDisabled,driverIllness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        Intent intent3 = getIntent();
        email = intent3.getStringExtra("email");
        id = intent3.getStringExtra("userID");
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = rootRef.child("Drivers");

        Query userQuery = userRef.orderByChild("email").equalTo(email);
        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    bt = findViewById(R.id.EditeAccount);
                    bt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            name = (EditText) findViewById(R.id.EditeDriver_Name);
                            PhoneNumberProfile = (EditText) findViewById(R.id.EditeDriver_Phone);
                            RelativeNumberProfile = (EditText) findViewById(R.id.EditeRelativeNumber);

                            selectedId1 = ((RadioGroup) findViewById(R.id.EditeDisabledsGroups)).getCheckedRadioButtonId();
                            selectedId2 = ((RadioGroup) findViewById(R.id.EditechronicIllnessGroups)).getCheckedRadioButtonId();
                            driverDisabled = ((RadioButton)findViewById(selectedId1)).getText().toString().trim();
                            driverIllness = ((RadioButton)findViewById(selectedId2)).getText().toString().trim();

                            chronicIllnessUpdate= (EditText) findViewById(R.id.EditechronicIllnessKind);
                            CarID = (EditText) findViewById(R.id.EditeCarID);
                            CarCompany = (EditText) findViewById(R.id.EditeCarCompany);
                            CarColorProfile = (EditText) findViewById(R.id.EditeCarColor);

                            DriverUpdated = new HashMap<>();
                            if (!name.getText().toString().equals(null))
                                DriverUpdated.put(("driverFullName"), name.getText().toString().trim());
                            if (!PhoneNumberProfile.getText().toString().equals(null))
                                DriverUpdated.put(("driver_PhoneNumber"), PhoneNumberProfile.getText().toString().trim());
                            if (!RelativeNumberProfile.getText().toString().equals(null))
                                DriverUpdated.put(("relativeNumberDB"), RelativeNumberProfile.getText().toString().trim());
                            if (!driverDisabled.equals(null))
                                DriverUpdated.put(("disabled"), driverDisabled);
                            if ((driverIllness.matches("Yes")) && driverIllness.isEmpty()){
                                chronicIllnessUpdate.setError("chronic illness is required!");
                                chronicIllnessUpdate.requestFocus();
                                DriverUpdated.put(("driver_Illness"), chronicIllnessUpdate.getText().toString().trim());
                                return;
                            }
                            if (!CarID.getText().toString().equals(null))
                                DriverUpdated.put(("carIDDriver"), CarID.getText().toString().trim());
                            if (!CarCompany.getText().toString().equals(null))
                                DriverUpdated.put(("carCompanyDriver"), CarCompany.getText().toString().trim());
                            if (!CarColorProfile.getText().toString().equals(null))
                                DriverUpdated.put(("carColorDriver"), CarColorProfile.getText().toString().trim());

                            FirebaseDatabase.getInstance().getReference().child("Drivers").child(id).updateChildren(DriverUpdated).
                                    addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getApplicationContext(), "Updated Successfully", Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(), "Could not update", Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        ImageButton back = (ImageButton) findViewById(R.id.backToMain);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                onBackPressed();
            }
        });
    }
}