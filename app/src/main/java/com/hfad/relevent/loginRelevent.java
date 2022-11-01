package com.hfad.relevent;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.hfad.SAADApplication.R;

public class loginRelevent extends AppCompatActivity {

    EditText email, password;
    Button signINR;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_relecent);

        email = (EditText) findViewById(R.id.TextEmailR);
        password = (EditText) findViewById(R.id.TextPasswordR);
        signINR = (Button) findViewById(R.id.SignINR);

        signINR.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                checkUsername();
            }
        });

    }
    void checkUsername() {
        boolean isValid = true;
        if (isEmpty(email)) {
            email.setError("You must enter username to login!");
            isValid = false;
        } else {
            if (!isEmail(email)) {
                password.setError("Enter valid email!");
                isValid = false;
            }
        }

        if (isEmpty(password)) {
            password.setError("You must enter password to login!");
            isValid = false;
        } else {
            if (password.getText().toString().length() < 4) {
                password.setError("Password must be at least 4 chars long!");
                isValid = false;
            }
        }

        if (isValid) {
            String usernameValue = email.getText().toString();
            String passwordValue = password.getText().toString();
            if (usernameValue.equals("src@src.com") && passwordValue.equals("src123456")) {
                Intent i = new Intent(loginRelevent.this, com.hfad.relevent.red.red_MainPage.class);
                startActivity(i);
                this.finish();
            } else if (usernameValue.equals("police@police.com") && passwordValue.equals("police123456")){
                Intent ii = new Intent(loginRelevent.this, com.hfad.relevent.police.police_MainPage.class);
                startActivity(ii);
                this.finish();
            }else {
                Toast t = Toast.makeText(this, "Wrong email or password!", Toast.LENGTH_SHORT);
                t.show();
            }
        }
    }
    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }
}

