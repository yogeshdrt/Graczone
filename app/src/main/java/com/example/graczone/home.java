package com.example.graczone;

import android.app.Dialog;
import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import com.example.graczone.LOGIN.DeleteAccountActivity;
import com.example.graczone.LOGIN.NetworkChangeListener;
import com.example.graczone.LOGIN.signInWithGoogleActivity;
import com.example.graczone.Wallet.wallet;
import com.example.graczone.ui.MyMatches.MyMatchesModel;
import com.example.graczone.ui.MyMatches.MyMatches_Fragment;
import com.example.graczone.ui.Notification.NotificationModel;
import com.example.graczone.ui.Notification.Notification_Fragment;
import com.example.graczone.ui.feedback.Feedback_Fragment;
import com.example.graczone.ui.joiningRule.Joining_Rules_Fragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

public class home extends AppCompatActivity {

    DrawerLayout drawer;
    TextView get_username, get_email;
    View hview;

    FirebaseAuth mauth;
    FirebaseUser currentUser;
    NavigationView navigationView;
    ArrayList<NotificationModel> modelArrayList;
    ArrayList<MyMatchesModel> myMatchesModels;
    ProgressDialog progressDialog;
    GoogleSignInClient mGoogleSignInClient;
    Bundle notificationBundle, myMatchesBundle;
    Dialog dialog;
    DownloadManager manager;
    int currentVersionCode, serverVersionCode;


    private AppBarConfiguration mAppBarConfiguration;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findVersionFromServer();

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.delete_account_popup);

        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.background));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        currentVersionCode = BuildConfig.VERSION_CODE;

        if (currentVersionCode < serverVersionCode) {
            dialog.show();
            dialog.findViewById(R.id.yesBtn).setOnClickListener(v -> {

                manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse("https://graczone.netlify.app/app-debug.apk");
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                long reference = manager.enqueue(request);
                Toast.makeText(getApplicationContext(), "downloading starting...", Toast.LENGTH_SHORT).show();

            });
            dialog.findViewById(R.id.noBtn).setOnClickListener(task -> dialog.dismiss());
        } else {
            Log.d("myTag", "currentV: " + currentVersionCode + " " + "seVC " + serverVersionCode);
        }


        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

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


        navigationView.getMenu().findItem(R.id.logout).setOnMenuItemClickListener(MenuItem -> {

            mGoogleSignInClient.signOut();
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(home.this, signInWithGoogleActivity.class));
            finish();
            return true;
        });

        navigationView.getMenu().findItem(R.id.nav_notification).setOnMenuItemClickListener(MenuItem -> {
            progressDialog.show();


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
                                modelArrayList.add(0, notificationModel);
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
                            nf.setArguments(notificationBundle);
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
                                getSupportFragmentManager().popBackStack();
//                        Log.d("myTag", "open Fragment id: " + getSupportFragmentManager().getBackStackEntryAt(i).getClass() + " name: " +
//                                getSupportFragmentManager().getBackStackEntryAt(i).getName());

                            }
                            progressDialog.dismiss();
                            ft.replace(R.id.nav_host_fragment, nf);
                            ft.addToBackStack(null);
                            ft.commit();
                            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                            navigationView.getMenu().getItem(2).setChecked(true);
//                            Toast.makeText(getApplicationContext(), "successfully add notification data", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.d("myTag", "show database error in notification");
                            progressDialog.dismiss();

                        }
                    });
//            Notification_Fragment nf = new Notification_Fragment();
//            nf.setArguments(notificationBundle);
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
//                getSupportFragmentManager().popBackStack();
////                        Log.d("myTag", "open Fragment id: " + getSupportFragmentManager().getBackStackEntryAt(i).getClass() + " name: " +
////                                getSupportFragmentManager().getBackStackEntryAt(i).getName());
//
//            }
//
//            ft.replace(R.id.nav_host_fragment, nf);
//            ft.addToBackStack(null);
//            ft.commit();
//            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//            navigationView.getMenu().getItem(2).setChecked(true);

