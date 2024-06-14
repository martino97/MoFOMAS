package com.example.mofomas.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mofomas.ConfirmOrder;
import com.example.mofomas.R;
import com.example.mofomas.adapter.CartItem;
import com.example.mofomas.adapter.CartItemAdapter;
import com.example.mofomas.adapter.PopularAdapter;
import com.example.mofomas.adapter.PopularItem;
import com.example.mofomas.adapter.CartManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.util.List;

public class Cart extends Fragment implements PopularAdapter.OnAddToCartClickListener {

    Button confirm;
    private static final String TAG = "CartFragment";
    private RecyclerView recyclerView;
    private CartItemAdapter adapter;
    private List<CartItem> cartItemList;
    private String currentUserId; // Add this to manage current user

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Cart fragment created");
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        confirm = view.findViewById(R.id.Cart);
        recyclerView = view.findViewById(R.id.cartrecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Retrieve current user email
        currentUserId = getCurrentUserEmail();

        // Load cart items for the current user from CartManager
        cartItemList = CartManager.getInstance(getContext()).getCartItemList(currentUserId);

        adapter = new CartItemAdapter(cartItemList, getContext());
        recyclerView.setAdapter(adapter);

        // Check if the fragment has received an item to add to the cart
        if (getArguments() != null) {
            PopularItem item = getArguments().getParcelable("item");
            if (item != null) {
                Log.d(TAG, "onCreateView: Received item - " + item.getFoodName());
                addItemToCart(item);
            }
        }

        // Set up confirm button click listener
        confirm.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ConfirmOrder.class);
            String cartItemsJson = new Gson().toJson(cartItemList);
            intent.putExtra("cartItems", cartItemsJson);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onAddToCartClick(PopularItem popularItem) {
        Log.d(TAG, "onAddToCartClick: Item clicked - " + popularItem.getFoodName());
        addItemToCart(popularItem);
    }

    public void addItemToCart(PopularItem popularItem) {
        CartItem cartItem = new CartItem(popularItem.getFoodName(), popularItem.getFoodPrice(), popularItem.getImageUrl(), 1);
        Log.d(TAG, "addItemToCart: Adding item - " + popularItem.getFoodName());
        if (!cartItemList.contains(cartItem)) {
            cartItemList.add(cartItem);
            Log.d(TAG, "addItemToCart: Item added - " + popularItem.getFoodName());
            adapter.notifyDataSetChanged();

            // Save to shared preferences for the current user
            CartManager.getInstance(getContext()).addItemToCart(currentUserId, cartItem);
        } else {
            Log.d(TAG, "addItemToCart: Item already in cart - " + popularItem.getFoodName());
        }
    }

    public void refreshCartItems() {
        cartItemList.clear();
        cartItemList.addAll(CartManager.getInstance(getContext()).getCartItemList(currentUserId));
        adapter.notifyDataSetChanged();
    }

    // Method to get the current user email
    private String getCurrentUserEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getEmail();
        } else {
            return "guest"; // Fallback or handle the case when user is not logged in
        }
    }
}
