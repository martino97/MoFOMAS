package com.example.mofomas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class ForgetPassword extends AppCompatActivity {


    private Button verify;
    private TextInputLayout phoneNumber;
    private TextInputEditText verificationEditText;
    private CountryCodePicker ccp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

      /*  String fullNameText = getIntent().getStringExtra("FullName");
        String emailText = getIntent().getStringExtra("Email");
        String usernameText = getIntent().getStringExtra("Username");
        String passwordText = getIntent().getStringExtra("Password");
        String dateOfBirth = getIntent().getStringExtra("dateOfBirth");
        String gender = getIntent().getStringExtra("Gender");*/

        // Initialize views
        verify = findViewById(R.id.sendRequest);
        ccp = findViewById(R.id.countryCode);
        phoneNumber = findViewById(R.id.forgetNumber);

        // Set click listener for verify button
        verify.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (validatePhoneNumber()) {
                    // Validate phone number and start VerifyOTP activity
                    String fullPhoneNumber = "+" + ccp.getSelectedCountryCode() + Objects.requireNonNull(phoneNumber.getEditText()).getText().toString().trim();
                    Intent intent = new Intent(ForgetPassword.this,VerifyOTP.class);



                   /* intent.putExtra("FullName",fullNameText);
                    intent.putExtra("Username",usernameText);
                    intent.putExtra("Email",emailText);
                    intent.putExtra("Password",passwordText);
                    intent.putExtra("dateOfBirth",dateOfBirth);
                    intent.putExtra("Gender",gender);
                    intent.putExtra("phoneNumber", fullPhoneNumber);*/
                    startActivity(intent);
                    finish();
                }
            }

        });
    }

    public boolean validatePhoneNumber() {
        String countryCodePicker = ccp.getSelectedCountryCode();
        String fullPhoneNumber = "+" + countryCodePicker + Objects.requireNonNull(phoneNumber.getEditText()).getText().toString().trim();
        String regex = "^[+][0-9\\-()/.\\s?]{6,15}[0-9]$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(fullPhoneNumber);
        return matcher.matches();

    }
}