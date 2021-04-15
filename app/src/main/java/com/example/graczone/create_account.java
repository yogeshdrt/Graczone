package com.example.graczone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class create_account extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        button = findViewById(R.id.button);
        button.setOnClickListener(this::home);
    }

    private void home(View view) {
        Intent intent = new Intent(create_account.this, home.class);
        startActivity(intent);
    }
}