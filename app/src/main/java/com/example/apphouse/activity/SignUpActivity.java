package com.example.apphouse.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.apphouse.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {
    private AppCompatButton btn_sign_up;
    private TextView tv_sign_in;
    private EditText edt_name, edt_email, edt_pass;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private LoadingProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        anhxa();
        auth = FirebaseAuth.getInstance();

        Click_Listener();
    }
    private void anhxa(){
        btn_sign_up = findViewById(R.id.btn_sign_up);
        tv_sign_in =findViewById(R.id.tv_sign_in);
        edt_name = findViewById(R.id.edt_name);
        edt_email = findViewById(R.id.edt_email);
        edt_pass = findViewById(R.id.edt_pass);
        dialog = new LoadingProgressDialog(this);
        reference = FirebaseDatabase.getInstance().getReference("User");

    }
    private void Click_Listener(){
        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_account();
            }
        });

        tv_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void create_account(){
        String email = edt_email.getText().toString();
        String pass = edt_pass.getText().toString();
        String name = edt_name.getText().toString();

        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "Tên không được để trống!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Email không được để trống!", Toast.LENGTH_SHORT).show();
        } else if (!email.contains("@gmail.com")) {
            Toast.makeText(this, "Email không đúng định dạng!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Mật khẩu không được để trống!", Toast.LENGTH_SHORT).show();
        } else if (pass.length() < 6) {
            Toast.makeText(this, "Mật khẩu phải từ 6 ký tự trở lên!", Toast.LENGTH_SHORT).show();
        } else {
            dialog.ShowDilag("Đang tạo tài khoản...");
            auth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                dialog.HideDialog();
                                FirebaseUser user = auth.getCurrentUser();
                                reference = FirebaseDatabase.getInstance().getReference("User").child(user.getUid());
                                if (user!=null){
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("name", name);
                                    hashMap.put("email", email);
                                    hashMap.put("id", user.getUid());
                                    hashMap.put("imageUrl", "default");
                                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                                finishAffinity();
                                            }
                                        }
                                    });
                                }

                            } else {
                                // If sign in fails, display a message to the user.
                                dialog.HideDialog();
                                Toast.makeText(SignUpActivity.this, "Tạo tài khoản thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}