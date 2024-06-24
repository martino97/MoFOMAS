package com.example.mofomas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

        // Set click listener for the "Add Item" CardView
        addItemCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to AddItemActivity
                Intent intent = new Intent(AdminDashboardActivity.this, AddItems.class);
                startActivity(intent);
            }
        });

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

        historyCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to OrderHistoryActivity
                Intent intent = new Intent(AdminDashboardActivity.this, OrderHistoryActivity.class);
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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
