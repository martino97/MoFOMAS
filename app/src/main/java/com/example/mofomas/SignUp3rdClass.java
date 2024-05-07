package com.example.mofomas;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hbb20.CountryCodePicker;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp3rdClass extends AppCompatActivity {



    private Button verify;
    private TextInputLayout phoneNumber;
    private TextInputEditText verificationEditText;
    private CountryCodePicker ccp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up3rd_class);

        String _fullName = getIntent().getStringExtra("fullName");
        String _email = getIntent().getStringExtra("email");
        String _username = getIntent().getStringExtra("username");
        String _password = getIntent().getStringExtra("password");
        String _date = getIntent().getStringExtra("dateOfBirth");
        String _gender = getIntent().getStringExtra("gender");

        // Initialize views
        verify = findViewById(R.id.verificationId);
        ccp = findViewById(R.id.countryCode);
        verificationEditText = findViewById(R.id.verification);
        phoneNumber = findViewById(R.id.signUpNumber);

        // Set click listener for verify button
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validatePhoneNumber()) {
                    // Validate phone number and start VerifyOTP activity
                    String fullPhoneNumber = "+" + ccp.getSelectedCountryCode() + phoneNumber.getEditText().getText().toString().trim();
                    Intent intent = new Intent(SignUp3rdClass.this, VerifyOTP.class);


                    intent.putExtra("phoneNumber", fullPhoneNumber);
                    intent.putExtra("fullName",_fullName);
                    intent.putExtra("username",_username);
                    intent.putExtra("password",_password);
                    intent.putExtra("email",_email);
                    intent.putExtra("dateOfBirth",_date);
                    intent.putExtra("gender",_gender);
                    startActivity(intent);
                }
            }

        });
    }

    public boolean validatePhoneNumber() {
        String countryCodePicker = ccp.getSelectedCountryCode();
        String fullPhoneNumber = "+" + countryCodePicker + phoneNumber.getEditText().getText().toString().trim();
        String regex = "^[+][0-9\\-()/.\\s?]{6,15}[0-9]$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(fullPhoneNumber);
        return matcher.matches();
    }
}