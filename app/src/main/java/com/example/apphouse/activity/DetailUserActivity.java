package com.example.apphouse.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.example.apphouse.R;
import com.example.apphouse.fragment.FragmentUser;
import com.example.apphouse.model.House;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.UUID;

public class DetailUserActivity extends AppCompatActivity {
    String [] typeHouse = {"Nhà", "Văn phòng", "Chung cư", "Biệt thự"};
    ArrayAdapter<String> adapter_typeHouse;
    private EditText edt_Title, edt_address, edt_price, edt_bed, edt_bath, edt_wifi, edt_Description;
    private ImageView img_add_image, img_back;
    private Uri imageUri;
    private AppCompatButton btn_update, btn_delete;
    FirebaseStorage firebaseStorage;
    private AutoCompleteTextView atuo_Complete_type_hosue;
    private LoadingProgressDialog dialog;
    private String a;
    public static final int REQUEST_CODE_ADD_GALLERY = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user);
        anhxa();
        firebaseStorage = FirebaseStorage.getInstance();
        adapter_typeHouse = new ArrayAdapter<>(this, R.layout.list_item, typeHouse);
        atuo_Complete_type_hosue.setAdapter(adapter_typeHouse);

        String idHouse = getIntent().getStringExtra("idHouse");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Houses");
        Query query = databaseReference.orderByChild("idHouse").equalTo(idHouse);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    DataSnapshot dataSnapshot = snapshot.getChildren().iterator().next();
                    House house = dataSnapshot.getValue(House.class);

                    edt_Title.setText(house.getTitle());
                    edt_address.setText(house.getAddress());
                    edt_price.setText(house.getPrice());
                    edt_bed.setText(house.getBed());
                    edt_bath.setText(house.getBath());
                    edt_wifi.setText(house.getWifi());
                    atuo_Complete_type_hosue.setText(house.getTypeHouse());
                    edt_Description.setText(house.getDescription());
                    a = house.getImageUrl();
                    Glide.with(DetailUserActivity.this).load(a).into(img_add_image);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title_house = edt_Title.getText().toString();
                String adress_house = edt_address.getText().toString();
                String price = edt_price.getText().toString();
                String bed_house = edt_bed.getText().toString();
                String bath_house = edt_bath.getText().toString();
                String wifi_house = edt_wifi.getText().toString();
                String Description_house = edt_Description.getText().toString();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Houses").child(idHouse);


                if (title_house.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Tiêu đề không được bỏ trống", Toast.LENGTH_SHORT).show();
                } else if (adress_house.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Địa chỉ không được bỏ trống", Toast.LENGTH_SHORT).show();
                } else if (price.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Giá không được bỏ trống", Toast.LENGTH_SHORT).show();
                } else if (bed_house.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Phòng ngủ không được để trống", Toast.LENGTH_SHORT).show();
                } else if (bath_house.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Phòng tắm không được để trống", Toast.LENGTH_SHORT).show();
                } else if (wifi_house.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Wifi không được bỏ trống", Toast.LENGTH_SHORT).show();
                } else if (Description_house.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Miêu tả không được bỏ trống", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.ShowDilag("Đang cập nhật...");
                    if (imageUri == null){
                        reference.child("title").setValue(title_house);
                        reference.child("address").setValue(adress_house);
                        reference.child("price").setValue(price);
                        reference.child("bed").setValue(bed_house);
                        reference.child("bath").setValue(bath_house);
                        reference.child("wifi").setValue(wifi_house);
                        reference.child("description").setValue(Description_house).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                dialog.HideDialog();
                                startActivity(new Intent(DetailUserActivity.this, HomeActivity.class));
                                finishAffinity();
                            }
                        });
                    } else {
                        final StorageReference reference1 = FirebaseStorage.getInstance().getReference().child("images/" + UUID.randomUUID().toString());
                        reference1.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                reference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        reference.child("title").setValue(title_house);
                                        reference.child("address").setValue(adress_house);
                                        reference.child("price").setValue(price);
                                        reference.child("bed").setValue(bed_house);
                                        reference.child("bath").setValue(bath_house);
                                        reference.child("wifi").setValue(wifi_house);
                                        reference.child("description").setValue(Description_house);
                                        reference.child("imageUrl").setValue(uri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                dialog.HideDialog();
                                                startActivity(new Intent(DetailUserActivity.this, HomeActivity.class));
                                                finishAffinity();
                                            }
                                        });

                                    }
                                });

                            }
                        });

                    }

                }


            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailUserActivity.this);
                builder.setTitle("Xóa");
                builder.setMessage("Bạn có xóa bài viết này không!");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference reference = database.getReference("Houses").child(idHouse);
                        dialog.ShowDilag("Đang xóa...");
                        reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                dialog.HideDialog();
                                startActivity(new Intent(DetailUserActivity.this, HomeActivity.class));
                                finishAffinity();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialog.HideDialog();;
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

        img_add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImg();
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailUserActivity.this, FragmentUser.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void anhxa(){
        edt_Title = findViewById(R.id.edt_Title);
        edt_address = findViewById(R.id.edt_address);
        edt_price = findViewById(R.id.edt_price);
        edt_bed = findViewById(R.id.edt_bed);
        edt_bath = findViewById(R.id.edt_bath);
        edt_wifi = findViewById(R.id.edt_wifi);
        edt_Description = findViewById(R.id.edt_Description);
        img_add_image = findViewById(R.id.img_add_image);
        atuo_Complete_type_hosue = findViewById(R.id.atuo_Complete_type_hosue);
        btn_update = findViewById(R.id.btn_update);
        dialog = new LoadingProgressDialog(this);
        btn_delete = findViewById(R.id.btn_delete);
        img_back = findViewById(R.id.img_back);
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


        }
    }

}