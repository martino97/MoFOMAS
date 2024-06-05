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
import java.util.ArrayList;
import java.util.List;

public class Search extends Fragment {

    private RecyclerView recyclerView;
    private MenuAdapter menuAdapter;
    private SearchView searchView;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchView = view.findViewById(R.id.searchView);
        recyclerView = view.findViewById(R.id.recyclerView);

        List<MenuItem> menuItems = getMenuItems();
        menuAdapter = new MenuAdapter(menuItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(menuAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                menuAdapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                menuAdapter.filter(newText);
                return false;
            }
        });

        return view;
    }

    private List<MenuItem> getMenuItems() {
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("Tea", "Tsh.500/=", R.drawable.food1));
        menuItems.add(new MenuItem("Burns", "Tsh.1000/=", R.drawable.food2));
        menuItems.add(new MenuItem("Cooked Banana", "Tsh.5500/=", R.drawable.food3));
        menuItems.add(new MenuItem("Coconut Beans ", "Tsh.2000/=", R.drawable.food4));
        menuItems.add(new MenuItem("Meet Stew", "Tsh.4000/=", R.drawable.food2));
        menuItems.add(new MenuItem("Roasted Beef", "Tsh.6500/=", R.drawable.food3));
        menuItems.add(new MenuItem("pilau with Flied Chicken", "Tsh.8000/=", R.drawable.food5));
        menuItems.add(new MenuItem("French frise with Chicken", "Tsh.6500/=", R.drawable.food3));
        menuItems.add(new MenuItem("Kababu ", "Tsh.5000/=", R.drawable.food1));
        menuItems.add(new MenuItem("Chapati with roasted beef", "Tsh.5000/=", R.drawable.food3));
        menuItems.add(new MenuItem("Coconut rice with Beef", "Tsh.6000/=", R.drawable.food2));
        menuItems.add(new MenuItem("Rhota", "Tsh.3000/=", R.drawable.food1));
        menuItems.add(new MenuItem("Bilyan", "Tsh.10000/=", R.drawable.food4));
        menuItems.add(new MenuItem("Mtori", "Tsh.4500/=", R.drawable.food1));
        // Add more items as needed
        return menuItems;
    }
}
