// OrderHistoryActivity.java
package com.example.mofomas.admin;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mofomas.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerViewOrderHistory;
    private OrderHistoryAdapter orderHistoryAdapter;
    private List<Order> orderHistoryList;
    private DatabaseReference orderHistoryRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        recyclerViewOrderHistory = findViewById(R.id.recyclerViewOrderHistory);
        recyclerViewOrderHistory.setLayoutManager(new LinearLayoutManager(this));

        orderHistoryList = new ArrayList<>();
        orderHistoryAdapter = new OrderHistoryAdapter(this, orderHistoryList);
        recyclerViewOrderHistory.setAdapter(orderHistoryAdapter);

        // Initialize Firebase reference
        orderHistoryRef = FirebaseDatabase.getInstance().getReference("OrderHistory");

        // Retrieve data from Firebase
        fetchOrderHistory();
    }

    private void fetchOrderHistory() {
        orderHistoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderHistoryList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Order order = snapshot.getValue(Order.class);
                    if (order != null) {
                        orderHistoryList.add(order);
                    }
                }
                orderHistoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }
}
