package com.example.graczone.LOGIN;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.graczone.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.Properties;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailVerificationActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 100;
    EditText otpEditText;
    Button verify_btn, otp_btn;
    Dialog dialog;
    int otp;
    TextView setUsername, setEmail;
    ProgressDialog progressDialog;
    String emailSend;
    FirebaseAuth auth;
    GoogleSignInAccount acct;
    GoogleSignInClient mGoogleSignInClient;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);

        acct = GoogleSignIn.getLastSignedInAccount(this);
        setEmail = findViewById(R.id.setEmail);
        setUsername = findViewById(R.id.setUsername);
        setEmail.setText(acct.getEmail());
        setUsername.setText(acct.getDisplayName());
        user = FirebaseAuth.getInstance().getCurrentUser();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        dialog = new Dialog(this);
        dialog.setContentView(R.layout.delete_account_popup);

        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.background));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);


        otpEditText = findViewById(R.id.otpEditText);
        otp_btn = findViewById(R.id.next_btn);
        verify_btn = findViewById(R.id.verify_btn);
        auth = FirebaseAuth.getInstance();

        verify_btn.setOnClickListener(v -> {
//            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(emailEditText.getWindowToken(), 0);
            try {
                progressDialog = new ProgressDialog(EmailVerificationActivity.this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                Log.d("myTag", "show progress dialog in email verification");
            } catch (Exception e) {
                Log.d("myTag", "error to show progress dialog in email verification");
            }
            emailSend = setEmail.getText().toString();

//
            Random random = new Random();
            otp = random.nextInt(8999) + 1000;
            String emailTo = "yogeshdrt@gmail.com";
            String password = "Yogi@123";
            String emailBody = "Hello,\n" +
                    "\n" +
                    "enter this OTP " + otp + " to verify your email address.";
            Properties properties = new Properties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");
            Session session = null;
            try {
                session = Session.getInstance(properties, new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(emailTo, password);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "please Try again", Toast.LENGTH_SHORT).show();
            }
            Log.d("myTag", "after session");
            try {
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(emailTo));
                message.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(emailSend));
                message.setSubject("Graczone");
                message.setText(emailBody);
                Transport.send(message);
                Toast.makeText(getApplicationContext(), "otp send successfully", Toast.LENGTH_SHORT).show();

            } catch (MessagingException e) {
                Log.d("myTag", "error to send message");
                Toast.makeText(getApplicationContext(), "error to send otp", Toast.LENGTH_LONG).show();
                throw new RuntimeException(e);
            }
            progressDialog.dismiss();
        });
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        otp_btn.setOnClickListener(v -> {
            Log.d("myTag", "" + otp);
            String otp = otpEditText.getText().toString();

            if (otp.isEmpty()) {
                Toast.makeText(getApplicationContext(), "please enter otp", Toast.LENGTH_SHORT).show();
            } else if (otp.equals(String.valueOf(otp))) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                imm.hideSoftInputFromWindow(otpEditText.getWindowToken(), 0);
                dialog.show();

                dialog.findViewById(R.id.yesBtn).setOnClickListener(v1 -> signIn());
                dialog.findViewById(R.id.noBtn).setOnClickListener(task -> dialog.dismiss());
            } else {
                Toast.makeText(getApplicationContext(), "enter valid otp", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void deleteAccount(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        user.reauthenticate(credential).addOnCompleteListener(task21 -> {
            if (task21.isSuccessful()) {
                FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).removeValue()
                        .addOnCompleteListener(task22 -> {
                            if (task22.isSuccessful()) {
                                Objects.requireNonNull(user).delete().addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        mGoogleSignInClient.revokeAccess().addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                dialog.dismiss();
                                                Toast.makeText(getApplicationContext(), "delete account successfully", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(EmailVerificationActivity.this, signInWithGoogleActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(getApplicationContext(), "failed to delete google account", Toast.LENGTH_SHORT).show();
                                            }

                                        });
                                    } else {
                                        Toast.makeText(getApplicationContext(), "failed to delete account please try again" + user.getEmail(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(getApplicationContext(), "failed to delete account please try again" + user.getEmail(), Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(getApplicationContext(), "failed to delete account please try again" + user.getEmail(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                Log.d("myTag", "acct null");
                try {
                    progressDialog = new ProgressDialog(EmailVerificationActivity.this);
                    progressDialog.show();
                    progressDialog.setContentView(R.layout.progress_dialog);
                    progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setCancelable(false);
                } catch (Exception e) {
                    Log.d("myTag", "error to show progress bar");
                }
                String personName = acct.getDisplayName();
//                String personGivenName = acct.getGivenName();
//                String personFamilyName = acct.getFamilyName();
                String personEmail = acct.getEmail();
//                String personId = acct.getId();
//                Uri personPhoto = acct.getPhotoUrl();
                assert account != null;
                deleteAccount(account);
//                Intent intent = new Intent(signInWithGoogleActivity.this, Select_Game.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                startActivity(intent);
//                finish();
            }

            // Signed in successfully, show authenticated UI.
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d("myTag", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
