package com.example.graczone;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;

import java.util.HashMap;
import java.util.regex.Pattern;

public class create_account extends AppCompatActivity {

    EditText username, email, password, confirm_password;

    Button btn_register;

    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_create_account);


        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_register = findViewById(R.id.btn_register);
        confirm_password = findViewById(R.id.confirm_password);

        auth = FirebaseAuth.getInstance();
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String txt_username = username.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                String txt_confirm_password = confirm_password.getText().toString();

                if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password) || TextUtils.isEmpty((txt_confirm_password))) {
                    Toast.makeText(create_account.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(txt_email).matches()) {
                    email.setError("email address is not valid!");
                } else if (txt_password.length() < 8) {
                    Toast.makeText(create_account.this, "password must be at least 8 characters", Toast.LENGTH_SHORT).show();
                } else if (txt_password.length() > 20) {
                    Toast.makeText(create_account.this, "password length not greater than 20", Toast.LENGTH_SHORT).show();
                } else if (!txt_password.equals(txt_confirm_password)) {
                    Toast.makeText(create_account.this, "new password and confirm password mismatch!", Toast.LENGTH_SHORT).show();
                } else {
                    String regex = "^(?=.*[0-9])"
                            + "(?=.*[a-z])(?=.*[A-Z])"
                            + "(?=.*[@#$%^&+=])"
                            + "(?=\\S+$).{8,20}$";
                    //Pattern pattern = Pattern.compile(regex);
                    if (!Pattern.compile(regex).matcher(txt_password).matches()) {
                        password.setError("Your Password is weak!\n password must contain atleast 1 lower and 1 upper case alphabet\n,1 numeric, 1 special character");
                    } else {
                        register(txt_username, txt_email, txt_password);
                    }

//                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
//                    Intent intent = new Intent(create_account.this, home.class);
//                    intent.putExtra("username", username.getText().toString());
//                    startActivity(intent);

                }
            }
        });
    }

    private void register(String username, String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", username);
                            hashMap.put("imageURL", "default");

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NotNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(create_account.this, home.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "not able to save this data!\n please register again.", Toast.LENGTH_SHORT).show();
                                    }

                                }

                            });
                        } else {
                            Toast.makeText(create_account.this, "you failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}