//            Log.d("myTag", "noti. open fragment: " + getSupportFragmentManager().getBackStackEntryCount());
//            Toast.makeText(getApplicationContext(), "successfully add notification data", Toast.LENGTH_SHORT).show();
            return true;
        });
        navigationView.getMenu().findItem(R.id.nav_mymatches).setOnMenuItemClickListener(MenuItem -> {

            MyMatches_Fragment mf = new MyMatches_Fragment();
            mf.setArguments(myMatchesBundle);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
                getSupportFragmentManager().popBackStack();

            }
            ft.replace(R.id.nav_host_fragment, mf);
            ft.addToBackStack(null);
            ft.commit();
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            navigationView.getMenu().getItem(1).setChecked(true);
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
            navigationView.getMenu().getItem(4).setChecked(true);
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
            navigationView.getMenu().getItem(3).setChecked(true);
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

        get_email.setText(currentUser.getEmail());
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        assert acct != null;
        get_username.setText(acct.getDisplayName());

        NavigationUI.setupWithNavController(navigationView, navController);

        ////Setting_icon-For Delete account///////////////

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        ImageView imageView = headerView.findViewById(R.id.setting_icon);
        imageView.setOnClickListener(v -> {
            for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
                getSupportFragmentManager().popBackStack();
            }
            navigationView.getMenu().getItem(1).setChecked(false);
            navigationView.getMenu().getItem(2).setChecked(false);
            navigationView.getMenu().getItem(3).setChecked(false);
            navigationView.getMenu().getItem(4).setChecked(false);
            navigationView.getMenu().getItem(5).setChecked(false);
            Intent intent = new Intent(home.this, DeleteAccountActivity.class);
            startActivity(intent);
        });

        //add Notification and myMatches
        FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid()).child("MyMatches")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d("myTag", "snapshot myMatches");
                        myMatchesBundle = new Bundle();
                        MyMatches_Fragment mf = new MyMatches_Fragment();
                        myMatchesModels = new ArrayList<>();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            Log.d("myTag", "in forloop my matches");
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
                            Log.d("myTag", "in add my matches model");
                            myMatchesModels.add(0, myMatchesModel);
                        }
                        myMatchesBundle.putSerializable("myMatchModels", myMatchesModels);
                        progressDialog.dismiss();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("myTag", "show database error in myMatches");
                        progressDialog.dismiss();

                    }
                });
        progressDialog.dismiss();


