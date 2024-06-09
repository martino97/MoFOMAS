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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Home extends Fragment {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Initialize data
        popularItemList = new ArrayList<>();

        // Set up adapter
        popularAdapter = new PopularAdapter(getActivity(), popularItemList);
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

                    // Add item to list
                    popularItemList.add(new PopularItem(name, price, imageUrl));
                }

                // Notify adapter of data change
                popularAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors here
            }
        });

        // Set up click listener for "View Menu" TextView
        TextView viewMenu = view.findViewById(R.id.textView4);
        viewMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("HomeFragment", "View Menu clicked");
                MenuBottomSheetFragment bottomSheetFragment = new MenuBottomSheetFragment();
                bottomSheetFragment.show(getParentFragmentManager(), bottomSheetFragment.getTag());
            }
        });

        return view;
    }
}