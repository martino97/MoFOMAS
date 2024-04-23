package com.example.mofomas;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;

public class Signup extends AppCompatActivity {
    //variables
    Button register, loginbtn;
    TextInputLayout fullName, username, password, email;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        //Hooks for next Screen
        register = findViewById(R.id.regid);
        loginbtn = findViewById(R.id.signinreg);

        //Hooks for passing data into next activities
        fullName = findViewById(R.id.fullnameid);
        username = findViewById(R.id.usernameid);
        email = findViewById(R.id.emailid);
        password = findViewById(R.id.passwordid);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void callNextSignupScreen(View view) {
        if(!validateFullName() | !validateUserName() | !validateEmail() | !validatePassword())
        {
            return;
        }
        Intent intent = new Intent(Signup.this, Signup2ndClass.class);
        startActivity(intent);
    }

    private boolean validateFullName() {
        String val = fullName.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            fullName.setError("Field  can not be empty");
            return false;
        } else {
            fullName.setError(null);
            fullName.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateUserName() {
        String val = username.getEditText().getText().toString().trim();
        //check white Spaces in the fields;
        String checkSpaces = "\\A\\w{1,20}\\z";
        if (val.isEmpty()) {
            username.setError("Field  can not be empty");
            return false;
        } else if (val.length() > 20) {
            username.setError("Username is too long");
            return false;
        } else if (!val.matches(checkSpaces)) {
            username.setError("No whitespaces are allowed");
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateEmail() {
        String val = email.getEditText().getText().toString().trim();
        //check white Spaces in the fields;
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (val.isEmpty()) {
            email.setError("Field  can not be empty");
            return false;
        } else if (val.length() > 20) {
            email.setError("Username is too long");
            return false;
        } else if (!val.matches(checkEmail)) {
            email.setError("Invalid email address");
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword() {
        String val = password.getEditText().getText().toString().trim();
        //check white Spaces in the fields;
        String checkPassword = "^" +
                "(?=.*[a-zA-Z])" +  //any letter
                "(?=.*[@#$%^&+=])" + //atleast one special character
                "(?=\\S+$)" +  //no white spaces
                ".{4,}" +      //Atleast four character
                "$";
        if (val.isEmpty()) {
            password.setError("Field  can not be empty");
            return false;
        } else if (!val.matches(checkPassword)) {
            password.setError("Password should contain four characters");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }
}