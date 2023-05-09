package com.example.apphouse.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.example.apphouse.model.House;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class AddHouseActivity extends AppCompatActivity {
    String [] typeHouse = {"Nhà", "Văn phòng", "Chung cư", "Biệt thự"};
    AutoCompleteTextView atuo_Complete_type_hosue;
    ImageView img_back, img_add_image;
    private EditText edt_Title, edt_address, edt_price, edt_bed, edt_bath, edt_wifi, edt_Description;
    private AppCompatButton btn_add_house;
    TextView tv_add_image;
    ArrayAdapter<String> adapter_typeHouse;
    public static final int REQUEST_CODE_ADD_GALLERY = 1;
    private FirebaseAuth auth;
    FirebaseStorage firebaseStorage;
    FirebaseDatabase database;
    private FirebaseUser user;
    private Uri imageUri;
    private LoadingProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_house);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        anhxa();

        adapter_typeHouse = new ArrayAdapter<>(this, R.layout.list_item, typeHouse);
        atuo_Complete_type_hosue.setAdapter(adapter_typeHouse);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddHouseActivity.this, HomeActivity.class));
                finish();
            }
        });

        img_add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImg();
            }
        });

        btn_add_house.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                user = auth.getCurrentUser();
                // Lay id nguoi dung hien tai
                String idUser = user.getUid();

                String title = edt_Title.getText().toString();
                String address = edt_address.getText().toString();
                String price = edt_price.getText().toString();
                String bed = edt_bed.getText().toString();
                String bath = edt_bath.getText().toString();
                String wifi = edt_wifi.getText().toString();
                String typeHouse = atuo_Complete_type_hosue.getText().toString();
                String description = edt_Description.getText().toString();

                if (title.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Tiêu đề không được bỏ trống", Toast.LENGTH_SHORT).show();
                } else if (address.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Địa chỉ không được bỏ trống", Toast.LENGTH_SHORT).show();
                } else if (price.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Giá không được bỏ trống", Toast.LENGTH_SHORT).show();
                } else if (bed.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Phòng ngủ không được để trống", Toast.LENGTH_SHORT).show();
                } else if (bath.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Phòng tắm không được để trống", Toast.LENGTH_SHORT).show();
                } else if (wifi.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Wifi không được bỏ trống", Toast.LENGTH_SHORT).show();
                } else if (description.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Miêu tả không được bỏ trống", Toast.LENGTH_SHORT).show();
                } else if (imageUri == null){
                    Toast.makeText(getApplicationContext(), "Hình ảnh không được bỏ trống", Toast.LENGTH_SHORT).show();
                } else {

                    dialog.ShowDilag("Đang đăng bài viết...");
                    final StorageReference reference = firebaseStorage.getReference().child("images/")
                            .child(System.currentTimeMillis()+"");

                    reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Tao node Houses
                                    DatabaseReference housesRef = database.getReference("Houses");
                                    // Tạo khóa tự động và lấy giá trị của tung house con
                                    String idH = housesRef.push().getKey();

                                    House house = new House();
                                    house.setIdHouse(idH);
                                    house.setIdUser(idUser);
                                    house.setTitle(title);
                                    house.setAddress(address);
                                    house.setPrice(price);
                                    house.setBed(bed);
                                    house.setBath(bath);
                                    house.setWifi(wifi);
                                    house.setTypeHouse(typeHouse);
                                    house.setDescription(description);
                                    house.setImageUrl(uri.toString());
                                    housesRef.child(idH).setValue(house) // Tao house con va idH trung voi id house con
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    dialog.HideDialog();
                                                    startActivity(new Intent(AddHouseActivity.this, HomeActivity.class));
                                                    finishAffinity();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    dialog.HideDialog();
                                                    Toast.makeText(AddHouseActivity.this, "Add House Fail", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            });
                        }
                    });

                }

            }
        });
    }
    private void anhxa(){
        atuo_Complete_type_hosue = findViewById(R.id.atuo_Complete_type_hosue);
        img_back = findViewById(R.id.img_back);
        img_add_image = findViewById(R.id.img_add_image);
        tv_add_image = findViewById(R.id.tv_add_image);
        edt_Title = findViewById(R.id.edt_Title);
        edt_address = findViewById(R.id.edt_address);
        edt_price = findViewById(R.id.edt_price);
        edt_bed = findViewById(R.id.edt_bed);
        edt_bath = findViewById(R.id.edt_bath);
        edt_wifi = findViewById(R.id.edt_wifi);
        btn_add_house = findViewById(R.id.add_house);
        edt_Description = findViewById(R.id.edt_Description);
        dialog = new LoadingProgressDialog(this);

    }

    private void uploadImg(){
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, REQUEST_CODE_ADD_GALLERY);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();

                    }
                }).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_GALLERY && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            Glide.with(this)
                    .load(imageUri)
                    .into(img_add_image);
            tv_add_image.setVisibility(View.GONE);

        }
    }


}