package com.example.apphouse.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.example.apphouse.R;
import com.example.apphouse.model.House;
import com.example.apphouse.model.Like;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DetailActivity extends AppCompatActivity {

    private TextView tv_tieude, tv_gia, tv_diachi, tv_bed, tv_bath, tv_wifi, tv_description;
    private ImageView imgHinhanh;
    private AppCompatButton btn_save;
    private LoadingProgressDialog dialog;
    private String id_nguoi_dang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        anhxa();

        String idHouse = getIntent().getStringExtra("idHouse");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Houses");

        // Lay thong tin cua item house duoc chon qua idHouse
        Query query = databaseReference.orderByChild("idHouse").equalTo(idHouse);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    DataSnapshot dataSnapshot = snapshot.getChildren().iterator().next();
                    House house = dataSnapshot.getValue(House.class);
                    id_nguoi_dang = house.getIdUser();
                    tv_tieude.setText(house.getTitle());
                    tv_diachi.setText(house.getAddress());
                    tv_gia.setText(house.getPrice());
                    tv_bed.setText(house.getBed());
                    tv_bath.setText(house.getBath());
                    tv_wifi.setText(house.getWifi());
                    tv_description.setText(house.getDescription());
                    String a = house.getImageUrl();
                    Glide.with(DetailActivity.this).load(a).into(imgHinhanh);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog1 = new Dialog(DetailActivity.this);
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.setContentView(R.layout.activity_like);
                Window window = dialog1.getWindow();
                if (window == null){
                    return;
                }
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                WindowManager.LayoutParams layoutParams = window.getAttributes();
                layoutParams.gravity = Gravity.CENTER;
                window.setAttributes(layoutParams);

                EditText edt_name = dialog1.findViewById(R.id.edt_name);
                EditText edt_sdt = dialog1.findViewById(R.id.edt_sdt);
                AppCompatButton btn_huy = dialog1.findViewById(R.id.btn_huy);
                AppCompatButton btn_gui = dialog1.findViewById(R.id.btn_gui);

                btn_huy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.dismiss();
                    }
                });

                btn_gui.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        
                        if (edt_name.getText().toString().isEmpty()){
                            Toast.makeText(DetailActivity.this, "Họ và tên không được bỏ trống!", Toast.LENGTH_SHORT).show();
                        } else if (edt_sdt.getText().toString().isEmpty()){
                            Toast.makeText(DetailActivity.this, "Số điện thoại không được bỏ trống!", Toast.LENGTH_SHORT).show();
                        } else if (edt_sdt.getText().toString().length() <= 8 ||  edt_sdt.getText().toString().length() >= 11){
                            Toast.makeText(DetailActivity.this, "Số điện thoại phải gồm 9 hoặc 10 số!", Toast.LENGTH_SHORT).show();
                        } else {
                            dialog.ShowDilag("Đang gửi yêu cầu liên hệ");
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Like");
                            String idHoueLike = reference.push().getKey();
                            Like like = new Like();
                            like.setId_like(idHoueLike);
                            like.setId_nguoi_dang(id_nguoi_dang);
                            like.setTitle_like(tv_tieude.getText().toString());
                            like.setDia_chi(tv_diachi.getText().toString());
                            like.setGia(tv_gia.getText().toString());
                            like.setSdt(edt_sdt.getText().toString());
                            like.setName(edt_name.getText().toString());
                            reference.child(idHoueLike).setValue(like).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    dialog.HideDialog();
                                    dialog1.dismiss();
                                }
                            });
                        }


                    }
                });

                dialog1.show();

            }
        });

    }
    private void anhxa(){
        imgHinhanh = findViewById(R.id.img_hinhanh);
        tv_tieude = findViewById(R.id.tv_tieude);
        tv_gia = findViewById(R.id.tv_gia);
        tv_diachi = findViewById(R.id.tv_diachi);
        tv_bed = findViewById(R.id.tv_bed);
        tv_wifi = findViewById(R.id.tv_wifi);
        tv_bath = findViewById(R.id.tv_bath);
        tv_wifi = findViewById(R.id.tv_wifi);
        btn_save = findViewById(R.id.btn_Save);
        tv_description = findViewById(R.id.tv_description);
        dialog = new LoadingProgressDialog(this);
    }

}