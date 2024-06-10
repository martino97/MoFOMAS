package com.example.mofomas.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mofomas.R;
import com.example.mofomas.admin.PackageModel;

import java.util.List;
public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.PackageViewHolder> {

    private List<PackageModel> packageList;

    public PackageAdapter(List<PackageModel> packageList) {
        this.packageList = packageList;
    }

    @Override
    public PackageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_package, parent, false);
        return new PackageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PackageViewHolder holder, int position) {
        PackageModel packageModel = packageList.get(position);
        holder.textViewCategory.setText(packageModel.getSelectedCategory());
        holder.textViewFullName.setText(packageModel.getFullName());
        holder.textViewPhoneNumber.setText(packageModel.getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return packageList.size();
    }

    public class PackageViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewCategory;
        public TextView textViewFullName;
        public TextView textViewPhoneNumber;

        public PackageViewHolder(View itemView) {
            super(itemView);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
            textViewFullName = itemView.findViewById(R.id.textViewFullName);
            textViewPhoneNumber = itemView.findViewById(R.id.textViewPhoneNumber);
        }
    }
}