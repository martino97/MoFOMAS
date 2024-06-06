package com.example.mofomas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mofomas.adapter.MenuItem;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MenuBottomSheetFragment extends BottomSheetDialogFragment {

    private RecyclerView recyclerView;
    private MenuAdapter menuAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_bottom_sheet, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Example data for the menu
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

        menuAdapter = new MenuAdapter(menuItems);
        recyclerView.setAdapter(menuAdapter);
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
