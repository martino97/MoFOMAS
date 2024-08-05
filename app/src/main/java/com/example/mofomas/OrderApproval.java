package com.example.mofomas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class OrderApproval extends AppCompatActivity {

    private LinearLayout containerLayout;
    private DatabaseReference approvalRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_approval);

        containerLayout = findViewById(R.id.containerLayout);
        approvalRef = FirebaseDatabase.getInstance().getReference("Approval");

        // Query to order by timestamp
        Query query = approvalRef.orderByChild("timestamp");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                containerLayout.removeAllViews(); // Clear existing views

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String username = snapshot.child("username").getValue(String.class);
                    String code = snapshot.child("code").getValue(String.class);

                    // Create a CardView for each item
                    CardView cardView = (CardView) LayoutInflater.from(OrderApproval.this)
                            .inflate(R.layout.approval_item_card, containerLayout, false);

                    TextView usernameTextView = cardView.findViewById(R.id.usernameTextView);
                    TextView codeTextView = cardView.findViewById(R.id.codeTextView);

                    usernameTextView.setText("Username: " + username);
                    codeTextView.setText("Code: " + code);

                    containerLayout.addView(cardView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }
}