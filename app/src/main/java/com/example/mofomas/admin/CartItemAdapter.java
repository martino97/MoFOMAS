// CartItemAdapter.java
package com.example.mofomas.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mofomas.R;

import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {

    private Context context;
    private List<CartItem> cartItemList;

    public CartItemAdapter(Context context, List<CartItem> cartItemList) {
        this.context = context;
        this.cartItemList = cartItemList;
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        CartItem cartItem = cartItemList.get(position);
        holder.textViewItemName.setText(cartItem.getName());
        holder.textViewItemQuantity.setText("Quantity: " + cartItem.getQuantity());
        holder.textViewItemAmount.setText("Amount: " + cartItem.getAmount());
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    static class CartItemViewHolder extends RecyclerView.ViewHolder {
        TextView textViewItemName, textViewItemQuantity, textViewItemAmount;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewItemName = itemView.findViewById(R.id.textViewItemName);
            textViewItemQuantity = itemView.findViewById(R.id.textViewItemQuantity);
            textViewItemAmount = itemView.findViewById(R.id.textViewItemAmount);
        }
    }
}
