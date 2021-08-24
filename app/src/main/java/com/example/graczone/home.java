package com.example.graczone;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
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
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

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

    private static final int PERMISSION_REQUEST_CODE = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        findVersionFromServer();


        dialog = new Dialog(this);
        dialog.setContentView(R.layout.installation_popup);

        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.background));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        currentVersionCode = BuildConfig.VERSION_CODE;

//        if (currentVersionCode < serverVersionCode) {
//            dialog.show();
//            dialog.findViewById(R.id.yesBtn).setOnClickListener(v -> {
//
//                manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
//                Uri uri = Uri.parse("https://graczone.netlify.app/graczone.apk");
//                DownloadManager.Request request = new DownloadManager.Request(uri);
//                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
//                long reference = manager.enqueue(request);
//                Toast.makeText(getApplicationContext(), "downloading starting...", Toast.LENGTH_SHORT).show();
//
//            });
//            dialog.findViewById(R.id.noBtn).setOnClickListener(task -> dialog.dismiss());
//        } else {
//            Log.d("myTag", "currentV: " + currentVersionCode + " " + "seVC " + serverVersionCode);
//        }


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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                if (locationAccepted && cameraAccepted) {
                    UpdateApp updateApp = new UpdateApp();
                    updateApp.setContext(home.this);
                    updateApp.execute("https://graczone.synticsapp.com/graczone.apk");
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    void findVersionFromServer() {


        new Thread(new Runnable() {

            public void run() {


                ArrayList<String> urls = new ArrayList<String>(); //to read each line
                //TextView t; //to show the result, please declare and find it inside onCreate()


                try {

                    // Create a URL for the desired page
                    URL url = new URL("https://graczone.synticsapp.com/updateVersion.txt"); //My text file location
                    //First open the connection
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(60000); // timing out in a minute

                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    //t=(TextView)findViewById(R.id.TextView1); // ideally do this in onCreate()
                    String str = "0";

                    while ((str = in.readLine()) != null) {

                        Log.d("myTag", "str: " + str);

                        urls.add(str);
                    }

                    in.close();
                } catch (Exception e) {
                    Log.d("myTag", e.toString());
                }
                Log.d("myTag", "try-catch chalgo");

                //since we are in background thread, to post results we have to go back to ui thread. do the following for that


                home.this.runOnUiThread(new Runnable() {

                    public void run() {
                        Log.d("myTag", "void run start ");
//                        t.setText(urls.get(0)); // My TextFile has 3 lines
                        Log.d("myTag", "Ve. " + urls.get(0));

                        serverVersionCode = Integer.parseInt(urls.get(0));

                        if (currentVersionCode < serverVersionCode) {
                            dialog.show();
                            dialog.findViewById(R.id.yesBtn).setOnClickListener(new View.OnClickListener() {

                                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                @Override
                                public void onClick(View v) {
                                    if (checkPermission()) {
                                        UpdateApp atualizaApp = new UpdateApp();
                                        atualizaApp.setContext(home.this);
                                        atualizaApp.execute("https://graczone.synticsapp.com/graczone.apk");
                                    } else {
                                        requestPermission();
                                    }
                                    dialog.dismiss();
                                }


                            });
                            dialog.findViewById(R.id.noBtn).setOnClickListener(task -> dialog.dismiss());

                        } else {

                            Log.d("myTag", "currentV: " + currentVersionCode + " " + "seVC " + serverVersionCode);
                        }
                    }
                });


            }
        }).start();

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

    public class UpdateApp extends AsyncTask<String, Integer, String> {
        private ProgressDialog mPDialog;
        private Context mContext;

        void setContext(Activity context) {
            mContext = context;
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mPDialog = new ProgressDialog(mContext);
                    mPDialog.setMessage("Please wait...");
                    mPDialog.setIndeterminate(true);
                    mPDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    mPDialog.setCancelable(false);
                    mPDialog.show();
                }
            });
        }

        @Override
        protected String doInBackground(String... arg0) {
            try {

                URL url = new URL(arg0[0]);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(true);
                c.connect();
                int lenghtOfFile = c.getContentLength();
                Log.d("myTag", "len of file: " + lenghtOfFile);

                String PATH = Objects.requireNonNull(mContext.getExternalFilesDir(null)).getAbsolutePath();
                File file = new File(PATH);
                boolean isCreate = file.mkdirs();
                File outputFile = new File(file, "graczone.apk");
                if (outputFile.exists()) {
                    boolean isDelete = outputFile.delete();
                }
                FileOutputStream fos = new FileOutputStream(outputFile);

                InputStream is = c.getInputStream();

                byte[] buffer = new byte[1024];
                int len1;
                long total = 0;
                while ((len1 = is.read(buffer)) != -1) {
                    total += len1;
                    fos.write(buffer, 0, len1);
                    publishProgress((int) ((total * 100) / lenghtOfFile));
                }
                fos.close();
                is.close();

                if (mPDialog != null)
                    mPDialog.dismiss();
                installApk();
            } catch (Exception e) {
                Log.d("myTag", "Update error! " + e.getMessage() + Arrays.toString(e.getStackTrace()));
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mPDialog != null)
                mPDialog.show();

        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (mPDialog != null) {
                mPDialog.setIndeterminate(false);
                mPDialog.setMax(100);
                mPDialog.setProgress(values[0]);
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (mPDialog != null)
                mPDialog.dismiss();
            if (result != null)
                Toast.makeText(mContext, "Download error: " + result, Toast.LENGTH_LONG).show();
            else
                Toast.makeText(mContext, "File Downloaded", Toast.LENGTH_SHORT).show();
        }


        private void installApk() {
            try {
                String PATH = Objects.requireNonNull(mContext.getExternalFilesDir(null)).getAbsolutePath();
                File file = new File(PATH + "/graczone.apk");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                if (Build.VERSION.SDK_INT >= 24) {
                    Uri downloaded_apk = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", file);
                    intent.setDataAndType(downloaded_apk, "application/vnd.android.package-archive");
                    List<ResolveInfo> resInfoList = mContext.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                    for (ResolveInfo resolveInfo : resInfoList) {
                        mContext.grantUriPermission(mContext.getApplicationContext().getPackageName() + ".provider", downloaded_apk, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);
                } else {
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
                    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
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