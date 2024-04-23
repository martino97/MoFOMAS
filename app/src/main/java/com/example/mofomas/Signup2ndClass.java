package com.example.mofomas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup2nd_class);
        //Hooks for passing data into next activity
        radio =findViewById(R.id.radioGroup);
        agePicker = findViewById(R.id.datePicker);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void callToLoginPage(View view)
    {
        Intent intent = new Intent(Signup2ndClass.this,Login.class);
        startActivity(intent);
    }
    public void callNextCountryScreen(View view)
    {
        if(!validateAge() | !validateGender())
        {
            return;
        }

        //store gender to next activity
        selectedGender = findViewById(radio.getCheckedRadioButtonId());
        String gender = selectedGender.getText().toString();

        //get Age details to next screen
        int day = agePicker.getDayOfMonth();
        int month = agePicker.getMonth();
        int year = agePicker.getYear();
        String dateOfBirth = day+"/"+month+"/"+year;

        String name = getIntent().getStringExtra("fullName");
        String username = getIntent().getStringExtra("username");
        String email = getIntent().getStringExtra("email");
        String password = getIntent().getStringExtra("password");

        Intent intent = new Intent(Signup2ndClass.this,SignUp3rdClass.class);
        startActivity(intent);
    }

    private  boolean validateGender()
    {
        if (radio.getCheckedRadioButtonId()==-1)
        {
            Toast.makeText(this, "Please select gender", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            return true;
        }
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
        else
            return true;
    }
}