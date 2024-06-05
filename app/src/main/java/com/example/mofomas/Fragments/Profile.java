package com.example.mofomas.Fragments;;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
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

    private TextView usernameTextView;
    private CardView cartCardView;
    private CardView premiumCardView;
    private CardView standardCardView;
    private CardView casualCardView;
    private static final String TAG = "ProfileFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        usernameTextView = view.findViewById(R.id.username);
        cartCardView = view.findViewById(R.id.cartCardView);
        premiumCardView = view.findViewById(R.id.premiumCardView);
        standardCardView = view.findViewById(R.id.standardCardView);
        casualCardView = view.findViewById(R.id.casualCardView);

        loadUsername();
        setupCardViewListeners();

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
                            String fetchedUsername = childSnapshot.child("username").getValue(String.class);
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
        cartCardView.setOnClickListener(v -> onCardClick("CART"));
        premiumCardView.setOnClickListener(v -> onCardClick("PREMIUM"));
        standardCardView.setOnClickListener(v -> onCardClick("STANDARD"));
        casualCardView.setOnClickListener(v -> onCardClick("CASUAL"));
    }

    private void onCardClick(String category) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(uid);
            databaseReference.child("selectedCategory").setValue(category);

            // Start new activity based on category
            // Intent intent = new Intent(getActivity(), FoodMenuActivity.class);
            //  intent.putExtra("CATEGORY", category);
            //  startActivity(intent);
        }
    }
}
