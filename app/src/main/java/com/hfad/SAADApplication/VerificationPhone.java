package com.hfad.SAADApplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class VerificationPhone extends AppCompatActivity {
    EditText otp;
    Button send;
    Button check;
    String r_number;
    private String verificationId,email,id;
    private final String TAG = this.getClass().getName().toUpperCase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_phone);

        Intent intent3 = getIntent();
        email = intent3.getStringExtra("email");
        id = intent3.getStringExtra("userID");
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = rootRef.child("Drivers");
        Log.v("USERID", userRef.getKey());

        //###########################
        send = findViewById(R.id.send);
        check = findViewById(R.id.check);
        otp = findViewById(R.id.otp);
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        //###########################

        userRef.addValueEventListener(new ValueEventListener() {
            String phone;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot keyId : dataSnapshot.getChildren()) {
                    if ((keyId.child("driver_email").getValue()) != null && keyId.child("driver_email").getValue().equals(email)) {
                        phone = keyId.child("driver_PhoneNumber").getValue(String.class);
                        r_number = keyId.child("relativeNumberDB").getValue(String.class);
                        break;
                    }
                }


                send.setOnClickListener(view -> {
                    progressBar.setVisibility(View.VISIBLE);
                    send.setVisibility(View.INVISIBLE);
                    PhoneAuthProvider.getInstance().verifyPhoneNumber("+966-"+phone, 60,
                            TimeUnit.SECONDS,
                            VerificationPhone.this,
                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                    progressBar.setVisibility(View.GONE);
                                    send.setVisibility(View.VISIBLE);

                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    progressBar.setVisibility(View.GONE);
                                    send.setVisibility(View.VISIBLE);
                                    Toast.makeText(VerificationPhone.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    progressBar.setVisibility(View.GONE);
                                    send.setVisibility(View.VISIBLE);
                                    Intent sendCode = new Intent(getApplicationContext(), VerificationPhone.class);
                                    sendCode.putExtra("verificationId", verificationId);
                                    sendCode.putExtra("email", email);
                                    sendCode.putExtra("userID", id);
                                    sendCode.putExtra("r_number", r_number);
                                    // Log.v("DATA",id);
                                    startActivity(sendCode);
                                    Toast.makeText(VerificationPhone.this, "the code is send", Toast.LENGTH_SHORT).show();
                                }
                            });
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        final ProgressBar progressBar2 = findViewById(R.id.progressBar2);
        verificationId = getIntent().getStringExtra("verificationId");

        check.setOnClickListener(view -> {
            if (otp.getText().toString().trim().isEmpty()) {
                Toast.makeText(VerificationPhone.this, "please enter valid code", Toast.LENGTH_SHORT).show();
                return;
            }

            String code = otp.getText().toString();
            if (verificationId != null) {
                progressBar2.setVisibility(View.VISIBLE);
                check.setVisibility(View.INVISIBLE);
                PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                        verificationId, code);
                FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                        .addOnCompleteListener(task -> {
                            progressBar2.setVisibility(View.GONE);
                            check.setVisibility(View.VISIBLE);
                            if (task.isSuccessful()) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(VerificationPhone.this);
                                builder.setTitle("CAUTION AND UNDERSTANDING OF USE (SAAD): ");
                                builder.setMessage("\n 1- When you choose the automatic inform, the alert will be sent directly to the police, ambulance and your relative whose number you entered previously. \n"  +
                                        "\n 2- When you choose a personal Inform, you have the choice of who to send the alert to. \n" );
                                builder.setPositiveButton("Yes I understanding!", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent opeanMainPage = new Intent(getApplicationContext(), MainActivity.class);
                                        opeanMainPage.putExtra("email", email);
                                        opeanMainPage.putExtra("userID", id);
                                        opeanMainPage.putExtra("r_number", r_number);
                                        Log.v("DATA",id);
                                        startActivity(opeanMainPage);
                                    }
                                });
                                builder.create().show();
                            } else {
                                Toast.makeText(VerificationPhone.this, "the verification code entered was invalidr", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
}
}