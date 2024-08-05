package com.example.mofomas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgetPassword extends AppCompatActivity {

    private Button verify;
    private TextInputLayout phoneNumber;
    private CountryCodePicker ccp;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        // Initialize Firebase Auth and Database
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize views
        verify = findViewById(R.id.sendRequest);
        ccp = findViewById(R.id.countryCode);
        phoneNumber = findViewById(R.id.forgetNumber);

        // Set click listener for verify button
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validatePhoneNumber()) {
                    checkPhoneNumberExistsAndSendOTP();
                }
            }
        });
    }

    private void checkPhoneNumberExistsAndSendOTP() {
        String fullPhoneNumber = "+" + ccp.getSelectedCountryCode() + Objects.requireNonNull(phoneNumber.getEditText()).getText().toString().trim();

        mDatabase.child("Users").orderByChild("phoneNumber").equalTo(fullPhoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Phone number exists, send OTP
                    sendOTP(fullPhoneNumber);
                } else {
                    // Phone number doesn't exist
                    Toast.makeText(ForgetPassword.this, "Phone number not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ForgetPassword.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendOTP(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        // This callback will be invoked in two situations:
                        // 1 - Instant verification. In some cases the phone number can be instantly
                        //     verified without needing to send or enter a verification code.
                        // 2 - Auto-retrieval. On some devices Google Play services can automatically
                        //     detect the incoming verification SMS and perform verification without
                        //     user action.
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(ForgetPassword.this, "Verification failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        // The SMS verification code has been sent to the provided phone number
                        // Now start the OTP verification activity
                        Intent intent = new Intent(ForgetPassword.this, VerifyOTP.class);
                        intent.putExtra("phoneNumber", phoneNumber);
                        intent.putExtra("verificationId", verificationId);
                        startActivity(intent);
                        finish();
                    }
                }
        );
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