package com.example.mofomas.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mofomas.MenuBottomSheetFragment;
import com.example.mofomas.R;
import com.example.mofomas.adapter.PopularAdapter;
import com.example.mofomas.adapter.PopularItem;
import com.example.mofomas.adapter.MenuItem;
import com.example.mofomas.callNextScreenOTP;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Home extends Fragment implements MenuBottomSheetFragment.CartUpdateListener {

    private static final String TAG = "HomeFragment";
    private RecyclerView recyclerView;
    private PopularAdapter popularAdapter;
    private List<PopularItem> popularItemList;

    public Home() {
        // Required empty public constructor
    }

    public static Home newInstance() {
        return new Home();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Initialize data
        popularItemList = new ArrayList<>();

        // Set up adapter
        popularAdapter = new PopularAdapter(getActivity(), popularItemList, new PopularAdapter.OnAddToCartClickListener() {
            @Override
            public void onAddToCartClick(PopularItem item) {
                Log.d(TAG, "onAddToCartClick: Item clicked - " + item.getFoodName());
                // Call method in the hosting activity to handle item addition to cart
                ((callNextScreenOTP) requireActivity()).addItemToCart(item);
            }
        });
        recyclerView.setAdapter(popularAdapter);

        // Retrieve data from Firebase Realtime Database
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("FoodItems");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                popularItemList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.child("foodName").getValue(String.class);
                    String price = ds.child("foodAmount").getValue(String.class);
                    String imageUrl = ds.child("foodImages").getValue(String.class);

                    Log.d(TAG, "Data from Firebase - Name: " + name + ", Price: " + price + ", Image: " + imageUrl);

                    // Add item to list
                    if (name != null && price != null && imageUrl != null) {
                        popularItemList.add(new PopularItem(name, price, imageUrl));
                    }
                }

                // Notify adapter of data change
                popularAdapter.notifyDataSetChanged();
                Log.d(TAG, "Data updated in adapter, item count: " + popularAdapter.getItemCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Database error: " + databaseError.getMessage());
            }
        });

        // Set up click listener for "View Menu" TextView
        TextView viewMenu = view.findViewById(R.id.textView4);
        viewMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "View Menu clicked");
                MenuBottomSheetFragment bottomSheetFragment = new MenuBottomSheetFragment();
                bottomSheetFragment.setCartUpdateListener(Home.this);
                bottomSheetFragment.show(getParentFragmentManager(), bottomSheetFragment.getTag());
            }
        });

        return view;
    }

    @Override
    public void onAddToCart(MenuItem item) {
        Log.d(TAG, "onAddToCart: Item added to cart from MenuBottomSheet - " + item.getName());
        // Convert MenuItem to PopularItem if necessary, or modify your callNextScreenOTP to accept MenuItem
        PopularItem popularItem = new PopularItem(item.getName(), item.getPrice(), item.getImageUrl());
        ((callNextScreenOTP) requireActivity()).addItemToCart(popularItem);
    }
}