package com.example.mofomas.adapter;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.mofomas.adapter.CartItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static final String PREF_NAME = "CartPreferences";
    private static CartManager instance;
    private SharedPreferences sharedPreferences;
    private Gson gson;

    private CartManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public static synchronized CartManager getInstance(Context context) {
        if (instance == null) {
            instance = new CartManager(context.getApplicationContext());
        }
        return instance;
    }

    public void addItemToCart(String userId, CartItem item) {
        List<CartItem> cartItemList = getCartItemList(userId);
        if (!cartItemList.contains(item)) {
            cartItemList.add(item);
        }
        saveCartItemList(userId, cartItemList);
    }

    public List<CartItem> getCartItemList(String userId) {
        String json = sharedPreferences.getString(userId, "");
        Type type = new TypeToken<ArrayList<CartItem>>() {}.getType();
        List<CartItem> cartItemList = gson.fromJson(json, type);
        return cartItemList != null ? cartItemList : new ArrayList<>();
    }

    public void saveCartItemList(String userId, List<CartItem> cartItemList) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = gson.toJson(cartItemList);
        editor.putString(userId, json);
        editor.apply();
    }

    public void clearCart(String userId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(userId);
        editor.apply();
    }
}
