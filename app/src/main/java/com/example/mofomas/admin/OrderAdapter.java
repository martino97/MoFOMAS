// OrderAdapter.java
package com.example.mofomas.admin;

import android.Manifest;
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

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orderList;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_view_order, parent, false);
        return new OrderViewHolder(view);
    }

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

        boolean isExpanded = holder.layoutExpandable.getVisibility() == View.VISIBLE;
        holder.textViewOrderDetails.setOnClickListener(v -> {
            holder.layoutExpandable.setVisibility(isExpanded ? View.GONE : View.VISIBLE);
        });

        holder.buttonConfirm.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.SEND_SMS}, 1);
            } else {
                sendSms(order.getPhoneNumber(), "Your order has been received by MoCU-FOMA kindly wait for your service being processed. Thank you!");
            }
        });
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
}
