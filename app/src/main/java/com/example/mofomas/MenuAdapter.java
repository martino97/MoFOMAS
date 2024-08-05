package com.example.mofomas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.mofomas.adapter.MenuItem;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {
    private List<MenuItem> menuItems;
    private OnAddToCartClickListener addToCartListener;

    public interface OnAddToCartClickListener {
        void onAddToCartClick(MenuItem item);
    }

    public MenuAdapter(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public void setOnAddToCartClickListener(OnAddToCartClickListener listener) {
        this.addToCartListener = listener;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuItem menuItem = menuItems.get(position);
        holder.menuName.setText(menuItem.getName());
        holder.menuPrice.setText(menuItem.getPrice());
        Glide.with(holder.itemView.getContext()).load(menuItem.getImageUrl()).into(holder.menuImage);

        holder.addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addToCartListener != null) {
                    addToCartListener.onAddToCartClick(menuItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public void updateMenuItems(List<MenuItem> newMenuItems) {
        menuItems.clear();
        menuItems.addAll(newMenuItems);
        notifyDataSetChanged();
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        public TextView menuName;
        public TextView menuPrice;
        public ImageView menuImage;
        public TextView addToCartButton;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            menuName = itemView.findViewById(R.id.menuView);
            menuPrice = itemView.findViewById(R.id.menuView2);
            menuImage = itemView.findViewById(R.id.menuImage);
            addToCartButton = itemView.findViewById(R.id.menuView1);
        }
    }
}