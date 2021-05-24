package com.example.graczone;

import android.app.Dialog;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.graczone.Wallet.wallet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

public class joining_TDM extends AppCompatActivity {

    Dialog dialog;
    Button btnn, join;
    ImageView vector01, vector02;

    TextView entry, rs_per_kill, rank1, teamup, map;
    String time, date, s1, s6;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joining_tdm);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        vector01 = findViewById(R.id.vector01);
        vector01.setColorFilter(getResources().getColor(R.color.blue));

        vector02 = findViewById(R.id.vector02);
        vector02.setColorFilter(getResources().getColor(R.color.red));


        Drawable drawable = toolbar.getNavigationIcon();
        drawable.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_ATOP);

        entry = findViewById(R.id.get_entry_fee);
        rs_per_kill = findViewById(R.id.get_rs_per_kill);
        rank1 = findViewById(R.id.get_rank1);

        map = findViewById(R.id.get_map);
        teamup = findViewById(R.id.get_teamup);

        Intent intent = getIntent();
        s1 = intent.getStringExtra("entry_fee");
        String s2 = intent.getStringExtra("rs_per_kill");
        String s3 = intent.getStringExtra("rank1");
        s6 = intent.getStringExtra("teamup");
        String s7 = intent.getStringExtra("map");
        time = intent.getStringExtra("time");
        date = intent.getStringExtra("date");

        entry.setText(s1);
        rs_per_kill.setText(s2);
        rank1.setText(s3);
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

        final EditText editText = (EditText) dialog.findViewById(R.id.enter_battlegrounds_id);
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                editText.setHint("enter pubg userName");

        });

//        btnn.setOnClickListener(v -> dialog.dismiss());
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
            startActivity(new Intent(joining_TDM.this, wallet.class));
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

