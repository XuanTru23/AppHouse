package com.example.apphouse.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apphouse.R;
import com.example.apphouse.activity.HomeActivity;
import com.example.apphouse.activity.LoadingProgressDialog;
import com.example.apphouse.model.Like;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecyclerViewLikeAdapter extends RecyclerView.Adapter<RecyclerViewLikeAdapter.UserViewHolder>{
    ArrayList<Like> list;
    private LoadingProgressDialog dialog;
    public RecyclerViewLikeAdapter(ArrayList<Like> list) {
        this.list = list;
    }


    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_like, parent,false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Like like = list.get(position);


        holder.tv_name.setText(like.getName());
        holder.tv_tieude_house.setText(like.getTitle_like());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idLike = like.getId_like();
                dialog = new LoadingProgressDialog(view.getContext());

                final Dialog dialog1 = new Dialog(view.getContext());
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.setContentView(R.layout.activity_detail_like);
                Window window = dialog1.getWindow();
                if (window == null){
                    return;
                }
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                WindowManager.LayoutParams layoutParams = window.getAttributes();
                layoutParams.gravity = Gravity.CENTER;
                window.setAttributes(layoutParams);

                AppCompatButton btn_huy = dialog1.findViewById(R.id.btn_huy);
                AppCompatButton btn_xoa = dialog1.findViewById(R.id.btn_xoa);

                TextView tv_name = dialog1.findViewById(R.id.tv_name);
                TextView tv_sdt = dialog1.findViewById(R.id.tv_sdt);
                TextView tv_tieude_house = dialog1.findViewById(R.id.tv_tieude_house);
                TextView tv_diachi = dialog1.findViewById(R.id.tv_diachi);
                TextView tv_gia = dialog1.findViewById(R.id.tv_gia);

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Like");
                Query query = databaseReference.orderByChild("id_like").equalTo(idLike);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            DataSnapshot dataSnapshot = snapshot.getChildren().iterator().next();
                            Like like1 = dataSnapshot.getValue(Like.class);
                            tv_name.setText(like1.getName());
                            tv_sdt.setText(like1.getSdt());
                            tv_tieude_house.setText(like1.getTitle_like());
                            tv_diachi.setText(like1.getDia_chi());
                            tv_gia.setText(like1.getGia());

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                btn_huy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.dismiss();
                    }
                });

                btn_xoa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setTitle("Xóa");
                        builder.setMessage("Bạn có muốn xóa yêu cầu liên hệ này không!");
                        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference reference = database.getReference("Like").child(idLike);
                                dialog.ShowDilag("Đang xóa...");
                                reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        dialog.HideDialog();
                                        dialog1.dismiss();
                                        Intent intent = new Intent(view.getContext(), HomeActivity.class);
                                        view.getContext().startActivity(intent);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        dialog.HideDialog();
                                        dialog1.dismiss();
                                    }
                                });
                            }
                        });
                        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();

                            }
                        });
                        builder.create();
                        builder.show();

                    }
                });
                dialog1.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_tieude_house, tv_name;
        private ImageView img_delete;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_tieude_house = itemView.findViewById(R.id.tv_tieude_house);
            tv_name = itemView.findViewById(R.id.name);
        }
    }
}
