package com.example.mofomas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Login extends AppCompatActivity {
    private TextInputLayout usernameText, password;
    private Button logIn, createAccount, forget;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        usernameText = findViewById(R.id.loginusername);
        password = findViewById(R.id.loginpassword);
        logIn = findViewById(R.id.loginid);
        createAccount = findViewById(R.id.Loginreg);
        forget = findViewById(R.id.forgetbtn);
        progressBar = findViewById(R.id.progressBar);

        // Set click listener for login button
        logIn.setOnClickListener(v -> letTheUserLogIn());

        // Set click listener for create account button
        createAccount.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, Signup.class);
            startActivity(intent);
        });

        forget.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, ForgetPassword.class);
            startActivity(intent);
        });
    }

    private void letTheUserLogIn() {
        if (!validateFields()) {
            return;
        }

        // Show the progress bar
        progressBar.setVisibility(View.VISIBLE);

        String username = Objects.requireNonNull(usernameText.getEditText()).getText().toString().trim();
        String userPassword = Objects.requireNonNull(password.getEditText()).getText().toString().trim();

        Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("username").equalTo(username);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String systemPassword = userSnapshot.child("Password").getValue(String.class);
                        Long isAdminLong = userSnapshot.child("isAdmin").getValue(Long.class);
                        boolean isAdmin = isAdminLong != null && isAdminLong == 1;
                        String email = userSnapshot.child("email").getValue(String.class);

                        if (systemPassword != null && systemPassword.equals(userPassword) && email != null) {
                            // Authenticate with Firebase Auth using email and password
                            mAuth.signInWithEmailAndPassword(email, userPassword)
                                    .addOnCompleteListener(task -> {
                                        // Hide the progress bar
                                        progressBar.setVisibility(View.GONE);

                                        if (task.isSuccessful()) {
                                            // User authenticated successfully
                                            Toast.makeText(Login.this, "User authenticated successfully.", Toast.LENGTH_SHORT).show();
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            if (user != null) {
                                                // Start the appropriate activity based on user role
                                                if (isAdmin) {
                                                    Intent intent = new Intent(Login.this, AdminDashboardActivity.class);
                                                    startActivity(intent);
                                                } else {
                                                    Intent intent = new Intent(Login.this, callNextScreenOTP.class);
                                                    startActivity(intent);
                                                }
                                                finish(); // Close the current activity to prevent going back to login screen
                                            }
                                        } else {
                                            showAuthenticationError();
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        // Hide the progress bar
                                        progressBar.setVisibility(View.GONE);
                                        showAuthenticationError();
                                    });
                            return;
                        }
                    }
                    // Hide the progress bar
                    progressBar.setVisibility(View.GONE);
                    showAuthenticationError();
                } else {
                    // Hide the progress bar
                    progressBar.setVisibility(View.GONE);
                    showAuthenticationError();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Hide the progress bar
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Login.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAuthenticationError() {
        Toast.makeText(Login.this, "Authentication failed. Please check your username and password.", Toast.LENGTH_SHORT).show();
    }

    private boolean validateFields() {
        String _username = Objects.requireNonNull(usernameText.getEditText()).getText().toString().trim();
        String _password = Objects.requireNonNull(password.getEditText()).getText().toString().trim();

        if (_username.isEmpty()) {
            usernameText.setError("Username cannot be empty");
            usernameText.requestFocus();
            return false;
        } else if (_password.isEmpty()) {
            password.setError("Password cannot be empty");
            password.requestFocus();
            return false;
        }
        return true;
    }
}
