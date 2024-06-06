package com.example.mofomas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mofomas.adapter.MenuItem;
import java.util.ArrayList;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {
    private List<MenuItem> menuItems;
    private List<MenuItem> filteredMenuItems;

    public MenuAdapter(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
        this.filteredMenuItems = new ArrayList<>(menuItems);
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuItem menuItem = filteredMenuItems.get(position);
        holder.menuName.setText(menuItem.getName());
        holder.menuPrice.setText(menuItem.getPrice());
        holder.menuImage.setImageResource(menuItem.getImageResource());
    }

    @Override
    public int getItemCount() {
        return filteredMenuItems.size();
    }

    public void filter(String query) {
        filteredMenuItems.clear();
        if (query.isEmpty()) {
            filteredMenuItems.addAll(menuItems);
        } else {
            for (MenuItem item : menuItems) {
                if (item.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredMenuItems.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        public TextView menuName;
        public TextView menuPrice;
        public ImageView menuImage;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            menuName = itemView.findViewById(R.id.menuView);
            menuPrice = itemView.findViewById(R.id.menuView2);
            menuImage = itemView.findViewById(R.id.menuImage);
        }
    }
}
