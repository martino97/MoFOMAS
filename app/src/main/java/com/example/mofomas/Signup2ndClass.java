package com.example.mofomas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class Signup2ndClass extends AppCompatActivity {
    RadioGroup radio;
    DatePicker agePicker;
    RadioButton selectedGender;
    Button submitbtn,login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup2nd_class);
        //Hooks for passing data into next activity
        radio =findViewById(R.id.radioGroup);
        agePicker = findViewById(R.id.datePicker);
        submitbtn =findViewById(R.id.loginid);


            submitbtn.setOnClickListener(new  View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!validateGender() || !validateAge()) {
                        return;
                    }
                    selectedGender = findViewById(radio.getCheckedRadioButtonId());
                    String gender = selectedGender.getText().toString();
                    int day = agePicker.getDayOfMonth();
                    int month = agePicker.getMonth();
                    int year = agePicker.getYear();
                    String dateOfBirth = day + "/" + (month + 1) + "/" + year;

                    String fullNameText = getIntent().getStringExtra("FullName");
                    String usernameText = getIntent().getStringExtra("Username");
                    String emailText = getIntent().getStringExtra("Email");
                    String passwordText = getIntent().getStringExtra("Password");

                    Intent intent = new Intent(Signup2ndClass.this, SignUp3rdClass.class);
                    intent.putExtra("FullName", fullNameText);
                    intent.putExtra("Username", usernameText);
                    intent.putExtra("Email", emailText);
                    intent.putExtra("Password", passwordText);
                    intent.putExtra("Gender", gender);
                    intent.putExtra("dateOfBirth", dateOfBirth);

                    startActivity(intent);

                }
                });

    }
    private  boolean validateGender()
    {
        if (radio.getCheckedRadioButtonId()==-1)
        {
            Toast.makeText(this, "Please select gender", Toast.LENGTH_SHORT).show();
            return false;
        }
            return true;

    }

    private boolean validateAge()
    {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int userAge = agePicker.getYear();
        int validAge = currentYear - userAge;
        //validate user age
        if(validAge < 17)
        {
            Toast.makeText(this, "You are not eligible to get service", Toast.LENGTH_SHORT).show();
            return false;
        }

            return true;
    }
}