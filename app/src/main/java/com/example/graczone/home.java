package com.example.graczone;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
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
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.graczone.LOGIN.LoginActivity;
import com.example.graczone.LOGIN.NetworkChangeListener;
import com.example.graczone.Wallet.wallet;
import com.example.graczone.ui.MyMatches.MyMatchesModel;
import com.example.graczone.ui.MyMatches.MyMatches_Fragment;
import com.example.graczone.ui.My_Profile.My_Profile_Fragment;
import com.example.graczone.ui.Notification.NotificationModel;
import com.example.graczone.ui.Notification.Notification_Fragment;
import com.example.graczone.ui.feedback.Feedback_Fragment;
import com.example.graczone.ui.joiningRule.Joining_Rules_Fragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Objects;

public class home extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs";
    DrawerLayout drawer;
    TextView get_username, get_email;
    View hview;

    FirebaseAuth mauth;
    FirebaseUser currentUser;
    DatabaseReference databaseReference;
    NavigationView navigationView;
    String username, arg1, arg2, arg3;
    ArrayList<NotificationModel> modelArrayList;
    ArrayList<MyMatchesModel> myMatchesModels;
    ProgressDialog progressDialog;


    private AppBarConfiguration mAppBarConfiguration;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        try {
            progressDialog = new ProgressDialog(home.this);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_dialog);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
        } catch (Exception e) {
            Log.d("myTag", "error in dialog");
        }

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            Log.d("myTag", "get extra not null");
//            String body = getIntent().getExtras().getString("body");
//            if (body != null && body.length() > 0) {
//                getIntent().removeExtra("body");
//
//            }

//            if (extras.getString("notify").equals("true")) {

//            NotificationModel notificationModel = new NotificationModel(getIntent().getStringExtra("title"),
//                    getIntent().getStringExtra("body"), getIntent().getStringExtra("time")
//                    , getIntent().getStringExtra("date"));
//            currentUser = FirebaseAuth.getInstance().getCurrentUser();
//            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid()).child("Notifications");
//            Log.d("myTag", "after reference");
//            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
//                    int flag = 0;
//
//                    for (DataSnapshot child : snapshot.getChildren()) {
//                        if (Objects.equals(child.child("date").getValue(), notificationModel.getDate()) && Objects.equals(child.child("time").getValue(), notificationModel.getTime())) {
//                            flag = 1;
//                        }
//
//                    }
//
//
//                    if (flag == 0) {
//                        if (getIntent().getStringExtra("status").equals("public")) {
//                            for (DataSnapshot child : snapshot.getChildren()) {
//                                FirebaseDatabase.getInstance().getReference("Users").child(Objects.requireNonNull(child.getKey())).child("Notifications")
//                                        .push().setValue(notificationModel);
//
//                            }
//                        } else {
//                            databaseReference.child(notificationModel.getDate() + "+" + notificationModel.getTime()).setValue(notificationModel);
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull @NotNull DatabaseError error) {
//
//                }
//            });


            navigationView.getMenu().performIdentifierAction(R.id.nav_notification, 0);
            Log.d("myTag", "not new intent");

        }

        mauth = FirebaseAuth.getInstance();
        currentUser = mauth.getCurrentUser();


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
                });


//        drawer = findViewById(R.id.drawer_layout);
//        navigationView = findViewById(R.id.nav_view);

        navigationView.getMenu().findItem(R.id.logout).setOnMenuItemClickListener(MenuItem -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(home.this, LoginActivity.class));
            finish();
            return true;
        });

        navigationView.getMenu().findItem(R.id.nav_myprofile).setOnMenuItemClickListener(MenuItem -> {

            try {
                progressDialog = new ProgressDialog(home.this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                progressDialog.setCanceledOnTouchOutside(false);
            } catch (Exception e) {
                Log.d("myTag", "error in dialog");
            }

            FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid())
                    .addValueEventListener(new ValueEventListener() {
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
//
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
                                getSupportFragmentManager().popBackStack();

                            }
                            ft.replace(R.id.nav_host_fragment, pf, "myProfileTag");
                            ft.addToBackStack(null);
                            ft.commit();
                            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                            navigationView.getMenu().getItem(3).setChecked(true);
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getApplicationContext(), "failed to fetch data", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                    });
            return true;
        });

        navigationView.getMenu().findItem(R.id.nav_notification).setOnMenuItemClickListener(MenuItem -> {

            FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid()).child("Notifications")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Log.d("myTag", "snapshot notification");
                            Bundle bundle = new Bundle();
                            Notification_Fragment nf = new Notification_Fragment();
                            modelArrayList = new ArrayList<>();
                            for (DataSnapshot child : snapshot.getChildren()) {
                                Log.d("myTag", "in forloop notification");
                                String body = Objects.requireNonNull(child.child("body").getValue()).toString();
                                String title = Objects.requireNonNull(child.child("title").getValue()).toString();
                                String date = Objects.requireNonNull(child.child("date").getValue()).toString();
                                String time = Objects.requireNonNull(child.child("time").getValue()).toString();
                                NotificationModel notificationModel = new NotificationModel(title, body, time, date);
                                Log.d("myTag", "in add notification model");
                                modelArrayList.add(notificationModel);
                            }
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
                            bundle.putSerializable("models", modelArrayList);
                            nf.setArguments(bundle);
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
                                getSupportFragmentManager().popBackStack();
//                        Log.d("myTag", "open Fragment id: " + getSupportFragmentManager().getBackStackEntryAt(i).getClass() + " name: " +
//                                getSupportFragmentManager().getBackStackEntryAt(i).getName());

                            }
                            ft.replace(R.id.nav_host_fragment, nf);
                            ft.addToBackStack(null);
                            ft.commit();
                            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                            navigationView.getMenu().getItem(2).setChecked(true);
                            Toast.makeText(getApplicationContext(), "successfully add notification data", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.d("myTag", "show database error in notification");

                        }
                    });
