package com.example.mofomas.admin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mofomas.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orderList;
    private DatabaseReference foodOrderRef;
    private DatabaseReference orderHistoryRef;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
        // Initialize Firebase references
        foodOrderRef = FirebaseDatabase.getInstance().getReference("FoodOrders");
        orderHistoryRef = FirebaseDatabase.getInstance().getReference("OrderHistory");
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_view_order, parent, false);
        return new OrderViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.textViewOrderDetails.setText("Order " + (position + 1));
        holder.textViewFullName.setText("Full Name: " + order.getFullName());
        holder.textViewPhoneNumber.setText("Phone Number: " + order.getPhoneNumber());
        holder.textViewDate.setText("Date: " + order.getDate());
        holder.textViewLocation.setText("Location: " + order.getLocation());
        holder.textViewTime.setText("Time: " + order.getTime());

        holder.recyclerViewCartItems.setLayoutManager(new LinearLayoutManager(context));
        CartItemAdapter cartItemAdapter = new CartItemAdapter(context, order.getCartItems());
        holder.recyclerViewCartItems.setAdapter(cartItemAdapter);

        // Set initial state
        holder.layoutExpandable.setVisibility(View.GONE);

        // Modified click listener for expandable/minimizable functionality
        holder.textViewOrderDetails.setOnClickListener(v -> {
            boolean isExpanded = holder.layoutExpandable.getVisibility() == View.VISIBLE;
            holder.layoutExpandable.setVisibility(isExpanded ? View.GONE : View.VISIBLE);

            // Update the text to indicate expanded/collapsed state
            holder.textViewOrderDetails.setText(isExpanded ?
                    "Order " + (position + 1) + " (Tap to expand)" :
                    "Order " + (position + 1) + " (Tap to collapse)");
        });

        // Check if order is confirmed and disable button if it is
        if (order.isConfirmed()) {
            holder.buttonConfirm.setEnabled(false);
        } else {
            holder.buttonConfirm.setEnabled(true);
            holder.buttonConfirm.setOnClickListener(v -> {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.SEND_SMS}, 1);
                } else {
                    sendSms(order.getPhoneNumber(), "Your order has been received by MoCU-FOMA kindly wait for your service being processed and issued. Thank you! for Choosing the home of best Meals");
                    moveOrderToHistory(order, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView textViewOrderDetails, textViewFullName, textViewPhoneNumber, textViewDate, textViewLocation, textViewTime;
        LinearLayout layoutExpandable;
        RecyclerView recyclerViewCartItems;
        Button buttonConfirm;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewOrderDetails = itemView.findViewById(R.id.textViewOrderDetails);
            textViewFullName = itemView.findViewById(R.id.textViewFullName);
            textViewPhoneNumber = itemView.findViewById(R.id.textViewPhoneNumber);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewLocation = itemView.findViewById(R.id.textViewLocation);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            layoutExpandable = itemView.findViewById(R.id.layoutExpandable);
            recyclerViewCartItems = itemView.findViewById(R.id.recyclerViewCartItems);
            buttonConfirm = itemView.findViewById(R.id.buttonConfirm);
        }
    }

    private void sendSms(String phoneNumber, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        Toast.makeText(context, "SMS sent to " + phoneNumber, Toast.LENGTH_SHORT).show();
    }

    private void moveOrderToHistory(Order order, int position) {
        // the Method getOrderId() will be used to remove the order item from the table path FoodOrders to table path OrderHistory in Firebase
        String orderId = order.getOrderId();

        // Add order to OrderHistory
        orderHistoryRef.child(orderId).setValue(order).addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()) {
                // Only if adding to OrderHistory is successful, remove order from FoodOrder
                foodOrderRef.child(orderId).removeValue().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "Order moved to history", Toast.LENGTH_SHORT).show();
                        // Update the order to be confirmed
                        order.setConfirmed(true);
                        notifyItemChanged(position);
                    } else {
                        Toast.makeText(context, "Failed to remove order from FoodOrder", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(context, "Failed to add order to history", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
