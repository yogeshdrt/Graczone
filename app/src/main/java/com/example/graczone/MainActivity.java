package com.example.graczone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class MainActivity extends AppCompatActivity {
    TextView create_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        create_account = findViewById(R.id.create_account);
        create_account.setOnClickListener(this::create_account);

    }

    private void create_account(View view) {
        Intent intent = new Intent(MainActivity.this, create_account.class);
        startActivity(intent);
    }
}