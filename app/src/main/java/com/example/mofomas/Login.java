package com.example.mofomas;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;

public class Login extends AppCompatActivity {
    Button signUp;
            //login;
    //TextInputLayout username,password;
    //TextView txtView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        //go to sign up page
        signUp = findViewById(R.id.Loginreg);
       /* login = findViewById(R.id.loginid);
        username = findViewById(R.id.Loginuser);
        password = findViewById(R.id.Loginpsd);
        txtView = findViewById(R.id.textView);*/

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,Signup.class);
                startActivity(intent);


              /*  Pair[] pairs = new Pair[5];
               pairs[0]  = new Pair<View,String>(signUp,"create acc");
               pairs[1]  = new Pair<View,String>(login,"Login");
                pairs[2]  = new Pair<View,String>(username,"user");
                pairs[3]  = new Pair<View,String>(password,"password");
                pairs[4]  = new Pair<View,String>(txtView,"Login_here");
                //call next Activities
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this,pairs);
                //animation and transition to new activities*/


            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}