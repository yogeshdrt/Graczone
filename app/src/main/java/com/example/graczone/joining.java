package com.example.graczone;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.graczone.Wallet.wallet;

public class joining extends AppCompatActivity {

    Dialog dialog;
    Button btnn, join;


    TextView entry, rs_per_kill, rank1, rank2, rank3, teamup, map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joining);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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

        btnn.setOnClickListener(v -> dialog.dismiss());


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.wallet_button_icon, menu);
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

