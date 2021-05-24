package com.example.graczone.LOGIN;

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

import com.example.graczone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;

import java.util.HashMap;
import java.util.regex.Pattern;

public class create_account extends AppCompatActivity {

    EditText username, email, password, confirm_password, phoneEditText;

    Button btn_register;

    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);


        username = findViewById(R.id.usernameTextView);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_register = findViewById(R.id.btn_register);
        confirm_password = findViewById(R.id.confirm_password);
        phoneEditText = findViewById(R.id.phoneEditText);

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
                } else if (phoneEditText.length() > 10 || phoneEditText.length() < 10) {
                    phoneEditText.setError("Invalid Phone Number");
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
                        auth.fetchSignInMethodsForEmail(txt_email).addOnCompleteListener(create_account.this, new OnCompleteListener<SignInMethodQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                if (!task.getResult().getSignInMethods().isEmpty()) {
                                    Toast.makeText(getApplicationContext(), "this email already exist!", Toast.LENGTH_SHORT).show();
                                } else {
                                    register(txt_username, txt_email, txt_password);
                                }
                            }
                        });
                    }

//                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
//                    Intent intent = new Intent(create_account.this, home.class);
//                    intent.putExtra("username", username.getText().toString());
//                    startActivity(intent);

                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(create_account.this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    private void register(String username, String email, String password) {
        String phone = phoneEditText.getText().toString();
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
                            hashMap.put("email", email);
                            hashMap.put("Phone", phone);

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NotNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "registration successfully!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(create_account.this, Select_Game.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "not able to save this data!\n please register again.", Toast.LENGTH_SHORT).show();
                                    }

                                }

                            });
                        } else {
                            Toast.makeText(create_account.this, "registration failed! ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}