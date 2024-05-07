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
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class VerifyOTP extends AppCompatActivity {
    Button button;

    PinView pinFromUser;
    String codeBySystem;
    FirebaseAuth mAuth;
    DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verify_otp);


        //hooks
        pinFromUser = findViewById(R.id.pinView);
        button = findViewById(R.id.verifyingCode);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        //getting a phone number:
        Intent intent = getIntent();
        String phoneNumber = intent.getStringExtra("phoneNumber");
        String fullName = intent.getStringExtra("fullName");
        String username = intent.getStringExtra("username");
        String email = intent.getStringExtra("email");
        String gender = intent.getStringExtra("gender");
        String date = intent.getStringExtra("dateOfBirth");
        String password = intent.getStringExtra("password");


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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void sendVerificationToUser(String phoneNo) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNo)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code =phoneAuthCredential.getSmsCode();
            if(code!=null)
            {
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



    public  void callNextScreenOTP(FirebaseUser view) {

        String code = Objects.requireNonNull(pinFromUser.getText()).toString();
        if (!code.isEmpty()) {
            verifyCode(code);
       }

    }
    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem, code);
        String verificationCode = credential.getSmsCode();
        if (verificationCode != null) {
            signInWithPhoneAuthCredential(credential);
        } else {
            // The verification code is null, handle the error
            Toast.makeText(VerifyOTP.this, "Invalid verification code", Toast.LENGTH_SHORT).show();
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            // Register the user in Firebase
                            assert user != null;
                            registerUserInFirebase(user);
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered by the user is invalid
                                Toast.makeText(VerifyOTP.this, "Invalid verification code", Toast.LENGTH_SHORT).show();
                            } else if (task.getException() instanceof FirebaseTooManyRequestsException) {
                                // The user has exceeded the maximum number of allowed attempts to verify their phone number
                                Toast.makeText(VerifyOTP.this, "Too many attempts. Please try again later.", Toast.LENGTH_SHORT).show();
                            } else {
                                // An unknown error occurred
                                Toast.makeText(VerifyOTP.this, "An error occurred. Please try again later.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void registerUserInFirebase(FirebaseUser mofomas) {
        // Create a User object with relevant details
        User newUser = new User(
                mofomas.getUid(),
                Objects.requireNonNull(getIntent().getStringExtra("fullName")),
                Objects.requireNonNull(getIntent().getStringExtra("username")),
                Objects.requireNonNull(getIntent().getStringExtra("email")),
                Objects.requireNonNull(getIntent().getStringExtra("gender")),
                Objects.requireNonNull(getIntent().getStringExtra("dateOfBirth"))
        );

        // Push the user object to Firebase Realtime Database
        usersRef.child(mofomas.getUid()).setValue(newUser)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User registered in Firebase Realtime Database");
                            // User registration successful, navigate to the next screen
                            callNextScreenOTP(mofomas);
                        } else {
                            Log.w(TAG, "Failed to register user in Firebase Realtime Database", task.getException());
                            // Handle the error, e.g., show an error message
                            Toast.makeText(VerifyOTP.this, "Failed to register user. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing to Firebase Realtime Database", e);
                        // Handle the error, e.g., show an error message
                        Toast.makeText(VerifyOTP.this, "Failed to register user. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    // Define a User class to hold user details
    public static class User {
        String userId;
        String fullName;
        String username;
        String email;
        String gender;
        String dateOfBirth;

        public User(String userId, String fullName, String username, String email, String gender, String dateOfBirth) {
            this.userId = userId;
            this.fullName = fullName;
            this.username = username;
            this.email = email;
            this.gender = gender;
            this.dateOfBirth = dateOfBirth;
        }
    }


  /*  private void createUserAccount(String verificationId, String phoneNumber) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, phoneNumber);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        user.linkWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Phone number linked to user account");
                            // Phone number linked successfully
                            callNextScreenOTP(user);
                        } else {
                            Log.w(TAG, "Phone number linking failed", task.getException());
                            // Phone number linking failed, handle the error
                            callNextScreenOTP(mAuth.getCurrentUser());
                        }
                    }
                });
    }
     //  FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
       // DatabaseReference reference = rootNode.getReference("users");
     //  reference.setValue("Malopa");*/
    }
