package com.example.apphouse.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.apphouse.activity.Profile_Activity;
import com.example.apphouse.R;
import com.example.apphouse.activity.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;


public class FragmentSetting extends Fragment {
    private View view;
    private TextView thong_tin_ca_nhan, dang_xuat;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        anhxa();
        click_Listene();
        return view;
    }
    private void anhxa(){
        thong_tin_ca_nhan = view.findViewById(R.id.thong_tin_ca_nhan);
        dang_xuat = view.findViewById(R.id.dang_xuat);
    }
    private void click_Listene(){
        dang_xuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext().getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        thong_tin_ca_nhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext().getApplicationContext(), Profile_Activity.class);
                startActivity(intent);
            }
        });

    }


}
