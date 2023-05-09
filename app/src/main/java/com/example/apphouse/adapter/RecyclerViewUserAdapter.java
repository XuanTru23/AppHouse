package com.example.apphouse.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apphouse.activity.DetailUserActivity;
import com.example.apphouse.R;
import com.example.apphouse.model.House;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerViewUserAdapter extends RecyclerView.Adapter<RecyclerViewUserAdapter.UserViewHolder>{
    ArrayList<House> list;

    private String previousTypeHouse;

    public RecyclerViewUserAdapter(ArrayList<House> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view_house, parent,false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        House house = list.get(position);

        Picasso.get().load(house.getImageUrl()).placeholder(R.drawable.pic1).into(holder.img_house);
        holder.tv_title.setText(house.getTitle());
        holder.tv_adress.setText(house.getAddress());
        holder.tv_type_house.setText(house.getTypeHouse());
        holder.tv_price.setText(house.getPrice());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetailUserActivity.class);
                intent.putExtra("idHouse", house.getIdHouse());
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{

        private ImageView img_house;
        private TextView tv_title, tv_adress, tv_price, tv_type_house;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            img_house = itemView.findViewById(R.id.img_house);
            tv_title = itemView.findViewById(R.id.tv_tittle);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_adress = itemView.findViewById(R.id.tv_adress);
            tv_type_house = itemView.findViewById(R.id.tv_type_house);
        }
    }
}
