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
import com.example.mofomas.adapter.PopularAdapter;

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

        adapter = new CartItemAdapter(cartItemList, getContext());
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void addItemToCart(CartItem cartItem) {
        if (!cartItemList.contains(cartItem)) {
            cartItemList.add(cartItem);
            adapter.notifyDataSetChanged();
        }
    }
}