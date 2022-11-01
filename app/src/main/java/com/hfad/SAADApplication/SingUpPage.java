package com.hfad.SAADApplication;


import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Objects;

public class SingUpPage extends AppCompatActivity {

    // Initialize Firebase Auth
    //Declare an instance of FirebaseAuth
    private FirebaseAuth mAuth;

    EditText fullName, email, PhoneNumber, NationalID, RelativeNumber,password, ConfirmPassword;
    EditText typeOfchronicIllness,CarID,CarCompany,CarColor;
     String Driver_Blood, Driver_Disabled ,Driver_Illness ;
    int selectedId1 , selectedId2 , selectedId3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up_page);

        ImageButton back = (ImageButton)findViewById(R.id.backToMain);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        //driver data
        fullName = findViewById(R.id.FullName);
        email = findViewById(R.id.Driver_Email);
        PhoneNumber = findViewById(R.id.Driver_Phone);
        NationalID = findViewById(R.id.NationalID);
        RelativeNumber = findViewById(R.id.RelativeNumber);
        password = findViewById(R.id.Driver_Pass);
        ConfirmPassword = findViewById(R.id.Driver_ConfirmPass);
        CarID = findViewById(R.id.CarID);
        CarCompany = findViewById(R.id.CarCompany);
        CarColor = findViewById(R.id.CarColor);
        typeOfchronicIllness= findViewById(R.id.chronicIllnessKind);
    }

    //to open create account page
    public void onClickSingUpNext (View view){
        if (view.getId() == R.id.CreateAccount) {// get selected radio button from radioGroup
            Driver_Register();
        }
    }

    public void Driver_Register() {

        String Driver_name = fullName.getText().toString().trim();
        String Driver_Email = email.getText().toString().trim();
        String Driver_Mobile = PhoneNumber.getText().toString().trim();
        String Driver_ID = NationalID.getText().toString().trim();
        String Driver_RelativeNumber = RelativeNumber.getText().toString().trim();
        String Driver_Pass = password.getText().toString().trim();
        String Driver_ConfirmPass = ConfirmPassword.getText().toString().trim();

        //find text selected from driver
        String illness_Kind = typeOfchronicIllness.getText().toString().trim();
        String Driver_CarID = CarID.getText().toString().trim();
        String Driver_CarCompany = CarCompany.getText().toString().trim();
        String Driver_CarColor = CarColor.getText().toString().trim();

        if (((RadioGroup) findViewById(R.id.BloodTypeGroups)).getCheckedRadioButtonId() == -1)
        {   // no radio buttons are checked
            Toast.makeText(SingUpPage.this, "The blood type must be determined! ", Toast.LENGTH_LONG).show();
        } else {
            // one of the radio buttons is checked
            selectedId1 = ((RadioGroup) findViewById(R.id.BloodTypeGroups)).getCheckedRadioButtonId();
            Driver_Blood = ((RadioButton) findViewById(selectedId1)).getText().toString().trim();
        }

        if (((RadioGroup) findViewById(R.id.DisabledsGroups)).getCheckedRadioButtonId() == -1)
        {   // no radio buttons are checked
            Toast.makeText(SingUpPage.this, "Correct button must be selected! ", Toast.LENGTH_LONG).show();
        } else {
            // one of the radio buttons is checked
            selectedId2 = ((RadioGroup) findViewById(R.id.DisabledsGroups)).getCheckedRadioButtonId();
            Driver_Disabled = ((RadioButton) findViewById(selectedId2)).getText().toString().trim();
        }

        if (((RadioGroup) findViewById(R.id.chronicIllnessGroups)).getCheckedRadioButtonId() == -1)
        { // no radio buttons are checked
            Toast.makeText(SingUpPage.this, "Correct button must be selected! ", Toast.LENGTH_LONG).show();
        } else {
            // one of the radio buttons is checked
            selectedId3 = ((RadioGroup) findViewById(R.id.chronicIllnessGroups)).getCheckedRadioButtonId();
            Driver_Illness = ((RadioButton)findViewById(selectedId3)).getText().toString().trim();
        }

        if ((Driver_name.isEmpty() && !Driver_name.matches("[a-zA-Z]+"))){
            fullName.setError("fullName is required!");
            fullName.requestFocus();
            return;
        } if ((Driver_Email.isEmpty())&&!Driver_Email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")){
            email.setError("Email is required!");
            email.requestFocus();
            return;
        }  if ((Driver_Mobile.isEmpty())&&!(Driver_Mobile.length()==9)){
            PhoneNumber.setError("mobile Number is required! and with out 0!");
            PhoneNumber.requestFocus();
            return;
        } if ((Driver_ID.isEmpty())&&!(Driver_ID.length()==10)){
            NationalID.setError("ID is required! and should be 10 characters");
            NationalID.requestFocus();
            return;
        } if ((Driver_RelativeNumber.isEmpty()&&!(Driver_RelativeNumber.length()==9))){
            RelativeNumber.setError("Relative Number is required! and with out 0!");
            RelativeNumber.requestFocus();
            return;
        } if ((Driver_Pass.isEmpty())){
            password.setError("PassWord is required!");
            password.requestFocus();
            return;
        } if ((Driver_ConfirmPass.isEmpty())){
            ConfirmPassword.setError("Confirm Password is required!");
            ConfirmPassword.requestFocus();
            return;
        }
        if (!Driver_Pass.matches(Driver_ConfirmPass)){
            ConfirmPassword.setError("Confirm Password is not equal!");
            ConfirmPassword.requestFocus();
            return;
        }
        if ((illness_Kind.matches("Yes")) && illness_Kind.isEmpty()){
            typeOfchronicIllness.setError("chronic illness is required!");
            typeOfchronicIllness.requestFocus();
            return;
        }
        if ((Driver_CarID.isEmpty())){
            CarID.setError("Car ID Number is required!");
            CarID.requestFocus();
            return;
        } if ((Driver_CarCompany.isEmpty())){
            CarCompany.setError("Car Company is required!");
            CarCompany.requestFocus();
            return;
        }
        if ((Driver_CarColor.isEmpty())){
            CarColor.setError("Car Color Number is required!");
            CarColor.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(Driver_Email).matches()){
            email.setError("PLease Provide valid email! ");
            email.requestFocus();
            return;
        } if(Driver_Pass.length()<8){
            password.setError("Min passWord length should be 8 characters!");
            password.requestFocus();
            return;
        }
        mAuth.createUserWithEmailAndPassword(Driver_Email,Driver_Pass).addOnCompleteListener(
                task -> {
                    if (task.isSuccessful()) {
                            Driver Driver_Accounts = new Driver(Driver_name, Driver_Email, Driver_Mobile, Driver_ID,
                                    Driver_RelativeNumber, Driver_Pass, Driver_Blood, Driver_Disabled, illness_Kind,
                                    Driver_CarID, Driver_CarCompany, Driver_CarColor);
                            FirebaseDatabase.getInstance().getReference("Drivers")
                                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                    .setValue(Driver_Accounts).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    Toast.makeText(SingUpPage.this, "User has been registered! ", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(SingUpPage.this, LogINPage.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(SingUpPage.this, "Falied to register! Try again! ", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
        );
    }
}
