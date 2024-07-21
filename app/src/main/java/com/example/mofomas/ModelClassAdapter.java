package com.example.mofomas;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class ModelClassAdapter extends RecyclerView.Adapter<ModelClassAdapter.ModelClassViewHolder> {

    private List<ModelClass> modelList;

    public ModelClassAdapter(List<ModelClass> modelList) {
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ModelClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new ModelClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModelClassViewHolder holder, int position) {
        ModelClass model = modelList.get(position);

        // Set order summary
        holder.orderIdTextView.setText("Order " + (position + 1) + " - " + (model.isConfirmed() ? "Confirmed" : "Delivered"));
        holder.orderDateTextView.setText("Date: " + model.getDate());

        // Set click listener for expansion
        holder.headerLayout.setOnClickListener(v -> {
            model.setExpanded(!model.isExpanded());
            holder.expandableContent.setVisibility(model.isExpanded() ? View.VISIBLE : View.GONE);
            holder.expandCollapseIcon.setRotation(model.isExpanded() ? 180 : 0);
        });

        // Clear previous items
        holder.itemsLayout.removeAllViews();

        // Add items to expandable content
        for (ModelClass.CartItem item : model.getCartItems()) {
            View itemView = LayoutInflater.from(holder.itemView.getContext()).inflate(R.layout.cart_item, holder.itemsLayout, false);

            ImageView itemImage = itemView.findViewById(R.id.imageView4);
            TextView itemName = itemView.findViewById(R.id.textView2);
            TextView itemQuantity = itemView.findViewById(R.id.textView7);
            TextView itemAmount = itemView.findViewById(R.id.textView3);

            Glide.with(holder.itemView.getContext()).load(item.getImageResource()).into(itemImage);
            itemName.setText(item.getName());
            itemQuantity.setText("Quantity: " + item.getQuantity());
            itemAmount.setText(item.getAmount());

            holder.itemsLayout.addView(itemView);
        }
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    static class ModelClassViewHolder extends RecyclerView.ViewHolder {
        TextView orderIdTextView,orderDateTextView,orderStatusTextView,orderTotalTextView;
        ImageView expandCollapseIcon;
        LinearLayout expandableContent, headerLayout, itemsLayout;

        ModelClassViewHolder(View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.orderIdTextView);
            orderDateTextView = itemView.findViewById(R.id.orderDateTextView);
            orderStatusTextView = itemView.findViewById(R.id.orderStatusTextView);
            orderTotalTextView = itemView.findViewById(R.id.orderTotalTextView);
            expandCollapseIcon = itemView.findViewById(R.id.expandCollapseIcon);
            expandableContent = itemView.findViewById(R.id.expandableContent);
            headerLayout = itemView.findViewById(R.id.headerLayout);
            itemsLayout = itemView.findViewById(R.id.itemsLayout);
        }
    }
}