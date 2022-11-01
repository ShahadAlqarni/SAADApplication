package com.hfad.SAADApplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogINPage extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText Driver_TextEmail, Driver_TextPassword;
    private static final String TAG = "LogINPage";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_inpage);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        Driver_TextEmail = findViewById(R.id.TextEmail);
        Driver_TextPassword = findViewById(R.id.TextPassword);
    }

    public void onClickSignIn(View view) {
        if (view.getId() == R.id.SignIN)
            Driver_login();
    }

    public void onClickSingUpDriver(View view) {
        Intent SingUpDriver = new Intent(this,SingUpPage.class);
        startActivity(SingUpDriver);
    }

    public void Driver_login() {
        String email = Driver_TextEmail.getText().toString().trim();
        String password = Driver_TextPassword.getText().toString().trim();

        if ((email.isEmpty())){
            Driver_TextEmail.setError("Email is reauired!");
            Driver_TextEmail.requestFocus();
            return;
        } if ((password.isEmpty())) {
            Driver_TextPassword.setError("Email is reauired!");
            Driver_TextPassword.requestFocus();
            return;
        } if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Driver_TextEmail.setError("PLease enter a valid email! ");
            Driver_TextEmail.requestFocus();
            return;
        } if(password.length()<8){
            Driver_TextPassword.setError("Min passWord length should be 8 characters!");
            Driver_TextPassword.requestFocus();
            return;
        }
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(
                task -> {
                    if (task.isSuccessful()) {
                        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user.isEmailVerified()) {
                        } else {
                            user.sendEmailVerification();
                            Log.d(TAG, "signInWithEmail:success");
                            updateUI(user);
                        }
                    } else {
                        Toast.makeText(LogINPage.this, "Email or Password Incorrect! Try agin please! ", Toast.LENGTH_LONG).show();
                    }
                });
    }
    public void updateUI(FirebaseUser currentUser) {
        Intent profileIntent = new Intent(getApplicationContext(), VerificationPhone.class);
        profileIntent.putExtra("email", currentUser.getEmail());
        profileIntent.putExtra("userID", currentUser.getUid());
        Log.v("DATA", currentUser.getUid());
        startActivity(profileIntent);
    }
}