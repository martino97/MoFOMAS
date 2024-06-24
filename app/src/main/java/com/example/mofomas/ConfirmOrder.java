package com.example.mofomas;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mofomas.adapter.CartItem;
import com.example.mofomas.adapter.CartItemAdapter;
import com.example.mofomas.adapter.CartManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ConfirmOrder extends AppCompatActivity {

    private static final String TAG = "ConfirmOrderActivity";
    private RecyclerView recyclerView;
    private CartItemAdapter adapter;
    private List<CartItem> cartItemList;
    private TextInputEditText locationEditText;
    private TextInputEditText dateEditText;
    private TextInputEditText timeEditText;
    private Button confirmOrderButton;
    private String currentUserId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        recyclerView = findViewById(R.id.confirmOrderRecyclerView);
        locationEditText = findViewById(R.id.locationEditText);
        dateEditText = findViewById(R.id.dateEditText);
        timeEditText = findViewById(R.id.timeEditText);
        confirmOrderButton = findViewById(R.id.confirmOrderButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get current user email
        currentUserId = getCurrentUserEmail();

        // Get cart items from intent
        String cartItemsJson = getIntent().getStringExtra("cartItems");
        Type type = new TypeToken<List<CartItem>>() {}.getType();
        cartItemList = new Gson().fromJson(cartItemsJson, type);

        adapter = new CartItemAdapter(cartItemList, this);
        recyclerView.setAdapter(adapter);

        Log.d(TAG, "onCreate: Cart items received - " + cartItemList.size());

        // Set up date picker dialog
        dateEditText.setOnClickListener(v -> showDatePickerDialog());

        // Set up time picker dialog
        timeEditText.setOnClickListener(v -> showTimePickerDialog());

        // Set up confirm order button click listener
        confirmOrderButton.setOnClickListener(v -> {
            String location = locationEditText.getText().toString().trim();
            String date = dateEditText.getText().toString().trim();
            String time = timeEditText.getText().toString().trim();

            if (location.isEmpty() || date.isEmpty() || time.isEmpty()) {
                Toast.makeText(this, "Please enter location, date, and time", Toast.LENGTH_SHORT).show();
            } else {
                if (isDateValid(date) && isTimeValid(time)) {
                    showConfirmationDialog(location, date, time);
                } else {
                    if (!isDateValid(date)) {
                        Toast.makeText(this, "The date entered is already passed. Please enter the current date or above.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Please enter a time at least 2 hours from now", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void showConfirmationDialog(String location, String date, String time) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Order")
                .setMessage("Are you sure you want to place this order?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    uploadOrderDetails(location, date, time);
                })
                .setNegativeButton("No", (dialog, which) -> {
                    // Do nothing
                })
                .show();
    }

    private boolean isTimeValid(String inputTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        try {
            Date orderTime = sdf.parse(inputTime);
            if (orderTime != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(orderTime);

                // Get current time
                Calendar currentTime = Calendar.getInstance();

                // Set the order time calendar to today, with the provided hour and minute
                calendar.set(Calendar.YEAR, currentTime.get(Calendar.YEAR));
                calendar.set(Calendar.MONTH, currentTime.get(Calendar.MONTH));
                calendar.set(Calendar.DAY_OF_MONTH, currentTime.get(Calendar.DAY_OF_MONTH));

                // Add 2 hours to the current time
                currentTime.add(Calendar.HOUR_OF_DAY, 2);

                // Check if the input time is equal to or after the current time plus 2 hours
                return calendar.equals(currentTime) || calendar.after(currentTime);
            }
        } catch (ParseException e) {
            Log.e(TAG, "Invalid time format", e);
        }
        return false;
    }

    private boolean isDateValid(String inputDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date selectedDate = sdf.parse(inputDate);
            if (selectedDate != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(selectedDate);

                // Get current date
                Calendar currentDate = Calendar.getInstance();

                // Check if the selected date is today or in the future
                return calendar.equals(currentDate) || calendar.after(currentDate);
            }
        } catch (ParseException e) {
            Log.e(TAG, "Invalid date format", e);
        }
        return false;
    }

    private void uploadOrderDetails(String location, String date, String time) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userEmail = user.getEmail();

            // Get reference to the Users node in Firebase Database
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");

            // Query to find the user node based on email
            Query query = usersRef.orderByChild("email").equalTo(userEmail);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // There should be only one matching user node since emails are unique
                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                            String fullName = userSnapshot.child("fullName").getValue(String.class);
                            String phoneNumber = userSnapshot.child("phoneNumber").getValue(String.class);

                            // Generate unique orderId
                            String orderId = FirebaseDatabase.getInstance().getReference("FoodOrders").push().getKey();

                            if (orderId == null) {
                                Log.e(TAG, "Failed to generate orderId");
                                Toast.makeText(ConfirmOrder.this, "Failed to confirm order. Please try again.", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            // Proceed with uploading order details
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FoodOrders").child(orderId);

                            Map<String, Object> orderDetails = new HashMap<>();
                            orderDetails.put("orderId", orderId);
                            orderDetails.put("userId", user.getUid());
                            orderDetails.put("userEmail", userEmail);
                            orderDetails.put("fullName", fullName);
                            orderDetails.put("phoneNumber", phoneNumber);
                            orderDetails.put("location", location);
                            orderDetails.put("date", date);
                            orderDetails.put("time", time);
                            orderDetails.put("cartItems", cartItemList);

                            databaseReference.setValue(orderDetails)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(ConfirmOrder.this, "Order confirmed successfully", Toast.LENGTH_SHORT).show();
                                        clearCart();
                                        notifyCartFragment();
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e(TAG, "Failed to upload order details", e);
                                        Toast.makeText(ConfirmOrder.this, "Failed to confirm order. Please try again.", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        Log.e(TAG, "User data not found in Users node");
                        Toast.makeText(ConfirmOrder.this, "User data not found. Unable to confirm order.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "Database error: " + error.getMessage());
                    Toast.makeText(ConfirmOrder.this, "Database error. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearCart() {
        CartManager.getInstance(this).clearCart(currentUserId);
    }

    private void notifyCartFragment() {
        // Notify the CartFragment to update its data
        // This can be achieved by sending a broadcast or using an event bus like LiveData, or directly invoking a method
        // Assuming we're using a local broadcast or similar mechanism
        // For this example, let's use LocalBroadcastManager (you'll need to add the dependency for LocalBroadcastManager)

        Intent intent = new Intent("ACTION_CLEAR_CART");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    // Method to get the current user email
    private String getCurrentUserEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getEmail();
        } else {
            return "guest"; // Fallback or handle the case when user is not logged in
        }
    }

    private void showDatePickerDialog() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create and show the DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Check if the selected date is after or equal to the current date
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(selectedYear, selectedMonth, selectedDay);
                    if (selectedDate.compareTo(calendar) >= 0) {
                        String formattedDate = (selectedMonth + 1) + "/" + selectedDay + "/" + selectedYear;
                        dateEditText.setText(formattedDate);
                    } else {
                        Toast.makeText(this, "Please select a date starting from today or later", Toast.LENGTH_SHORT).show();
                    }
                }, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        // Get the current time
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create and show the TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, selectedHour, selectedMinute) -> {
                    String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute);
                    timeEditText.setText(formattedTime);
                }, hour, minute, true);
        timePickerDialog.show();
    }
}
