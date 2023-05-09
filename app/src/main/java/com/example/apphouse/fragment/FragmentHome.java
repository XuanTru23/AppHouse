package com.example.apphouse.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.apphouse.R;
import com.example.apphouse.adapter.RecyclerViewAdapter;
import com.example.apphouse.model.House;
import com.example.apphouse.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentHome extends Fragment {
    private View view;
    private EditText editText_timkiem;
    private ImageView img_home;
    private RecyclerView recyclerView_Popular;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<House> list;
    private ConstraintLayout ct_house, ct_apartment, ct_villa, ct_office;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference reference;
    private FirebaseDatabase database;
    private TextView tv_name, tv_all_house;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        auth = FirebaseAuth.getInstance();
        anhxa();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        list = new ArrayList<>();

        // Thiet lap hien thi RecyclerView_Popular
        setRecyclerView_Popular();
        set_profile();
        // Hien thi du lieu len RecyclerView va callback du lieu khi bi thay doi
        Click_Listener();
        call_back_house();

        

        

        return view;
    }

    private void anhxa(){
        recyclerView_Popular = view.findViewById(R.id.recyclerViewPopular);
        tv_name = view.findViewById(R.id.tv_name);
        img_home = view.findViewById(R.id.img_home);
        ct_house = view.findViewById(R.id.ct_house);
        ct_apartment = view.findViewById(R.id.ct_apartment);
        ct_office = view.findViewById(R.id.ct_office);
        ct_villa = view.findViewById(R.id.ct_villa);
        tv_all_house = view.findViewById(R.id.tv_all_house);
        editText_timkiem = view.findViewById(R.id.editText);
    }

    private void setRecyclerView_Popular(){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        recyclerView_Popular.setLayoutManager(gridLayoutManager);
        recyclerViewAdapter = new RecyclerViewAdapter(list);
        recyclerView_Popular.setAdapter(recyclerViewAdapter);
    }

    private void set_profile(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("User").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User users = snapshot.getValue(User.class);
                String imageUrl = snapshot.child("imageUrl").getValue(String.class);
                // Hien thi anh len glide
                Glide.with(getContext().getApplicationContext())
                        .load(imageUrl)
                        .circleCrop()
                        .into(img_home);
                tv_name.setText(users.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void Click_Listener(){

        editText_timkiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tu_khoa = editText_timkiem.getText().toString();
                if (tu_khoa == null || tu_khoa.isEmpty()){
                    setRecyclerView_Popular();
                } else {
                    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Houses");
                    databaseRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            List<House> list1 = new ArrayList<>();
                            for (DataSnapshot ds : snapshot.getChildren()){
                                House house = ds.getValue(House.class);
                                if (house.getTitle().contains(tu_khoa)){
                                    list1.add(house);
                                }
                            }
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
                            recyclerView_Popular.setLayoutManager(gridLayoutManager);
                            recyclerViewAdapter = new RecyclerViewAdapter(list1);
                            recyclerView_Popular.setAdapter(recyclerViewAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }
        });
        ct_house.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Houses");
                String type_house = "Nhà";
                databaseRef.orderByChild("typeHouse").equalTo(type_house).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<House> list1 = new ArrayList<>();
                        for (DataSnapshot ds : snapshot.getChildren()){
                            House house = ds.getValue(House.class);
                            list1.add(house);
                        }
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
                        recyclerView_Popular.setLayoutManager(gridLayoutManager);
                        recyclerViewAdapter = new RecyclerViewAdapter(list1);
                        recyclerView_Popular.setAdapter(recyclerViewAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext().getApplicationContext(), "Lấy dữ liệu thất bại!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        ct_villa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Houses");
                String type_house = "Biệt thự";
                databaseRef.orderByChild("typeHouse").equalTo(type_house).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<House> list1 = new ArrayList<>();
                        for (DataSnapshot ds : snapshot.getChildren()){
                            House house = ds.getValue(House.class);
                            list1.add(house);
                        }
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
                        recyclerView_Popular.setLayoutManager(gridLayoutManager);
                        recyclerViewAdapter = new RecyclerViewAdapter(list1);
                        recyclerView_Popular.setAdapter(recyclerViewAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext().getApplicationContext(), "Lấy dữ liệu thất bại!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        ct_office.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Houses");
                String type_house = "Văn phòng";
                databaseRef.orderByChild("typeHouse").equalTo(type_house).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<House> list1 = new ArrayList<>();
                        for (DataSnapshot ds : snapshot.getChildren()){
                            House house = ds.getValue(House.class);
                            list1.add(house);
                        }
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
                        recyclerView_Popular.setLayoutManager(gridLayoutManager);
                        recyclerViewAdapter = new RecyclerViewAdapter(list1);
                        recyclerView_Popular.setAdapter(recyclerViewAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext().getApplicationContext(), "Lấy dữ liệu thất bại!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        ct_apartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Houses");
                String type_house = "Chung cư";
                databaseRef.orderByChild("typeHouse").equalTo(type_house).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<House> list1 = new ArrayList<>();
                        for (DataSnapshot ds : snapshot.getChildren()){
                            House house = ds.getValue(House.class);
                            list1.add(house);
                        }
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
                        recyclerView_Popular.setLayoutManager(gridLayoutManager);
                        recyclerViewAdapter = new RecyclerViewAdapter(list1);
                        recyclerView_Popular.setAdapter(recyclerViewAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext().getApplicationContext(), "Lấy dữ liệu thất bại!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        tv_all_house.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRecyclerView_Popular();
            }
        });
    }

    private void call_back_house(){
        DatabaseReference houseRef = FirebaseDatabase.getInstance().getReference("Houses");
        houseRef.addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                House house = snapshot.getValue(House.class);
                if (house != null){
                    list.add(house);
                    recyclerViewAdapter.notifyDataSetChanged();
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                House house = snapshot.getValue(House.class);
                if (house == null || list.isEmpty()){
                    return;
                }

                for (int i = 0; i < list.size(); i++){
                    if (house.getIdHouse() == list.get(i).getIdHouse()){
                        list.set(i, house);
                        break;
                    }
                }
                recyclerViewAdapter.notifyDataSetChanged();
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                House house = snapshot.getValue(House.class);
                if (house == null || list == null || list.isEmpty()){
                    return;
                }
                for (int i = 0; i < list.size(); i++){
                    if (house.getIdHouse() == list.get(i).getIdHouse()){
                        list.remove(list.get(i));
                        break;
                    }
                }
                recyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
