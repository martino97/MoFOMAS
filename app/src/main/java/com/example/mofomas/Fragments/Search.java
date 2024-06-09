package com.example.mofomas.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mofomas.MenuAdapter;
import com.example.mofomas.R;
import com.example.mofomas.adapter.MenuItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Search extends Fragment {

    private RecyclerView recyclerView;
    private MenuAdapter menuAdapter;
    private SearchView searchView;
    private List<MenuItem> menuItems;
    private List<MenuItem> originalMenuItems;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchView = view.findViewById(R.id.searchView);
        recyclerView = view.findViewById(R.id.recyclerView);

        menuItems = new ArrayList<>();
        originalMenuItems = new ArrayList<>();

        // Retrieve data from Firebase Realtime Database
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("FoodItems");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                originalMenuItems.clear();
                menuItems.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.child("foodName").getValue(String.class);
                    String price = ds.child("foodAmount").getValue(String.class);
                    String imageUrl = ds.child("foodImages").getValue(String.class);

                    // Add item to list
                    MenuItem menuItem = new MenuItem(name, price, imageUrl);
                    originalMenuItems.add(menuItem);
                    menuItems.add(menuItem);
                }

                // Notify adapter of data change
                menuAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors here
            }
        });

        menuAdapter = new MenuAdapter(menuItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(menuAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterMenuItems(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterMenuItems(newText);
                return false;
            }
        });

        return view;
    }

    private void filterMenuItems(String query) {
        menuItems.clear();
        for (MenuItem menuItem : originalMenuItems) {
            if (menuItem.getName().toLowerCase().contains(query.toLowerCase())) {
                menuItems.add(menuItem);
            }
        }
        menuAdapter.notifyDataSetChanged();
    }
}