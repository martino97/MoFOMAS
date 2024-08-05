package com.example.mofomas;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mofomas.admin.AddItems;
import com.example.mofomas.admin.OrderHistoryActivity;
import com.example.mofomas.ViewOrder;
import com.example.mofomas.admin.ViewPackage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminDashboardActivity extends AppCompatActivity {

    private TextView pendingOrderCountTextView;
    private TextView completedOrderCountTextView;
    private DatabaseReference foodOrderRef;
    private DatabaseReference orderHistoryRef;
    private DatabaseReference approvalRef;
    private FirebaseUser currentUser;
    private DatabaseReference userRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_dashboard);

        // Find the "Add Item" CardView by its ID
        CardView addItemCardView = findViewById(R.id.cardViewAddMenu);
        CardView packageCardView = findViewById(R.id.packageId);
        CardView viewOrderCardView = findViewById(R.id.viewOrder);
        CardView historyCardView = findViewById(R.id.historyCard);

        // Find the TextViews for pending and completed order counts
        pendingOrderCountTextView = findViewById(R.id.pendingOrderCount);
        completedOrderCountTextView = findViewById(R.id.completedOrderCount);
        ImageView wholeTimeEarningsIcon = findViewById(R.id.wholeTimeEarningsIcon);
        TextView wholeTimeEarningsText = findViewById(R.id.wholeTimeEarningsCount);
       TextView usernameTextView = findViewById(R.id.usernameTextView);



        // Set click listener for the "Add Item" CardView
        addItemCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to AddItemActivity
                Intent intent = new Intent(AdminDashboardActivity.this, AddItems.class);
                startActivity(intent);
            }
        });

        // Set click listener for "ViewOrder" CardView
        viewOrderCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ViewOrder Activity
                Intent intent = new Intent(AdminDashboardActivity.this, ViewOrder.class);
                startActivity(intent);
            }
        });

        // Set click listener for the "Package" CardView
        packageCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ViewPackageActivity
                Intent intent = new Intent(AdminDashboardActivity.this, ViewPackage.class);
                startActivity(intent);
            }
        });

        // Set click listener for "OrderHistory" CardView
        historyCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to OrderHistoryActivity
                Intent intent = new Intent(AdminDashboardActivity.this, OrderHistoryActivity.class);
                startActivity(intent);
            }
        });

        // Set click listener for "wholeTimeEarningsIcon" ImageView
        wholeTimeEarningsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboardActivity.this, OrderApproval.class);
                startActivity(intent);
            }
        });

        // Retrieve and display the total number of pending orders
        foodOrderRef = FirebaseDatabase.getInstance().getReference("FoodOrders");
        foodOrderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();
                pendingOrderCountTextView.setText("" + count);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });

        // Retrieve and display the total number of completed orders
        orderHistoryRef = FirebaseDatabase.getInstance().getReference("OrderHistory");
        orderHistoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();
                completedOrderCountTextView.setText("" + count);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
        approvalRef = FirebaseDatabase.getInstance().getReference("Approval");
        approvalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();
                wholeTimeEarningsText.setText("" + count);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });

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


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
