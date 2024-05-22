package com.example.mofomas.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mofomas.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.PopularViewHolder> {

    private Context context;
    private List<PopularItem> popularItemList;

    public PopularAdapter(Context context, List<PopularItem> popularItemList) {
        this.context = context;
        this.popularItemList = popularItemList;
    }

    @NonNull
    @Override
    public PopularViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.popular_item, parent, false);
        return new PopularViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularViewHolder holder, int position) {
        PopularItem item = popularItemList.get(position);
        holder.foodName.setText(item.getFoodName());
        holder.foodPrice.setText(item.getFoodPrice());
        holder.foodImage.setImageResource(item.getImageResource());
    }

    @Override
    public int getItemCount() {
        return popularItemList.size();
    }

    public static class PopularViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        CircleImageView foodImage;
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
}
