package com.example.mofomas.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mofomas.R;

import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartViewHolder> {

    private List<CartItem> cartItemList;
    private Context context;

    public CartItemAdapter(List<CartItem> cartItemList, Context context) {
        this.cartItemList = cartItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItemList.get(position);
        holder.foodName.setText(cartItem.getName());
        holder.quantity.setText(String.valueOf(cartItem.getQuantity()));
        holder.imageView.setImageResource(cartItem.getImageResource());

        holder.incrementButton.setOnClickListener(v -> {
            if (cartItem.getQuantity() < 10) {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                notifyItemChanged(position);
            }
        });

        holder.decrementButton.setOnClickListener(v -> {
            if (cartItem.getQuantity() > 1) {
                cartItem.setQuantity(cartItem.getQuantity() - 1);
                notifyItemChanged(position);
            }
        });

        holder.deleteButton.setOnClickListener(v -> {
            cartItemList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartItemList.size());
        });
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView foodName, quantity;
        ImageView imageView;
        ImageButton incrementButton, decrementButton, deleteButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.textView2);
            quantity = itemView.findViewById(R.id.textView7);
            imageView = itemView.findViewById(R.id.imageView4);
            incrementButton = itemView.findViewById(R.id.button);
            decrementButton = itemView.findViewById(R.id.button2);
            deleteButton = itemView.findViewById(R.id.button3);
        }
    }
}