package com.example.graczone;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.graczone.Wallet.wallet;
import com.example.graczone.ui.MyMatches.MyMatchesModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.reflect.TypeToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class joining extends AppCompatActivity {

    Dialog dialog;
    Button btnn, join;


    TextView entry, rs_per_kill, rank1, rank2, rank3, teamup, map;
    String s6, time, date, s1, s2, s3, s4, s5, s7;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    ArrayList<MyMatchesModel> myMatchesModels;


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joining);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Drawable drawable = toolbar.getNavigationIcon();
        assert drawable != null;
        drawable.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_ATOP);

        entry = findViewById(R.id.get_entry_fee);
        rs_per_kill = findViewById(R.id.get_rs_per_kill);
        rank1 = findViewById(R.id.get_rank1);
        rank2 = findViewById(R.id.get_rank2);
        rank3 = findViewById(R.id.get_rank3);
        map = findViewById(R.id.get_map);
        teamup = findViewById(R.id.get_teamup);

        Intent intent = getIntent();
        s1 = intent.getStringExtra("entry_fee");
        s2 = intent.getStringExtra("rs_per_kill");
        s3 = intent.getStringExtra("rank1");
        s4 = intent.getStringExtra("rank2");
        s5 = intent.getStringExtra("rank3");
        s6 = intent.getStringExtra("teamup");
        s7 = intent.getStringExtra("map");
        time = intent.getStringExtra("time");
        date = intent.getStringExtra("date");

        entry.setText(s1);
        rs_per_kill.setText(s2);
        rank1.setText(s3);
        rank2.setText(s4);
        rank3.setText(s5);
        teamup.setText(s6);
        map.setText(s7);


        //  String k = extractDigits(s);


        dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_window);

        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.background));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);


        join = findViewById(R.id.join_btn);
        join.setOnClickListener(v -> dialog.show());


        // Inflate the layout for this fragment

        btnn = dialog.findViewById(R.id.popup_confirm);


        final EditText editText = dialog.findViewById(R.id.enter_battlegrounds_id);
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                editText.setHint("");
            }
        });
        btnn.setOnClickListener(v -> {
                    if (editText.getText().toString().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "enter valid pubg id", Toast.LENGTH_SHORT).show();
                    } else {
                        FirebaseMessaging.getInstance().subscribeToTopic("match1").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    String email = firebaseUser.getEmail();
                                    if (email == null) {
                                        email = "null";
                                        Log.d("myTag", "email null");
                                    }
                                    databaseReference = FirebaseDatabase.getInstance().getReference(s6).child(date + "+" + time);
                                    databaseReference.child("EntryFee").setValue(s1);
                                    databaseReference.child("participants").child(editText.getText().toString()).child("email")
                                            .setValue(email).addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            Log.d("myTag", "succ. add participant");
                                        } else {
                                            Log.d("myTag", "error in participant");
                                        }
                                    });
                                    Log.d("myTag", "add id in joining");
                                    saveMyMatches(s1, s2, s7, time, date, s3, s4, s5);
                                    Toast.makeText(getApplicationContext(), "successfully joined", Toast.LENGTH_SHORT).show();
                                    join.setEnabled(false);
                                    join.setText("JOINED");
                                    join.setTextColor(Color.GRAY);
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
        );


    }

    void saveMyMatches(String s1, String s2, String s7, String time, String date, String s3, String s4, String s5) {

        MyMatchesModel myMatchesModel = new MyMatchesModel(s7, time, date, s1, s2, s3, s4, s5);
        SharedPreferences sharedPreferences = getSharedPreferences("myMatchesPre", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        if (!sharedPreferences.contains("myMatchModels")) {
            myMatchesModels = new ArrayList<>();
        } else {
            String json = sharedPreferences.getString("myMatchModels", null);
            Type type = new TypeToken<ArrayList<MyMatchesModel>>() {
            }.getType();
            myMatchesModels = gson.fromJson(json, type);
        }
        myMatchesModels.add(0, myMatchesModel);
        String json1 = gson.toJson(myMatchesModels);
        editor.putString("myMatchModels", json1);
        editor.apply();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.wallet_button_icon, menu);

        Drawable mydrawable = menu.getItem(0).getIcon();
        mydrawable.mutate();
        mydrawable.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.wallet_button) {
            startActivity(new Intent(joining.this, wallet.class));
            return true;
        }
        return false;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

  /*  public String extractDigits(String src) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < src.length(); i++) {
            char c = src.charAt(i);
            if (Character.isDigit(c)) {
                builder.append(c);
            }
        }
        return builder.toString();
    }*/


}

