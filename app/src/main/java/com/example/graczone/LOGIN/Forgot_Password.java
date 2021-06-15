package com.example.graczone.LOGIN;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.graczone.R;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_Password extends AppCompatActivity {

    Button reset;
    EditText enter_email;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        auth = FirebaseAuth.getInstance();

        enter_email = findViewById(R.id.enter_email_forgot);


        reset = findViewById(R.id.reset_btn);
        reset.setOnClickListener(v -> {
            String email = enter_email.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                Log.d("myTag", "empty email when forgot");
                Toast.makeText(Forgot_Password.this, "Email id required", Toast.LENGTH_SHORT).show();
            } else {
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(Forgot_Password.this, "A password reset link is send to " + email, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(Forgot_Password.this, "invalid email!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }


        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Forgot_Password.this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }



}


