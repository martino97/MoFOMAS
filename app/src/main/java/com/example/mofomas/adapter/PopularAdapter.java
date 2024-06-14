package com.example.mofomas.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mofomas.R;

import java.util.List;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.PopularViewHolder> {

    private static final String TAG = "PopularAdapter";
    private Context context;
    private List<PopularItem> popularItemList;
    private OnAddToCartClickListener onAddToCartClickListener;

    public PopularAdapter(Context context, List<PopularItem> popularItemList, OnAddToCartClickListener onAddToCartClickListener) {
        this.context = context;
        this.popularItemList = popularItemList;
        this.onAddToCartClickListener = onAddToCartClickListener;
    }

    @NonNull
    @Override
    public PopularViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.popular_item, parent, false);
        return new PopularViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularViewHolder holder, @SuppressLint("RecyclerView") int position) {
        PopularItem item = popularItemList.get(position);
        holder.foodName.setText(item.getFoodName());
        holder.foodPrice.setText(item.getFoodPrice());
        Glide.with(context).load(item.getImageUrl()).into(holder.foodImage);

        // Add click listener for "Add to Cart" button
        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Add to Cart clicked for item: " + item.getFoodName());
                if (onAddToCartClickListener != null) {
                    onAddToCartClickListener.onAddToCartClick(item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return popularItemList.size();
    }

    public static class PopularViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView foodImage;
        TextView foodName;
        TextView foodPrice;
        TextView addToCart;

        public PopularViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            foodImage = itemView.findViewById(R.id.imageView4);
            foodName = itemView.findViewById(R.id.textView2);
            foodPrice = itemView.findViewById(R.id.textView5);
            addToCart = itemView.findViewById(R.id.textView4);
        }
    }

    //This is an interface that retrieve item from the popular item to Cart
    public interface OnAddToCartClickListener {
        void onAddToCartClick(PopularItem item);
    }
}
