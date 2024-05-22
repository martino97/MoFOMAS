package com.example.mofomas.Fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mofomas.R;
import com.example.mofomas.adapter.CartItem;
import com.example.mofomas.adapter.CartItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class Cart extends Fragment {

    private RecyclerView recyclerView;
    private CartItemAdapter adapter;
    private List<CartItem> cartItemList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerView = view.findViewById(R.id.cartrecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        cartItemList = new ArrayList<>();
        // Add some dummy data for testing
        cartItemList.add(new CartItem("Tea", 1, R.drawable.food1));
        cartItemList.add(new CartItem("Burns", 2, R.drawable.food2));
        cartItemList.add(new CartItem("Cooked Banana", 1, R.drawable.food3));
        cartItemList.add(new CartItem("pilau", 3, R.drawable.food4));
        cartItemList.add(new CartItem("Coconut rice with Peas", 4, R.drawable.food5));
        cartItemList.add(new CartItem("Stef porridge", 1, R.drawable.food4));
        cartItemList.add(new CartItem("Coconut beans", 2, R.drawable.food5));
        cartItemList.add(new CartItem("Meat Stew", 3, R.drawable.food3));
        cartItemList.add(new CartItem("Vegetable Rice", 7, R.drawable.food2));
        cartItemList.add(new CartItem("Juice", 2, R.drawable.food1));

        adapter = new CartItemAdapter(cartItemList, getContext());
        recyclerView.setAdapter(adapter);

        return view;
    }
}
