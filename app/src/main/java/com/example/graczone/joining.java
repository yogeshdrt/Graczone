package com.example.graczone;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

public class joining extends AppCompatActivity {

    Dialog dialog;
    Button btnn, join;


    TextView entry, rs_per_kill, rank1, rank2, rank3, teamup, map;


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joining);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        String s1 = intent.getStringExtra("entry_fee");
        String s2 = intent.getStringExtra("rs_per_kill");
        String s3 = intent.getStringExtra("rank1");
        String s4 = intent.getStringExtra("rank2");
        String s5 = intent.getStringExtra("rank3");
        String s6 = intent.getStringExtra("teamup");
        String s7 = intent.getStringExtra("map");

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
                editText.setHint("enter pubg player id");
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
                                    Toast.makeText(getApplicationContext(), "successfully entered in room", Toast.LENGTH_SHORT).show();
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

