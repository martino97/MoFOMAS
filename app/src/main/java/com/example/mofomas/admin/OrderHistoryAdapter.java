package com.example.mofomas.admin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mofomas.R;

import java.util.List;
import java.util.Random;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder> {
    private Context context;
    private List<Order> orderHistoryList;
    private SharedPreferences sharedPreferences;

    public OrderHistoryAdapter(Context context, List<Order> orderHistoryList) {
        this.context = context;
        this.orderHistoryList = orderHistoryList;
        this.sharedPreferences = context.getSharedPreferences("OrderDeliveryStatus", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_history, parent, false);
        return new OrderHistoryViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, int position) {
        Order order = orderHistoryList.get(position);
        holder.textViewOrderDetails.setText("Order " + (position + 1));
        holder.textViewFullName.setText("Full Name: " + order.getFullName());
        holder.textViewPhoneNumber.setText("Phone Number: " + order.getPhoneNumber());
        holder.textViewDate.setText("Date: " + order.getDate());
        holder.textViewLocation.setText("Location: " + order.getLocation());
        holder.textViewTime.setText("Time: " + order.getTime());

        holder.recyclerViewOrderHistory.setLayoutManager(new LinearLayoutManager(context));
        CartItemAdapter cartItemAdapter = new CartItemAdapter(context, order.getCartItems());
        holder.recyclerViewOrderHistory.setAdapter(cartItemAdapter);

        boolean isExpanded = holder.layoutExpandable.getVisibility() == View.VISIBLE;
        holder.textViewOrderDetails.setOnClickListener(v -> {
            holder.layoutExpandable.setVisibility(isExpanded ? View.GONE : View.VISIBLE);
        });

        // Check if the order has been delivered
        String orderId = order.getOrderId(); // Assuming you have a unique orderId in your Order class
        boolean isDelivered = sharedPreferences.getBoolean(orderId, false);

        if (isDelivered) {
            holder.buttonOnDelivery.setVisibility(View.GONE);
        } else {
            holder.buttonOnDelivery.setVisibility(View.VISIBLE);
            holder.buttonOnDelivery.setOnClickListener(v -> {
                String token = generateToken();
                String message = "Your order will be delivered and arrive soon by our MoCUFOMA delivery agent and your token number is " + token;
                sendSMS(order.getPhoneNumber(), message);

                // Mark the order as delivered
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(orderId, true);
                editor.apply();

                holder.buttonOnDelivery.setVisibility(View.GONE);
            });
        }
    }

    @Override
    public int getItemCount() {
        return orderHistoryList.size();
    }

    private String generateToken() {
        Random random = new Random();
        StringBuilder token = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            token.append(random.nextInt(10));
        }
        return token.toString();
    }

    private void sendSMS(String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error message)
        }
    }

    static class OrderHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView textViewOrderDetails, textViewFullName, textViewPhoneNumber, textViewDate, textViewLocation, textViewTime;
        LinearLayout layoutExpandable;
        RecyclerView recyclerViewOrderHistory;
        Button buttonOnDelivery;

        public OrderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewOrderDetails = itemView.findViewById(R.id.OrderDetails);
            textViewFullName = itemView.findViewById(R.id.textViewFullName);
            textViewPhoneNumber = itemView.findViewById(R.id.textViewPhoneNumber);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewLocation = itemView.findViewById(R.id.textViewLocation);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            layoutExpandable = itemView.findViewById(R.id.layoutExpandable);
            recyclerViewOrderHistory = itemView.findViewById(R.id.recyclerViewHistory);
            buttonOnDelivery = itemView.findViewById(R.id.buttonOnDelivery);
        }
    }
}