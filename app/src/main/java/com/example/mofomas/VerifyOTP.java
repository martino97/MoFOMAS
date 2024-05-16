package com.example.mofomas;


import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class VerifyOTP extends AppCompatActivity {
    Button button;

    PinView pinFromUser;
    String codeBySystem;
    FirebaseAuth mAuth;
    DatabaseReference usersRef;
    String fullNameText, usernameText, emailText, passwordText, gender, dateOfBirth, phonenumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_verify_otp);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");

        // Initialize views
        pinFromUser = findViewById(R.id.pinView);
        button = findViewById(R.id.verifyingCode);

        // Get phone number from previous activity
        Intent intent = getIntent();
        String phoneNumber = intent.getStringExtra("phoneNumber");

        // Send verification code to user
        sendVerificationToUser(phoneNumber);

        button.setOnClickListener(v -> {
            String code = Objects.requireNonNull(pinFromUser.getText()).toString();
            if (!code.isEmpty()) {
                verifyCode(code);
            } else {
                Toast.makeText(VerifyOTP.this, "Please enter the OTP", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendVerificationToUser(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                pinFromUser.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codeBySystem = s;
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(VerifyOTP.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem, code);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = task.getResult().getUser();
                        if (user != null) {
                            // User authenticated successfully, store user data in Firebase
                            storeUserDataInFirebase();
                        }
                    } else {
                        // Verification failed
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(VerifyOTP.this, "Invalid verification code", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(VerifyOTP.this, "Verification failed. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void storeUserDataInFirebase() {
        // Retrieve user data from previous activities
        Intent intent = getIntent();
        String fullName = intent.getStringExtra("FullName");
        String username = intent.getStringExtra("Username");
        String email = intent.getStringExtra("Email");
        String password = intent.getStringExtra("Password");
        String gender = intent.getStringExtra("Gender");
        String dateOfBirth = intent.getStringExtra("dateOfBirth");
        String phoneNumber = intent.getStringExtra("phoneNumber");

        // Create a new User object
        User newUser = new User(fullName, username, email, password, gender, dateOfBirth, phoneNumber);

        // Store user data in Firebase
        usersRef.child(username).setValue(newUser)
                .addOnSuccessListener(aVoid -> {
                    // User data stored successfully
                    Toast.makeText(VerifyOTP.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                    // Navigate to MainActivity or any other activity
                    startActivity(new Intent(VerifyOTP.this, MainActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    // Failed to store user data
                    Log.e(TAG, "Error writing user data to Firebase", e);
                    Toast.makeText(VerifyOTP.this, "Failed to register user. Please try again.", Toast.LENGTH_SHORT).show();
                });
    }

    // Define a User class to hold user details
    public static class User {
        public String fullName;
        public String username;
        public String email;
        public String Password;
        public String gender;
        public String dateOfBirth;
        public String phoneNumber;

        public User(String fullName, String username, String email, String Password, String gender, String dateOfBirth, String phoneNumber) {
            this.fullName = fullName;
            this.username = username;
            this.email = email;
            this.Password = Password;
            this.gender = gender;
            this.dateOfBirth = dateOfBirth;
            this.phoneNumber = phoneNumber;
        }
    }
}