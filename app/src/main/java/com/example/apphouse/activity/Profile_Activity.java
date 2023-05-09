package com.example.apphouse.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.example.apphouse.R;
import com.example.apphouse.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class Profile_Activity extends AppCompatActivity {

    private ImageView img_profile;
    private TextView tv_name, tv_email, tv_img_profile, tv_thay_pass;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference reference;
    public static final int REQUEST_CODE_GALLERY = 1;

    private LoadingProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        auth = FirebaseAuth.getInstance();
        anhxa();
        set_profile();
        click_Listene();

    }

    private void anhxa(){
        tv_email = findViewById(R.id.tv_email);
        tv_name = findViewById(R.id.tv_name);
        tv_img_profile = findViewById(R.id.tv_img_profile);
        img_profile = findViewById(R.id.img_profile);
        tv_thay_pass = findViewById(R.id.tv_thay_pass);
        dialog = new LoadingProgressDialog(this);
    }
    private void click_Listene(){
        tv_img_profile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
        });

        tv_thay_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog_New_password();
            }
        });

    }

    private void set_profile(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("User").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User users = snapshot.getValue(User.class);
                String imageUrl = snapshot.child("imageUrl").getValue(String.class);
                // Sử dụng thư viện Glide để tải và hiển thị ảnh từ URL
                Glide.with(getApplicationContext())
                        .load(imageUrl)
                        .circleCrop()
                        .into(img_profile);
                tv_name.setText(users.getName());
                tv_email.setText(users.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            saveImageToFirebase(imageUri);
        }
    }

    private void saveImageToFirebase(Uri imageUri) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images/" + UUID.randomUUID().toString());
        dialog.ShowDilag("Đang thay đổi ảnh đại diện...");
        storageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        dialog.HideDialog();
                        // Lưu URL của ảnh vào Firebase Realtime Database
                        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("User").child(user.getUid());
                        databaseRef.child("imageUrl").setValue(uri.toString());
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Xử lý khi upload ảnh thất bại
            }
        });
    }


    private void openDialog_New_password(){
        final Dialog dialog1 = new Dialog(this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.activity_new_password);
        Window window = dialog1.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);

        EditText edt_new_pass = dialog1.findViewById(R.id.edt_new_pass);
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
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String newPassword = edt_new_pass.getText().toString();
                if (newPassword.isEmpty()){
                    Toast.makeText(Profile_Activity.this, "Mật khẩu mới không được bỏ trống", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.ShowDilag("Đang cập nhật mật khẩu...");
                    user.updatePassword(newPassword)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        dialog1.dismiss();
                                        dialog.HideDialog();
                                    }
                                }
                            });
                }

            }
        });

        dialog1.show();

    }
}