package com.example.mofomas;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.HashMap;
import java.util.Map;

public class Deliverer extends AppCompatActivity {

    private TextInputLayout codeInputLayout;
    private TextInputEditText codeEditText;
    // Add new TextInputLayout and TextInputEditText for username
    private TextInputLayout usernameInputLayout;
    private TextInputEditText usernameEditText;
    private Button submitButton;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliverer);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize views
        codeInputLayout = findViewById(R.id.codeInputLayout);
        codeEditText = findViewById(R.id.codeEditText);
        // Initialize new username views
        usernameInputLayout = findViewById(R.id.nameInputLayout);
        usernameEditText = findViewById(R.id.nameEditText);
        submitButton = findViewById(R.id.submitButton);
        progressBar = findViewById(R.id.progressBar);

        // Set click listener for submit button
        submitButton.setOnClickListener(v -> submitCode());
    }

    private void submitCode() {
        String code = codeEditText.getText().toString().trim();
        // Get the entered username
        String enteredUsername = usernameEditText.getText().toString().trim();

        if (!validateCode(code) || !validateUsername(enteredUsername)) {
            return;
        }

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        showProgressBar();

        // Use the entered username directly
        sendApprovalData(code, currentUser.getUid(), enteredUsername);
    }

    private void sendApprovalData(String code, String userId, String username) {
        // Create a map of data to send to Firebase
        Map<String, Object> approvalData = new HashMap<>();
        approvalData.put("code", code);
        approvalData.put("userId", userId);
        approvalData.put("username", username);
        approvalData.put("timestamp", System.currentTimeMillis());

        // Send data to Firebase
        mDatabase.child("Approval").push().setValue(approvalData)
                .addOnCompleteListener(task -> {
                    hideProgressBar();
                    if (task.isSuccessful()) {
                        Toast.makeText(Deliverer.this, "Code submitted successfully", Toast.LENGTH_SHORT).show();
                        clearInput();
                    } else {
                        Toast.makeText(Deliverer.this, "Failed to submit code", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    hideProgressBar();
                    Toast.makeText(Deliverer.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private boolean validateCode(String code) {
        if (code.isEmpty()) {
            codeInputLayout.setError("Code cannot be empty");
            return false;
        } else if (code.length() != 4) {  // Assuming the code should be 4 digits
            codeInputLayout.setError("Code must be 4 digits");
            return false;
        } else {
            codeInputLayout.setError(null);
            return true;
        }
    }

    // Add a new method to validate the username
    private boolean validateUsername(String username) {
        if (username.isEmpty()) {
            usernameInputLayout.setError("Username cannot be empty");
            return false;
        } else {
            usernameInputLayout.setError(null);
            return true;
        }
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        submitButton.setEnabled(false);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
        submitButton.setEnabled(true);
    }

    private void clearInput() {
        codeEditText.getText().clear();
        usernameEditText.getText().clear();
    }
}