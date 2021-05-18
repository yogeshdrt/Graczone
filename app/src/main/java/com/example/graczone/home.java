package com.example.graczone;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.graczone.Wallet.wallet;
import com.example.graczone.ui.Settings.Settings_Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class home extends AppCompatActivity {

    Dialog dialog;
    TextView get_username, get_email;
    View hview;

    FirebaseAuth mauth;
    FirebaseUser currentUser;
    String username, arg1, arg2, arg3;


    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mauth = FirebaseAuth.getInstance();
        currentUser = mauth.getCurrentUser();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("MyNotifications", "MyNotifications", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }




        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "welcome";
                        if (!task.isSuccessful()) {
                            msg = "Failed";
                        }
                        Toast.makeText(home.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.getMenu().findItem(R.id.logout).setOnMenuItemClickListener(MenuItem -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(home.this, LoginActivity.class));
            finish();
            return true;
        });

        navigationView.getMenu().findItem(R.id.nav_settings).setOnMenuItemClickListener(MenuItem -> {
            Settings_Fragment sf = new Settings_Fragment();
            Bundle bundle = new Bundle();
            FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Settings_Fragment sf = new Settings_Fragment();
                    Bundle bundle = new Bundle();
                    arg1 = snapshot.child("username").getValue().toString();
                    arg2 = snapshot.child("email").getValue().toString();
                    arg3 = snapshot.child("Phone").getValue().toString();

                    bundle.putString("arg1", arg1);
                    bundle.putString("arg2", arg2);
                    bundle.putString("arg3", arg3);
                    sf.setArguments(bundle);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.FrameContainer, sf);
                    ft.addToBackStack(null);
                    ft.commit();
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    navigationView.getMenu().getItem(3).setChecked(false);
//                    Toast.makeText(getApplicationContext(), "success fetch data" + arg3, Toast.LENGTH_SHORT).show();


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "failed to fetch data", Toast.LENGTH_SHORT).show();
                }
            });
            return true;
        });
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_mymatches, R.id.nav_notification)
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


    public void popUp(){
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.wallet_button_icon, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.wallet_button) {
            startActivity(new Intent(home.this, wallet.class));
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
}