//            Bundle bundle = new Bundle();
//            Notification_Fragment nf = new Notification_Fragment();
//            SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
//            Gson gson = new Gson();
//            if (sharedPreferences.contains("models")) {
//
//                String json = sharedPreferences.getString("models", null);
//                Type type = new TypeToken<ArrayList<NotificationModel>>() {
//                }.getType();
//                modelArrayList = gson.fromJson(json, type);
//            } else {
//                modelArrayList = new ArrayList<>();
////                NotificationModel notificationModel = new NotificationModel("default", "default", "default", "default");
////                modelArrayList.add(notificationModel);
//            }
//            bundle.putSerializable("models", modelArrayList);
//            nf.setArguments(bundle);
////            fragment = getSupportFragmentManager().findFragmentById(R.id.profileFragment);
////            if (fragment != null) {
////                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
////            }
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
//                getSupportFragmentManager().popBackStack();
//                Log.d("myTag", "open Fragment id: " + getSupportFragmentManager().getBackStackEntryAt(i).getClass() + " name: " +
//                        getSupportFragmentManager().getBackStackEntryAt(i).getName());
//
//            }
//            ft.replace(R.id.nav_host_fragment, nf, "notificationTag");
//            ft.addToBackStack(null);
//            ft.commit();
//            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//            navigationView.getMenu().getItem(2).setChecked(true);
            Log.d("myTag", "noti. open fragment: " + getSupportFragmentManager().getBackStackEntryCount());
//            Toast.makeText(getApplicationContext(), "successfully add notification data", Toast.LENGTH_SHORT).show();
            return true;
        });

        navigationView.getMenu().findItem(R.id.nav_mymatches).setOnMenuItemClickListener(MenuItem -> {

            FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid()).child("MyMatches")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Log.d("myTag", "snapshot myMatches");
                            Bundle bundle = new Bundle();
                            MyMatches_Fragment mf = new MyMatches_Fragment();
                            myMatchesModels = new ArrayList<>();
                            for (DataSnapshot child : snapshot.getChildren()) {
                                Log.d("myTag", "in forloop notification");
                                String dateTextView = Objects.requireNonNull(child.child("dateTextView").getValue()).toString();
                                String timeTextView = Objects.requireNonNull(child.child("timeTextView").getValue()).toString();
                                String entryFeeTextView = Objects.requireNonNull(child.child("entryFeeTextView").getValue()).toString();
                                String killTextView = Objects.requireNonNull(child.child("killTextView").getValue()).toString();
                                String mapTextView = Objects.requireNonNull(child.child("mapTextView").getValue()).toString();
                                String rank1TextView = Objects.requireNonNull(child.child("rank1TextView").getValue()).toString();
                                String rank2TextView = Objects.requireNonNull(child.child("rank2TextView").getValue()).toString();
                                String rank2lTextView = Objects.requireNonNull(child.child("rank2lTextView").getValue()).toString();
                                String rank3lTextView = Objects.requireNonNull(child.child("rank3lTextView").getValue()).toString();
                                String rank3TextView = Objects.requireNonNull(child.child("rank3TextView").getValue()).toString();
                                String teamUp = Objects.requireNonNull(child.child("teamUp").getValue()).toString();
                                String match = Objects.requireNonNull(child.child("match").getValue()).toString();
                                MyMatchesModel myMatchesModel = new MyMatchesModel(mapTextView, timeTextView, dateTextView, entryFeeTextView, killTextView, rank1TextView, rank2TextView, rank3TextView, rank2lTextView, rank3lTextView, teamUp, match);
                                Log.d("myTag", "in add notification model");
                                myMatchesModels.add(myMatchesModel);
                            }
                            bundle.putSerializable("myMatchModels", myMatchesModels);
                            mf.setArguments(bundle);
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
                                getSupportFragmentManager().popBackStack();

                            }
                            ft.replace(R.id.nav_host_fragment, mf);
                            ft.addToBackStack(null);
                            ft.commit();
                            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                            navigationView.getMenu().getItem(1).setChecked(true);
//                            Toast.makeText(getApplicationContext(), "successfully add myMatches data", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.d("myTag", "show database error in notification");

                        }
                    });

