package com.example.mofomas.admin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mofomas.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminAdapter extends RecyclerView.Adapter<AdminViewHolder> {
    private Context context;

    public AdminAdapter(Context context, List<DataClass> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    private List<DataClass> dataList;

    @NonNull
    @Override
    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item,parent,false);
        return new AdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminViewHolder holder, int position) {
        Glide.with(context).load(dataList.get(position).getFoodImages()).into(holder.recImage);
        holder.recFood.setText(dataList.get(position).getFoodName());
        holder.recCost.setText(dataList.get(position).getFoodAmount());
       // holder.recDesc.setText(dataList.get(position).getFoodDesc());

       holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,DetailActivity.class);
                intent.putExtra("Image",dataList.get(holder.getAdapterPosition()).getFoodImages());
               intent.putExtra("Amount",dataList.get(holder.getAdapterPosition()).getFoodAmount());
                intent.putExtra("Tittle",dataList.get(holder.getAdapterPosition()).getFoodName());

                context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {return dataList.size();}
    public void searchDataList(ArrayList<DataClass>searchList)
    {
        dataList = searchList;
        notifyDataSetChanged();
    }
}
class AdminViewHolder extends RecyclerView.ViewHolder{
CircleImageView recImage;
TextView recFood,recCost,recDesc;
CardView recCard;
    public AdminViewHolder(@NonNull View itemView) {
        super(itemView);
        recImage = itemView.findViewById(R.id.recImage);
        recFood = itemView.findViewById(R.id.textName);
        recCost = itemView.findViewById(R.id.textCost);
        recCard = itemView.findViewById(R.id.recCard);
        //recDesc = itemView.findViewById(R.id.detailDesc);
    }
}
