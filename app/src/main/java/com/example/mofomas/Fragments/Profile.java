package com.example.mofomas.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.mofomas.Login;
import com.example.mofomas.R;
import com.example.mofomas.adapter.CartManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends Fragment {

    private TextView usernameTextView;
    private CardView cartCardView;
    private CardView premiumCardView;
    private CardView standardCardView;
    private CardView casualCardView;
    private Button logOut;
    private static final String TAG = "ProfileFragment";
    private String currentUserId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        usernameTextView = view.findViewById(R.id.username);
        cartCardView = view.findViewById(R.id.cartCardView);
        premiumCardView = view.findViewById(R.id.premiumCardView);
        standardCardView = view.findViewById(R.id.standardCardView);
        casualCardView = view.findViewById(R.id.casualCardView);
        logOut = view.findViewById(R.id.button);

        // Get current user email to identify the current user
        currentUserId = getCurrentUserEmail();

        loadUsername();
        setupCardViewListeners();
        setupLogOutButton(); // Added line

        return view;
    }

    private void loadUsername() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            Log.d(TAG, "User email: " + email);

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
            Log.d(TAG, "Database query path: " + databaseReference.toString());

            databaseReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.d(TAG, "DataSnapshot: " + snapshot.toString());
                    if (snapshot.exists()) {
                        Log.d(TAG, "Snapshot exists");
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            String fetchedUsername = childSnapshot.child("fullName").getValue(String.class);
                            if (fetchedUsername != null) {
                                usernameTextView.setText(fetchedUsername);
                                Log.d(TAG, "Username fetched and set: " + fetchedUsername);
                                return;
                            } else {
                                Log.d(TAG, "Username not found in snapshot");
                            }
                        }
                    } else {
                        Log.d(TAG, "Snapshot does not exist");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "Database error: " + error.getMessage());
                }
            });
        } else {
            Log.d(TAG, "User is not authenticated");
        }
    }

    private void setupCardViewListeners() {
        cartCardView.setOnClickListener(v -> onCardClick("SIMPLE"));
        premiumCardView.setOnClickListener(v -> onCardClick("PREMIUM"));
        standardCardView.setOnClickListener(v -> onCardClick("STANDARD"));
        casualCardView.setOnClickListener(v -> onCardClick("CASUAL"));
    }

    private void onCardClick(String category) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
            databaseReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            String fullName = childSnapshot.child("fullName").getValue(String.class);
                            String phoneNumber = childSnapshot.child("phoneNumber").getValue(String.class);

                            String uid = user.getUid();
                            DatabaseReference packageReference = FirebaseDatabase.getInstance().getReference("Package").child(uid);
                            packageReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        // User has already chosen a package, restrict them from choosing again
                                        Toast.makeText(getActivity(), "You have already chosen a package", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // User has not chosen a package, allow them to choose
                                        packageReference.child("selectedCategory").setValue(category);
                                        packageReference.child("fullName").setValue(fullName);
                                        packageReference.child("phoneNumber").setValue(phoneNumber);
                                        Toast.makeText(getActivity(), "Package chosen successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.e(TAG, "Database error: " + error.getMessage());
                                }
                            });
                        }
                    } else {
                        Log.d(TAG, "User data not found");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "Database error: " + error.getMessage());
                }
            });
        } else {
            Log.d(TAG, "User is not authenticated");
        }
    }

    // Get current user email
    private String getCurrentUserEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getEmail();
        } else {
            return "guest"; // Fallback or handle the case when user is not logged in
        }
    }

    // Logging user out and clearing the cart by using firebase authentication ID's
    private void setupLogOutButton() {
        logOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            CartManager.getInstance(getActivity()).clearCart(currentUserId);

            Intent intent = new Intent(getActivity(), Login.class);
            startActivity(intent);
            getActivity().finish();
        });
    }
}
