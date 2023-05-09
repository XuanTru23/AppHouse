package com.example.apphouse.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apphouse.R;
import com.example.apphouse.adapter.RecyclerViewUserAdapter;
import com.example.apphouse.model.House;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FragmentUser extends Fragment {
    private View view;
    private RecyclerView recyclerViewUser;
    private RecyclerViewUserAdapter recyclerViewUserAdapter;
    private ArrayList<House> list;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private FirebaseUser user;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user, container, false);
        anhxa();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        list = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), recyclerViewUser.VERTICAL, false);
        recyclerViewUser.setLayoutManager(linearLayoutManager);
        recyclerViewUserAdapter = new RecyclerViewUserAdapter(list);
        recyclerViewUser.setAdapter(recyclerViewUserAdapter);

        user = auth.getCurrentUser();
        // Lay id nguoi dung hien tai
        String idUser = user.getUid();

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Houses");

        databaseRef.orderByChild("idUser").equalTo(idUser).addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                House house = snapshot.getValue(House.class);
                if (house != null){
                    list.add(house);
                    recyclerViewUserAdapter.notifyDataSetChanged();
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                House house = snapshot.getValue(House.class);
                if (list == null || list.isEmpty()){
                    return;
                }

                for (int i = 0; i < list.size(); i++){
                    if (house.getIdHouse() == list.get(i).getIdHouse()){
                        list.set(i, house);
                        break;
                    }
                }
                recyclerViewUserAdapter.notifyDataSetChanged();
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
                recyclerViewUserAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }
    private void anhxa(){
        recyclerViewUser = view.findViewById(R.id.recyclerUser);
    }

}
