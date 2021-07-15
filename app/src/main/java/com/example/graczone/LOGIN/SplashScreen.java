package com.example.graczone.LOGIN;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.graczone.R;
import com.example.graczone.home;
import com.example.graczone.ui.Notification.NotificationModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class SplashScreen extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs";
    ArrayList<NotificationModel> modelArrayList;


    FirebaseUser firebaseUser;
    Intent intent;
    DatabaseReference databaseReference;
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;
    int flag;
    String status;
    NotificationModel notification;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();


        Thread thread = new Thread(this::run);
        thread.start();

        if (networkInfo == null) {
            Toast.makeText(getApplicationContext(), "make sure your device is connected to internet", Toast.LENGTH_LONG).show();
        }

    }


    private void run() {
        try {
            sleep(1000);
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            if (networkInfo != null && firebaseUser != null) {
                intent = new Intent(SplashScreen.this, home.class);

            } else {
                intent = new Intent(SplashScreen.this, signInWithGoogleActivity.class);
            }

            startActivity(intent);
            finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}