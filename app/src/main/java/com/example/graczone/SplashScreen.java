package com.example.graczone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.graczone.LOGIN.LoginActivity;
import com.example.graczone.ui.Notification.NotificationModel;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class SplashScreen extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs";
    ArrayList<NotificationModel> modelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (getIntent() != null && getIntent().hasExtra("title")) {
            String title = getIntent().getExtras().getString("title");
            String body = getIntent().getExtras().getString("body");
            String time = getIntent().getExtras().getString("time");
            String date = getIntent().getExtras().getString("date");
            NotificationModel notification = new NotificationModel(title, body, time, date);
            SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            if (!sharedPreferences.contains("models")) {
                modelArrayList = new ArrayList<>();
            } else {
                String json = sharedPreferences.getString("models", null);
                Type type = new TypeToken<ArrayList<NotificationModel>>() {
                }.getType();
                modelArrayList = gson.fromJson(json, type);
            }
            modelArrayList.add(notification);
            String json1 = gson.toJson(modelArrayList);
            editor.putString("models", json1);
            editor.apply();
        } else {
            Toast.makeText(getApplicationContext(), "failed to fetch data from bac. not.", Toast.LENGTH_SHORT).show();
        }

        Thread thread = new Thread(() -> {
            try {
                sleep(1000);
                Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(intent);
                finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }
}