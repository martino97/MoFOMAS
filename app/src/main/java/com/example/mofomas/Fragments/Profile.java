package com.example.mofomas.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mofomas.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends Fragment {

    private TextView fullNameTextView;
    private TextView usernameTextView;
    private TextView emailTextView;
    private TextView genderTextView;
    private TextView dateOfBirthTextView;

    private DatabaseReference mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize TextViews
        fullNameTextView = view.findViewById(R.id.fullNameTextView);
        usernameTextView = view.findViewById(R.id.usernameTextView);
        emailTextView = view.findViewById(R.id.emailTextView);
        genderTextView = view.findViewById(R.id.genderTextView);
        dateOfBirthTextView = view.findViewById(R.id.dateOfBirthTextView);

        // Retrieve and display user data
        loadUserData();

        return view;
    }

    private void loadUserData() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String username = currentUser.getDisplayName(); // Use the username as the key
            DatabaseReference userRef = mDatabase.child("Users").child(username);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d("ProfileFragment", "DataSnapshot: " + dataSnapshot);
                    if (dataSnapshot.exists()) {
                        // Retrieve user data
                        String fullName = dataSnapshot.child("fullName").getValue(String.class);
                        String username = dataSnapshot.child("username").getValue(String.class);
                        String email = dataSnapshot.child("email").getValue(String.class);
                        String gender = dataSnapshot.child("gender").getValue(String.class);
                        String dateOfBirth = dataSnapshot.child("dateOfBirth").getValue(String.class);

                        // Update TextViews with retrieved data
                        if (fullName != null) fullNameTextView.setText(fullName);
                        if (username != null) usernameTextView.setText(username);
                        if (email != null) emailTextView.setText(email);
                        if (gender != null) genderTextView.setText(gender);
                        if (dateOfBirth != null) dateOfBirthTextView.setText(dateOfBirth);
                    } else {
                        Log.d("ProfileFragment", "User data does not exist");
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("ProfileFragment", "Error retrieving user data: " + databaseError.getMessage());
                }
            });
        }
    }
}
