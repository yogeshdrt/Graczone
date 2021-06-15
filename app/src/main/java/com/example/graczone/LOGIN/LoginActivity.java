package com.example.graczone.LOGIN;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.graczone.EmailVerificationActivity;
import com.example.graczone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    TextView new_user_create_account;

    EditText email, password;
    Button btn_login;

    TextView forgot_password;

    FirebaseAuth auth;

    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;

    FirebaseUser firebaseUser;

/*    protected void onStart() {
        super.onStart();

        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser!=null){
          Intent intent = new Intent(LoginActivity.this, home.class);
            startActivity(intent);
            finish();
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        new_user_create_account = findViewById(R.id.new_user_create_account);
        new_user_create_account.setOnClickListener(this::LoginActivity);

        auth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email);

        password = findViewById(R.id.password);

        //forgot password
        forgot_password = findViewById(R.id.forgot_password_btn);
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, Forgot_Password.class);
                startActivity(i);
                finish();
            }
        });


        btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                networkInfo = connectivityManager.getActiveNetworkInfo();

                if (networkInfo != null) {

                    String txt_email = email.getText().toString();
                    String txt_password = password.getText().toString();

                    if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {

                        Toast.makeText(LoginActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    } else {

                        auth.signInWithEmailAndPassword(txt_email, txt_password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {

                                            Intent intent = new Intent(LoginActivity.this, Select_Game.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                                            startActivity(intent);
                                            finish();

                                        } else {
                                            Toast.makeText(LoginActivity.this, "Authentication failed!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "please check your internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void LoginActivity(View view) {
        Intent intent = new Intent(LoginActivity.this, EmailVerificationActivity.class);
        startActivity(intent);
        finish();
    }
}