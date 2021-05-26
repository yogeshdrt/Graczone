package com.example.graczone;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.graczone.LOGIN.LoginActivity;
import com.example.graczone.Wallet.wallet;
import com.example.graczone.ui.MyMatches.MyMatchesModel;
import com.example.graczone.ui.MyMatches.MyMatches_Fragment;
import com.example.graczone.ui.My_Profile.My_Profile_Fragment;
import com.example.graczone.ui.Notification.NotificationModel;
import com.example.graczone.ui.Notification.Notification_Fragment;
import com.google.android.material.navigation.NavigationView;
import com.google.common.reflect.TypeToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class home extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs";
    DrawerLayout drawer;
    TextView get_username, get_email;
    View hview;

    FirebaseAuth mauth;
    FirebaseUser currentUser;
    NavigationView navigationView;
    String username, arg1, arg2, arg3;
    ArrayList<NotificationModel> modelArrayList;
    ArrayList<MyMatchesModel> myMatchesModels;
    Fragment fragment;


    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //FirebaseMessaging.getInstance().setAutoInitEnabled(true);


        mauth = FirebaseAuth.getInstance();
        currentUser = mauth.getCurrentUser();

        Log.d("myTag", "open fragment: " + getSupportFragmentManager().getBackStackEntryCount());


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("MyNotifications", "MyNotifications", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }


        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(task -> {
                    String msg = "";
                    if (!task.isSuccessful()) {
                        msg = "Failed";
                    }
                    //Toast.makeText(home.this, msg, Toast.LENGTH_SHORT).show();
                });


        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        navigationView.getMenu().findItem(R.id.logout).setOnMenuItemClickListener(MenuItem -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(home.this, LoginActivity.class));
            finish();
            return true;
        });

        navigationView.getMenu().findItem(R.id.nav_myprofile).setOnMenuItemClickListener(MenuItem -> {
            FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    My_Profile_Fragment pf = new My_Profile_Fragment();
                    Bundle bundle = new Bundle();
                    arg1 = snapshot.child("username").getValue().toString();
                    arg2 = snapshot.child("email").getValue().toString();
                    arg3 = snapshot.child("Phone").getValue().toString();

                    bundle.putString("arg1", arg1);
                    bundle.putString("arg2", arg2);
                    bundle.putString("arg3", arg3);
                    pf.setArguments(bundle);
//                    fragment = getSupportFragmentManager().findFragmentById(R.id.notificationFragment);
//                    if (fragment != null) {
//                        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
//                    }
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
                        getSupportFragmentManager().popBackStack();
                        Log.d("myTag", "open Fragment id: " + getSupportFragmentManager().getBackStackEntryAt(i).getClass() + " name: " +
                                getSupportFragmentManager().getBackStackEntryAt(i).getName());

                    }
                    ft.replace(R.id.nav_host_fragment, pf, "myProfileTag");
                    ft.addToBackStack(null);
                    ft.commit();
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    navigationView.getMenu().getItem(3).setChecked(true);
                    Log.d("myTag", "prof. open fragment: " + getSupportFragmentManager().getBackStackEntryCount());
//                    Toast.makeText(getApplicationContext(), "success fetch data" + arg3, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "failed to fetch data", Toast.LENGTH_SHORT).show();
                }
            });
            return true;
        });

        navigationView.getMenu().findItem(R.id.nav_notification).setOnMenuItemClickListener(MenuItem -> {

//            FirebaseDatabase.getInstance().getReference("Notifications").addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    Bundle bundle = new Bundle();
//                    Notification_Fragment nf = new Notification_Fragment();
//                    modelArrayList = new ArrayList<>();
//                    for (DataSnapshot child : snapshot.getChildren()) {
//                        body = child.child("body").getValue().toString();
//                        title = child.child("title").getValue().toString();
//                        time = child.child("time").getValue().toString();
//                        date = child.child("date").getValue().toString();
//                        NotificationModel notificationModel = new NotificationModel(title, body, time, date);
//                        modelArrayList.add(notificationModel);
//                    }
//                    SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
//                    Gson gson = new Gson();
//                    if(sharedPreferences.contains("models")) {
//
//                        String json = sharedPreferences.getString("models", null);
//                        Type type = new TypeToken<ArrayList<NotificationModel>>(){}.getType();
//                        modelArrayList = gson.fromJson(json, type);
//                    } else {
//                        modelArrayList = new ArrayList<>();
//                    }
//                    bundle.putSerializable("models", modelArrayList);
//                    nf.setArguments(bundle);
//                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                    ft.replace(R.id.nav_host_fragment, nf);
//                    ft.addToBackStack(null);
//                    ft.commit();
//                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//                    navigationView.getMenu().getItem(2).setChecked(true);
//                    Toast.makeText(getApplicationContext(), "successfully add notification data", Toast.LENGTH_SHORT).show();
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    Toast.makeText(getApplicationContext(), "failed to add data of notification", Toast.LENGTH_SHORT).show();
//
//                }
//            });
            Bundle bundle = new Bundle();
            Notification_Fragment nf = new Notification_Fragment();
            SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
            Gson gson = new Gson();
            if (sharedPreferences.contains("models")) {

                String json = sharedPreferences.getString("models", null);
                Type type = new TypeToken<ArrayList<NotificationModel>>() {
                }.getType();
                modelArrayList = gson.fromJson(json, type);
            } else {
                modelArrayList = new ArrayList<>();
                NotificationModel notificationModel = new NotificationModel("default", "default", "default", "default");
                modelArrayList.add(notificationModel);
            }
            bundle.putSerializable("models", modelArrayList);
            nf.setArguments(bundle);
//            fragment = getSupportFragmentManager().findFragmentById(R.id.profileFragment);
//            if (fragment != null) {
//                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
//            }
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
                getSupportFragmentManager().popBackStack();
                Log.d("myTag", "open Fragment id: " + getSupportFragmentManager().getBackStackEntryAt(i).getClass() + " name: " +
                        getSupportFragmentManager().getBackStackEntryAt(i).getName());

            }
            ft.replace(R.id.nav_host_fragment, nf, "notificationTag");
            ft.addToBackStack(null);
            ft.commit();
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            navigationView.getMenu().getItem(2).setChecked(true);
            Log.d("myTag", "noti. open fragment: " + getSupportFragmentManager().getBackStackEntryCount());
//            Toast.makeText(getApplicationContext(), "successfully add notification data", Toast.LENGTH_SHORT).show();
            return true;
        });

        navigationView.getMenu().findItem(R.id.nav_mymatches).setOnMenuItemClickListener(MenuItem -> {

            Bundle bundle = new Bundle();
            MyMatches_Fragment mf = new MyMatches_Fragment();
            SharedPreferences sharedPreferences = getSharedPreferences("myMatchesPre", MODE_PRIVATE);
            Gson gson = new Gson();
            if (sharedPreferences.contains("myMatchModels")) {

                String json = sharedPreferences.getString("myMatchModels", null);
                Type type = new TypeToken<ArrayList<MyMatchesModel>>() {
                }.getType();
                myMatchesModels = gson.fromJson(json, type);
            } else {
                myMatchesModels = new ArrayList<>();
                MyMatchesModel myMatchesModel = new MyMatchesModel("default", "default", "default", "default", "default", "default", "default", "default");
                myMatchesModels.add(myMatchesModel);
            }
            bundle.putSerializable("myMatchModels", myMatchesModels);
            mf.setArguments(bundle);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            fragment = getSupportFragmentManager().findFragmentById(R.id.myMatchesFragment);
            for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
                getSupportFragmentManager().popBackStack();
                Log.d("myTag", "open Fragment id: " + getSupportFragmentManager().getBackStackEntryAt(i).getClass() + " name: " +
                        getSupportFragmentManager().getBackStackEntryAt(i).getName());

            }
            ft.replace(R.id.nav_host_fragment, mf, "myMatchTag");
            ft.addToBackStack(null);
            ft.commit();
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            navigationView.getMenu().getItem(1).setChecked(true);
            Log.d("myTag", "my match open fragment: " + getSupportFragmentManager().getBackStackEntryCount());
//            Toast.makeText(getApplicationContext(), "successfully add myMatches data", Toast.LENGTH_SHORT).show();
            return true;
        });
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.feedback_Fragment, R.id.joining_Rules_Fragment)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

        hview = navigationView.getHeaderView(0);
        get_username = hview.findViewById(R.id.get_username);
        get_email = hview.findViewById(R.id.get_email);
        FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                username = snapshot.child("username").getValue().toString();
                get_username.setText(username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
        get_email.setText(currentUser.getEmail());
        NavigationUI.setupWithNavController(navigationView, navController);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.wallet_button_icon, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.wallet_button) {
            for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
                getSupportFragmentManager().popBackStack();
                Log.d("myTag", "open Fragment id: " + getSupportFragmentManager().getBackStackEntryAt(i).getClass() + " name: " +
                        getSupportFragmentManager().getBackStackEntryAt(i).getName());

            }
            startActivity(new Intent(home.this, wallet.class));
            Log.d("myTag", "switch to wallet");
            return true;
        }
        return false;
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } else {
            super.onBackPressed();

//            for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
//                getSupportFragmentManager().popBackStack();
//                Log.d("myTag", "open Fragment id: " + getSupportFragmentManager().getBackStackEntryAt(i).getClass() + " name: " +
//                        getSupportFragmentManager().getBackStackEntryAt(i).getName());
//
//            }


            if (getSupportFragmentManager().findFragmentById(R.id.myMatchesFragment) == null) {
                navigationView.getMenu().getItem(1).setChecked(false);
            }
            if (getSupportFragmentManager().findFragmentByTag("notyFrag") == null) {
                navigationView.getMenu().getItem(2).setChecked(false);
                Log.d("myTag", "check nf fragment null");
            }
            navigationView.getMenu().getItem(3).setChecked(false);
        }
        Log.d("myTag", "open fragment: " + getSupportFragmentManager().getBackStackEntryCount());


    }

}