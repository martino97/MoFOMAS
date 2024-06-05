package com.example.mofomas.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mofomas.MenuBottomSheetFragment;
import com.example.mofomas.R;
import com.example.mofomas.adapter.PopularAdapter;
import com.example.mofomas.adapter.PopularItem;

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
        popularItemList.add(new PopularItem("Tea", "Tsh.7000/=", R.drawable.food1));
        popularItemList.add(new PopularItem("Burns", "Tsh.8000/=", R.drawable.food2));
        popularItemList.add(new PopularItem("Cooked Banana", "Tsh.9000/=", R.drawable.food3));
        popularItemList.add(new PopularItem("pilau", "Tsh.4000/=", R.drawable.food4));
        popularItemList.add(new PopularItem("Coconut rice with Peas", "Tsh.3000/=", R.drawable.food5));
        popularItemList.add(new PopularItem("Stef porridge", "Tsh.5000/=", R.drawable.food1));
        popularItemList.add(new PopularItem("Meat Stew", "Tsh.6000/=", R.drawable.food2));
        popularItemList.add(new PopularItem("Vegetable Rice", "Tsh.3500/=", R.drawable.food3));
        popularItemList.add(new PopularItem("Juice", "Tsh.2000/=", R.drawable.food4));
        popularItemList.add(new PopularItem("Rhotah", "Tsh.10000/=", R.drawable.food5));
        // Add more items if needed

        // Set up adapter
        popularAdapter = new PopularAdapter(getActivity(), popularItemList);
        recyclerView.setAdapter(popularAdapter);

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
