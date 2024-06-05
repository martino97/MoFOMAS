package com.example.mofomas;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.google.firebase.FirebaseException;
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
    Button resendCodeButton;
    TextView phoneNumberTextView;
    PinView pinFromUser;
    String codeBySystem;
    FirebaseAuth mAuth;
    DatabaseReference usersRef;
    String phoneNumber;
    CountDownTimer countDownTimer;
    boolean isOtpReceived = false;

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
        resendCodeButton = findViewById(R.id.resendCode);
        phoneNumberTextView = findViewById(R.id.phoneNumberTextView);

        // Initially hide the resend button
        resendCodeButton.setVisibility(View.GONE);

        // Get phone number from previous activity
        Intent intent = getIntent();
        phoneNumber = intent.getStringExtra("phoneNumber");

        // Format phone number
        if (phoneNumber.startsWith("0")) {
            phoneNumber = phoneNumber.substring(1);
        }
        phoneNumber =  phoneNumber;

        // Set formatted phone number in the TextView
        phoneNumberTextView.setText("Enter the One Time Password Sent\n to your Phone " + phoneNumber);

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

        resendCodeButton.setOnClickListener(v -> {
            if (resendCodeButton.isEnabled()) {
                sendVerificationToUser(phoneNumber);
                startCountDownTimer();
            } else {
                Toast.makeText(VerifyOTP.this, "Please wait before requesting a new code", Toast.LENGTH_SHORT).show();
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
        startOtpWaitTimer();
    }

    private void startOtpWaitTimer() {
        // Show the resend button after 60 seconds if OTP is not received
        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                // Do nothing each tick
            }

            public void onFinish() {
                if (!isOtpReceived) {
                    resendCodeButton.setVisibility(View.VISIBLE);
                    startCountDownTimer();
                }
            }
        }.start();
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            isOtpReceived = true;
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
        String Password = intent.getStringExtra("Password");
        String gender = intent.getStringExtra("Gender");
        String dateOfBirth = intent.getStringExtra("dateOfBirth");

        // Create a new User object
        User newUser = new User(fullName, username, email, Password, gender, dateOfBirth, phoneNumber);

        // Generate a unique ID for the user
        String userId = usersRef.push().getKey();

        // Store user data in Firebase under the unique ID
        if (userId != null) {
            usersRef.child(userId).setValue(newUser)
                    .addOnSuccessListener(aVoid -> {
                        // User data stored successfully
                        Toast.makeText(VerifyOTP.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                        // Create authentication for user with email and password
                        createAuthUser(email, Password,userId);
                    })
                    .addOnFailureListener(e -> {
                        // Failed to store user data
                        Log.e(TAG, "Error writing user data to Firebase", e);
                        Toast.makeText(VerifyOTP.this, "Failed to register user. Please try again.", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void createAuthUser(String email, String password, String userId) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // User authenticated successfully
                        startActivity(new Intent(VerifyOTP.this, MainActivity.class));
                        finish();
                    } else {
                        // Authentication failed
                        Log.e(TAG, "Error creating auth user", task.getException());
                        Toast.makeText(VerifyOTP.this, "Failed to create user authentication. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void startCountDownTimer() {
        resendCodeButton.setEnabled(false);
        countDownTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                resendCodeButton.setText("Resend Code (" + millisUntilFinished / 1000 + "s)");
            }

            public void onFinish() {
                resendCodeButton.setText("Resend Code");
                resendCodeButton.setEnabled(true);
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
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

        public User() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

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
