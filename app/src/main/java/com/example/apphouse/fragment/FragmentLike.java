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
import com.example.apphouse.adapter.RecyclerViewLikeAdapter;
import com.example.apphouse.model.Like;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FragmentLike extends Fragment {
    private View view;
    private RecyclerView recyclerViewLike;
    private RecyclerViewLikeAdapter recyclerViewLikeAdapter;
    private ArrayList<Like> list;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private FirebaseUser user;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_like, container, false);
        anhxa();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        list = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), recyclerViewLike.VERTICAL, false);
        recyclerViewLike.setLayoutManager(linearLayoutManager);
        recyclerViewLikeAdapter = new RecyclerViewLikeAdapter(list);
        recyclerViewLike.setAdapter(recyclerViewLikeAdapter);

        user = auth.getCurrentUser();
        // Lay id nguoi dung hien tai
        String idUser = user.getUid();
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Like");
        databaseRef.orderByChild("id_nguoi_dang").equalTo(idUser).addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Like like = snapshot.getValue(Like.class);
                if (like != null){
                    list.add(like);
                    recyclerViewLikeAdapter.notifyDataSetChanged();
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Like like = snapshot.getValue(Like.class);
                if (list == null || list.isEmpty()){
                    return;
                }

                for (int i = 0; i < list.size(); i++){
                    if (like.getId_like() == list.get(i).getId_like()){
                        list.set(i, like);
                        break;
                    }
                }
                recyclerViewLikeAdapter.notifyDataSetChanged();
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Like like = snapshot.getValue(Like.class);
                if (like == null || list == null || list.isEmpty()){
                    return;
                }
                for (int i = 0; i < list.size(); i++){
                    if (like.getId_like() == list.get(i).getId_like()){
                        list.remove(list.get(i));
                        break;
                    }
                }
                recyclerViewLikeAdapter.notifyDataSetChanged();
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
        recyclerViewLike = view.findViewById(R.id.recyclerLike);
    }


}