//            Bundle bundle = new Bundle();
//            MyMatches_Fragment mf = new MyMatches_Fragment();
//            SharedPreferences sharedPreferences = getSharedPreferences("myMatchesPre", MODE_PRIVATE);
//            Gson gson = new Gson();
//            if (sharedPreferences.contains("myMatchModels")) {
//
//                String json = sharedPreferences.getString("myMatchModels", null);
//                Type type = new TypeToken<ArrayList<MyMatchesModel>>() {
//                }.getType();
//                myMatchesModels = gson.fromJson(json, type);
//            } else {
//                myMatchesModels = new ArrayList<>();
//            }
//            bundle.putSerializable("myMatchModels", myMatchesModels);
//            mf.setArguments(bundle);
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
//                getSupportFragmentManager().popBackStack();
//                Log.d("myTag", "open Fragment id: " + getSupportFragmentManager().getBackStackEntryAt(i).getClass() + " name: " +
//                        getSupportFragmentManager().getBackStackEntryAt(i).getName());
//
//            }
//            ft.replace(R.id.nav_host_fragment, mf, "myMatchTag");
//            ft.addToBackStack(null);
//            ft.commit();
//            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//            navigationView.getMenu().getItem(1).setChecked(true);
//            Log.d("myTag", "my match open fragment: " + getSupportFragmentManager().getBackStackEntryCount());
//            Toast.makeText(getApplicationContext(), "successfully add myMatches data", Toast.LENGTH_SHORT).show();
            return true;
        });

        navigationView.getMenu().findItem(R.id.feedback).setOnMenuItemClickListener(item -> {

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
                getSupportFragmentManager().popBackStack();
            }
            fragmentTransaction.replace(R.id.nav_host_fragment, new Feedback_Fragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            navigationView.getMenu().getItem(5).setChecked(true);
            return true;
        });

        navigationView.getMenu().findItem(R.id.joining_rules).setOnMenuItemClickListener(item -> {

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
                getSupportFragmentManager().popBackStack();
            }
            fragmentTransaction.replace(R.id.nav_host_fragment, new Joining_Rules_Fragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            navigationView.getMenu().getItem(4).setChecked(true);
            return true;
        });


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

        hview = navigationView.getHeaderView(0);
        get_username = hview.findViewById(R.id.get_username);
        get_email = hview.findViewById(R.id.get_email);
        FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        username = snapshot.child("username").getValue().toString();
                        get_username.setText(username);
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "failed to fetch data", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                });
        get_email.setText(currentUser.getEmail());
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    public void onNewIntent(Intent intent) {
        //called when a new intent for this class is created.
        // The main case is when the app was in background, a notification arrives to the tray, and the user touches the notification

        super.onNewIntent(intent);

        Log.d("myTag", "onNewIntent - starting");
        Bundle extras = intent.getExtras();
        if (extras != null) {

            Log.d("myTag", "out of notify" + extras.getString("notify"));

//            if (extras.getString("notify").equals("true")) {
//                Log.d("myTag", "pass notify");
//
//                NotificationModel notificationModel = new NotificationModel(extras.getString("title"),
//                        extras.getString("body"), extras.getString("time")
//                        , extras.getString("date"));
//                currentUser = FirebaseAuth.getInstance().getCurrentUser();
//                databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid()).child("Notifications");
//                Log.d("myTag", "after reference");
//                if (databaseReference == null) {
//
//                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
//                            int flag = 0;
//
//                            for (DataSnapshot child : snapshot.getChildren()) {
//                                if (Objects.equals(child.child("date").getValue(), notificationModel.getDate()) && Objects.equals(child.child("time").getValue(), notificationModel.getTime())) {
//                                    flag = 1;
//                                }
//
//                            }
//
//
//                            if (flag == 0) {
//                                if (getIntent().getStringExtra("status").equals("public")) {
//                                    for (DataSnapshot child : snapshot.getChildren()) {
//                                        FirebaseDatabase.getInstance().getReference("Users").child(Objects.requireNonNull(child.getKey())).child("Notifications")
//                                                .push().setValue(notificationModel);
//
//                                    }
//                                } else {
//                                    databaseReference.push().setValue(notificationModel);
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull @NotNull DatabaseError error) {
//
//                            Log.d("myTag", "database error");
//
//                        }
//                    });
//                } else {
//                    databaseReference.push().setValue(notificationModel);
//                }
//            }
            navigationView.getMenu().performIdentifierAction(R.id.nav_notification, 0);
            Log.d("myTag", "in click manually");
        }
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
            progressDialog.dismiss();
            super.onBackPressed();


            if (getSupportFragmentManager().findFragmentById(R.id.myMatchesFragment) == null) {
                navigationView.getMenu().getItem(1).setChecked(false);
            }
            if (getSupportFragmentManager().findFragmentByTag("notyFrag") == null) {
                navigationView.getMenu().getItem(2).setChecked(false);
                Log.d("myTag", "check nf fragment null");
            }
            navigationView.getMenu().getItem(3).setChecked(false);
            navigationView.getMenu().getItem(4).setChecked(false);
            navigationView.getMenu().getItem(5).setChecked(false);
        }


    }

    @Override
    protected void onStart() {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, intentFilter);
        Log.d("myTag", "call on start");
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        Log.d("myTag", "call on stop");
        super.onStop();
    }
}