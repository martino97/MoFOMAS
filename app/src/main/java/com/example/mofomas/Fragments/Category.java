package com.example.mofomas.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mofomas.ModelClass;
import com.example.mofomas.ModelClassAdapter;
import com.example.mofomas.PaymentMethods;
import com.example.mofomas.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Category extends Fragment {

    private RecyclerView recyclerView;
    private ModelClassAdapter adapter;
    private List<ModelClass> modelList;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        // Find the LinearLayout containing the icon and text
        LinearLayout paymentLayout = view.findViewById(R.id.payment_layout);

        // Set click listener for the entire layout
        paymentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog();
            }
        });

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.categoriesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize model list and adapter
        modelList = new ArrayList<>();
        adapter = new ModelClassAdapter(modelList);
        recyclerView.setAdapter(adapter);

        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("OrderHistory");

        // Get current user
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Load data from Firebase
        loadOrderData();

        return view;
    }

    private void showBottomSheetDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_layout, null);

        ImageView methodIcon = bottomSheetView.findViewById(R.id.method_icon);
        methodIcon.setImageResource(R.drawable.payment);

        TextView methodText = bottomSheetView.findViewById(R.id.method_text);
        methodText.setText("Proceed with Payment");

        methodIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                navigateToPaymentMethods();
            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    private void navigateToPaymentMethods() {
        Intent intent = new Intent(getActivity(), PaymentMethods.class);
        intent.putExtra("TYPE", "Payment");
        startActivity(intent);
    }

    private void loadOrderData() {
        if (currentUser == null) {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String currentUserId = currentUser.getUid();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                modelList.clear();
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    ModelClass order = orderSnapshot.getValue(ModelClass.class);
                    if (order != null && order.getUserId().equals(currentUserId)) {
                        modelList.add(order);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load order history: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
