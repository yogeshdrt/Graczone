package com.example.graczone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.graczone.LOGIN.LoginActivity;
import com.example.graczone.ui.Notification.NotificationModel;
import com.google.common.reflect.TypeToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class SplashScreen extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs";
    ArrayList<NotificationModel> modelArrayList;


    FirebaseUser firebaseUser;
    Intent intent;
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        if (getIntent() != null && getIntent().hasExtra("title")) {
            String title = getIntent().getExtras().getString("title");
            String body = getIntent().getExtras().getString("body");
            String time = getIntent().getExtras().getString("time");
            String date = getIntent().getExtras().getString("date");
            if (body != null && body.length() > 0) {
                getIntent().removeExtra("body");
                saveNotification(title, body, time, date);
            }
//            NotificationModel notification = new NotificationModel(title, body, time, date);
//            SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            Gson gson = new Gson();
//            if (!sharedPreferences.contains("models")) {
//                modelArrayList = new ArrayList<>();
//            } else {
//                String json = sharedPreferences.getString("models", null);
//                Type type = new TypeToken<ArrayList<NotificationModel>>() {
//                }.getType();
//                modelArrayList = gson.fromJson(json, type);
//            }
//            modelArrayList.add(notification);
//            String json1 = gson.toJson(modelArrayList);
//            editor.putString("models", json1);
//            editor.apply();
        }

        Thread thread = new Thread(() -> {
            try {
                sleep(4000);
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                if (networkInfo != null && firebaseUser != null) {
                    intent = new Intent(SplashScreen.this, home.class);
                } else {
                    intent = new Intent(SplashScreen.this, LoginActivity.class);
                }

                startActivity(intent);
                finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();

        if (networkInfo == null) {
            Toast.makeText(getApplicationContext(), "make sure your device is connected to internet", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onNewIntent(Intent intent) {
        //called when a new intent for this class is created.
        // The main case is when the app was in background, a notification arrives to the tray, and the user touches the notification

        super.onNewIntent(intent);

        // Log.d("myTag", "onNewIntent - starting");
        Bundle extras = intent.getExtras();
        if (extras != null) {
            for (String key : extras.keySet()) {
                Object value = extras.get(key);
                //Log.d("myTag", "Extras received at onNewIntent:  Key: " + key + " Value: " + value);
            }
            String title = extras.getString("title");
            String body = extras.getString("body");
            String time = extras.getString("time");
            String date = extras.getString("date");
            if (body != null && body.length() > 0) {
                getIntent().removeExtra("body");
                saveNotification(title, body, time, date);
            }
        }
    }

    void saveNotification(String title, String body, String time, String date) {

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
        modelArrayList.add(0, notification);
        String json1 = gson.toJson(modelArrayList);
        editor.putString("models", json1);
        editor.apply();

    }
}