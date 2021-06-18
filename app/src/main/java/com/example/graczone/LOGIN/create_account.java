package com.example.graczone.LOGIN;

import android.app.ProgressDialog;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Pattern;

public class create_account extends AppCompatActivity {

    EditText username, password, confirm_password, phoneEditText;

    Button btn_register;
    ProgressDialog progressDialog;

    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);


        username = findViewById(R.id.usernameTextView);
        password = findViewById(R.id.password);
        btn_register = findViewById(R.id.btn_register);
        confirm_password = findViewById(R.id.confirm_password);
        phoneEditText = findViewById(R.id.phoneEditText);

        auth = FirebaseAuth.getInstance();
        btn_register.setOnClickListener(view -> {

            String txt_username = username.getText().toString();
            String txt_password = password.getText().toString();
            String txt_confirm_password = confirm_password.getText().toString();

            if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_password) || TextUtils.isEmpty((txt_confirm_password))) {
                Toast.makeText(create_account.this, "All fields are required", Toast.LENGTH_SHORT).show();
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
                    Bundle bundle = getIntent().getExtras();
                    if (bundle != null) {
                        String emailSend = bundle.getString("email");
                        Log.d("myTag", "from getIntent");
                        register(txt_username, emailSend, txt_password);
//                                        Toast.makeText(getApplicationContext(), "from intent"+emailSend, Toast.LENGTH_SHORT).show();
                    }
                }

//                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
//                    Intent intent = new Intent(create_account.this, home.class);
//                    intent.putExtra("username", username.getText().toString());
//                    startActivity(intent);

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
        try {

            progressDialog = new ProgressDialog(create_account.this);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_dialog);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
        } catch (Exception e) {
            Log.d("myTag", "error in progress bar in create account");
        }
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
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

                        reference.setValue(hashMap).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "registration successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(create_account.this, Select_Game.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "not able to save this data!\n please register again.", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }

                        });
                    } else {
                        Toast.makeText(create_account.this, "registration failed! ", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
    }
}