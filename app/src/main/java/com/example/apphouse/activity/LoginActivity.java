package com.example.apphouse.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

public class LoginActivity extends AppCompatActivity {
    private AppCompatButton btn_login;
    private TextView tv_forgot_password, tv_sign_up;
    private EditText edt_email, edt_pass;
    private FirebaseAuth auth;
    private LoadingProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        anhxa();
        Click_Listener();

    }
    private void anhxa(){
        btn_login = findViewById(R.id.btn_login);
        tv_forgot_password = findViewById(R.id.tv_forgot_password);
        tv_sign_up = findViewById(R.id.tv_sign_up);
        edt_email = findViewById(R.id.edt_email);
        edt_pass = findViewById(R.id.edt_pass);
        dialog = new LoadingProgressDialog(this);
    }
    private void Click_Listener(){
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        tv_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        tv_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogforgot_password();
            }
        });
    }

    private void login(){
        String email = edt_email.getText().toString();
        String pass = edt_pass.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Email không được để trống!", Toast.LENGTH_SHORT).show();
        } else if (!email.contains("@gmail.com")) {
            Toast.makeText(this, "Email không đúng định dạng!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Mật khẩu không được để trống!", Toast.LENGTH_SHORT).show();
        } else if (pass.length() < 6) {
            Toast.makeText(this, "Mật khẩu phải từ 6 ký tự trở lên!", Toast.LENGTH_SHORT).show();
        } else {
            dialog.ShowDilag("Đang đăng nhập...");
            auth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            dialog.HideDialog();
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finishAffinity();
                            } else {
                                dialog.HideDialog();
                                // If sign in fails, display a message to the user.
                                Toast.makeText(LoginActivity.this, "Tài khoản mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void openDialogforgot_password(){
        final Dialog dialog1 = new Dialog(this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.activity_forgot_password);
        Window window = dialog1.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);

        EditText edt_email_pass = dialog1.findViewById(R.id.edt_email_pass);
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
                if (edt_email_pass.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Email nhận link đặt mật khẩu không được bỏ trống", Toast.LENGTH_SHORT).show();
                } else if (!edt_email_pass.getText().toString().contains("@gmail.com")) {
                    Toast.makeText(getApplicationContext(), "Email không đúng định dạng!", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    dialog.ShowDilag("Dang gui");
                    auth.sendPasswordResetEmail(edt_email_pass.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        dialog.HideDialog();
                                        dialog1.dismiss();
                                    }
                                }
                            });
                }


            }
        });

        dialog1.show();

    }

}