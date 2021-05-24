package com.example.graczone.Wallet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.graczone.R;

public class wallet extends AppCompatActivity {

    Button btn2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        btn2 = findViewById(R.id.withdraw_btn);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(wallet.this, withdraw.class);
                startActivity(intent);
            }
        });

        Log.d("myTag", "work in wallet");

    }


    @Override
    public void onBackPressed(){
        super.onBackPressed();
        this.finish();
}
    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
}

}