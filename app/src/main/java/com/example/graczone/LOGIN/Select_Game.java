package com.example.graczone.LOGIN;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.graczone.R;
import com.example.graczone.home;

public class Select_Game extends AppCompatActivity {

    CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_game);

        cardView = findViewById(R.id.Game_Battlegrounds);
        cardView.setOnClickListener(v -> {
            Intent intent = new Intent(Select_Game.this, home.class);
            Log.d("myTag", "click select game");
            startActivity(intent);
            finish();
        });

        Log.d("myTag", "click select game");


    }
}
