package com.example.mofomas;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mofomas.MenuAdapter;
import com.example.mofomas.adapter.MenuItem;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MenuBottomSheetFragment extends BottomSheetDialogFragment {

    private RecyclerView recyclerViews;
    private MenuAdapter menuAdapter;
    private List<MenuItem> menuItems;
    private CartUpdateListener cartUpdateListener;

    public interface CartUpdateListener {
        void onAddToCart(MenuItem item);
    }

    public void setCartUpdateListener(CartUpdateListener listener) {
        this.cartUpdateListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_bottom_sheet, container, false);
        recyclerViews = view.findViewById(R.id.recyclerView);
        recyclerViews.setLayoutManager(new LinearLayoutManager(getContext()));

        menuItems = new ArrayList<>();

        menuAdapter = new MenuAdapter(menuItems);
        menuAdapter.setOnAddToCartClickListener(new MenuAdapter.OnAddToCartClickListener() {
            @Override
            public void onAddToCartClick(MenuItem item) {
                if (cartUpdateListener != null) {
                    cartUpdateListener.onAddToCart(item);
                    Log.d("MenuBottomSheetFragment", "Item added to cart: " + item.getName());
                }
            }
        });
        recyclerViews.setAdapter(menuAdapter);

        // Retrieve data from Firebase Realtime Database
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("FoodItems");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                menuItems.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String foodName = ds.child("foodName").getValue(String.class);
                    String foodAmount = ds.child("foodAmount").getValue(String.class);
                    String foodImages = ds.child("foodImages").getValue(String.class);

                    Log.d("MenuBottomSheetFragment", "Retrieved menu item: " + foodName + ", " + foodAmount + ", " + foodImages);

                    // Add item to list
                    menuItems.add(new MenuItem(foodName, foodAmount, foodImages));
                }

                // Update the adapter with the new data
                menuAdapter.notifyDataSetChanged(); // Add this line to notify the adapter
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("MenuBottomSheetFragment", "Error retrieving data: " + databaseError.getMessage());
            }
        });

        // Set up click listener for the arrow ImageButton to dismiss the BottomSheet
        ImageView arrowButton = view.findViewById(R.id.arrow);
        arrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }
}