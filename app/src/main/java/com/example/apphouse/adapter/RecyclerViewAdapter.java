package com.example.apphouse.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apphouse.R;
import com.example.apphouse.activity.DetailActivity;
import com.example.apphouse.model.House;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.PopularViewHolder>{
    private Context context;
    private List<House> mHouseList;

    public RecyclerViewAdapter(List<House> mHouseList) {
        this.mHouseList = mHouseList;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<House> list){
        this.mHouseList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PopularViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view_popular, parent, false);
        return new PopularViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularViewHolder holder, int position) {
        House house = mHouseList.get(position);
        if (house == null){
            return;
        }
        Picasso.get().load(house.getImageUrl()).placeholder(R.drawable.pic1).into(holder.img_popular);
        holder.tv_title_popular.setText(house.getTitle());
        holder.tv_adress.setText(house.getAddress());
        holder.tv_price.setText(house.getPrice());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetailActivity.class);
                intent.putExtra("idHouse", house.getIdHouse());
                view.getContext().startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        if (mHouseList != null){
            return mHouseList.size();
        }
        return 0;
    }

    public class PopularViewHolder extends RecyclerView.ViewHolder{
        private ImageView img_popular;
        private TextView tv_title_popular, tv_adress, tv_price;
        ConstraintLayout constraintLayout;

        public PopularViewHolder(@NonNull View itemView) {
            super(itemView);
            img_popular = itemView.findViewById(R.id.imageView_popular);
            tv_title_popular = itemView.findViewById(R.id.textView_title_popular);
            tv_adress = itemView.findViewById(R.id.textView_adress);
            tv_price = itemView.findViewById(R.id.textView_price);
            constraintLayout = itemView.findViewById(R.id.RecyclerView_item);
        }
    }
}