//        progressDialog.dismiss();
//        NavigationUI.setupWithNavController(navigationView, navController);
//
//        ////Setting_icon-For Delete account///////////////
//
//        NavigationView navigationView = findViewById(R.id.nav_view);
//        View headerView = navigationView.getHeaderView(0);
//        ImageView imageView = headerView.findViewById(R.id.setting_icon);
//        imageView.setOnClickListener(v -> {
//            Intent intent = new Intent(home.this, DeleteAccountActivity.class);
//            startActivity(intent);
//        });
    }


    public void onNewIntent(Intent intent) {
        //called when a new intent for this class is created.
        // The main case is when the app was in background, a notification arrives to the tray, and the user touches the notification

        super.onNewIntent(intent);

        Log.d("myTag", "onNewIntent - starting");
        Bundle extras = intent.getExtras();
        if (extras != null) {

            Log.d("myTag", "out of notify" + extras.getString("notify"));


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
            navigationView.getMenu().getItem(0).setChecked(true);
            navigationView.getMenu().getItem(1).setChecked(false);
            navigationView.getMenu().getItem(2).setChecked(false);
            navigationView.getMenu().getItem(3).setChecked(false);
            navigationView.getMenu().getItem(4).setChecked(false);
            navigationView.getMenu().getItem(5).setChecked(false);
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
            navigationView.getMenu().getItem(0).setChecked(true);
        }


    }

    void findVersionFromServer() {

        new Thread(new Runnable() {

            public void run() {


                ArrayList<String> urls = new ArrayList<String>(); //to read each line
                //TextView t; //to show the result, please declare and find it inside onCreate()


                try {
                    // Create a URL for the desired page
                    URL url = new URL("http://graczone.netlify.app/updateVersion.txt"); //My text file location
                    //First open the connection
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(60000); // timing out in a minute

                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    //t=(TextView)findViewById(R.id.TextView1); // ideally do this in onCreate()
                    String str;
                    while ((str = in.readLine()) != null) {
                        urls.add(str);
                    }
                    in.close();
                } catch (Exception e) {
                    Log.d("MyTag", e.toString());
                }

                //since we are in background thread, to post results we have to go back to ui thread. do the following for that

                home.this.runOnUiThread(new Runnable() {
                    public void run() {
//                        t.setText(urls.get(0)); // My TextFile has 3 lines
                        Log.d("myTag", "Ve. " + urls.get(0));
                        serverVersionCode = Integer.parseInt(urls.get(0));
                        if (currentVersionCode < serverVersionCode) {
                            dialog.show();
                            dialog.findViewById(R.id.yesBtn).setOnClickListener(v -> {

                                manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                                File file = new File("/mnt/sdcard/Download/user_manual.apk");
                                boolean isDeleted = false;
                                if (file.exists()) {
                                    isDeleted = file.delete();
                                    Log.d("myTag", "delete and  download!");
                                } else {
                                    Log.d("myTag", "first time download!");
                                }
                                if (file.exists() == isDeleted) {
                                    Uri uri = Uri.parse("https://graczone.netlify.app/app-debug.apk");
                                    DownloadManager.Request request = new DownloadManager.Request(uri);
                                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                                    long reference = manager.enqueue(request);

                                    BroadcastReceiver receiver = new BroadcastReceiver() {
                                        @Override
                                        public void onReceive(Context context, Intent intent) {

                                            String action = intent.getAction();
                                            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                                                Toast.makeText(getApplicationContext(), "Download Completed", Toast.LENGTH_LONG).show();

                                                long downloadId = intent.getLongExtra(
                                                        DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                                                DownloadManager.Query query = new DownloadManager.Query();
                                                query.setFilterById(reference);
                                                Cursor c = manager.query(query);
                                                if (c.moveToFirst()) {
                                                    int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                                                    if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
                                                        String uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));

                                                        Log.d("ainfo", uriString);

                                                        if (downloadId == c.getInt(0)) {
                                                            Log.d("DOWNLOAD PATH:", c.getString(c.getColumnIndex("local_uri")));

                                                            //if your device is rooted then you can install or update app in background directly
                                                            Toast.makeText(getApplicationContext(), "App Installing...Please Wait", Toast.LENGTH_LONG).show();
                                                            File file = new File("/mnt/sdcard/Download/app-debug.apk");
                                                            Log.d("IN INSTALLER:", "/mnt/sdcard/Download/app-debug.apk");
                                                            if (file.exists()) {
                                                                try {
                                                                    String command;
                                                                    Log.d("IN File exists:", "/mnt/sdcard/Download/app-debug.apk");

                                                                    command = "pm install -r " + "/mnt/sdcard/Download/app-debug.apk";
                                                                    Log.d("COMMAND:", command);
                                                                    Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", command});
                                                                    proc.waitFor();
                                                                    Toast.makeText(getApplicationContext(), "App Installed Successfully", Toast.LENGTH_LONG).show();

                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }

                                                        }
                                                    }
                                                }
                                                c.close();
                                            }

                                        }
                                    };

                                    dialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "downloading starting...", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Error in Updating...Please try Later", Toast.LENGTH_LONG).show();
                                }

                            });
                            dialog.findViewById(R.id.noBtn).setOnClickListener(task -> dialog.dismiss());
//                            Intent myIntent = new Intent(getApplicationContext(), ShowNote.class);
//                            myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(myIntent);

                        } else {
                            Log.d("myTag", "currentV: " + currentVersionCode + " " + "seVC " + serverVersionCode);
                        }
                    }
                });

            }
        }).start();

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