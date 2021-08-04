package com.example.graczone.LOGIN;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.messaging.FirebaseMessaging;
import com.shobhitpuri.custombuttons.GoogleSignInButton;

import java.util.Objects;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static android.content.ContentValues.TAG;

public class signInWithGoogleActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 100;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_with_google);

        firebaseAuth = FirebaseAuth.getInstance();

        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        GoogleSignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setTextSize(20);
        signInButton.setPadding(0, 0, 7, 0);
        signInButton.setTextColor(getResources().getColor(R.color.purple_500));


        signInButton.setOnClickListener(v -> signIn());
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
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
                    progressDialog = new ProgressDialog(signInWithGoogleActivity.this);
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
                firebaseAuthWithGoogle(account, personEmail, personName);
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

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct, String emailSend, String username) {
        Log.d("myTag", "firebaseAuthWithGoogle:" + acct.getId());
//        mGoogleSignInClient.signOut();
        //Calling get credential from the oogleAuthProviderG
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        Log.d("myTag", "after credential firebaseAuthWithGoogle:" + acct.getId());
        //Override th onComplete() to see we are successful or not.
        firebaseAuth.fetchSignInMethodsForEmail(emailSend)
                .addOnCompleteListener(signInWithGoogleActivity.this, task -> {
                    if (!Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getSignInMethods()).isEmpty()) {
//                        progressDialog.dismiss();
                        firebaseAuth.signInWithCredential(credential)
                                .addOnCompleteListener(task13 -> {
                                    if (task13.isSuccessful()) {
// Update UI with the sign-in user's information
                                        FirebaseUser user = firebaseAuth.getCurrentUser();
                                        assert user != null;

                                        String email = user.getEmail();
                                        Intent intent = new Intent(signInWithGoogleActivity.this, Select_Game.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                                        startActivity(intent);
                                        finish();
                                        progressDialog.dismiss();

                                        Log.d("myTag", "signInWithCredential:success: currentUser: " + email);
//                        Toast.makeText(signInWithGoogleActivity.this, "Firebase Authentication Succeeded ", Toast.LENGTH_LONG).show();
                                    } else {
// If sign-in fails to display a message to the user.
                                        Log.d("myTag", "signInWithCredential:failure", task.getException());
                                        progressDialog.dismiss();
                                        Toast.makeText(signInWithGoogleActivity.this, "Firebase Authentication failed:" + task.getException(), Toast.LENGTH_LONG).show();
                                    }
//                                    progressDialog.dismiss();
                                });
//                        Toast.makeText(getApplicationContext(), "this email already exist!", Toast.LENGTH_SHORT).show();
                    } else if (!task.isSuccessful()) {
                        progressDialog.dismiss();
                        try {
                            throw Objects.requireNonNull(task.getException());
                        } catch (Exception FirebaseAuthInvalidCredentialsException) {
                            Log.d(TAG, "onComplete:wrong_password");
                        }
                        Log.d("myTag", "in not successfully");
                        Toast.makeText(getApplicationContext(), "Please Try again", Toast.LENGTH_SHORT).show();
                    } else {

                        firebaseAuth.signInWithCredential(credential)
                                .addOnCompleteListener(task14 -> {
                                    if (task14.isSuccessful()) {
// Update UI with the sign-in user's information
                                        FirebaseUser user = firebaseAuth.getCurrentUser();
                                        assert user != null;

                                        String email = user.getEmail();


                                        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task12 -> {
                                            if (task12.isSuccessful()) {
                                                String token = task12.getResult();
                                                FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).setValue(token)
                                                        .addOnCompleteListener(task1 -> {
                                                            if (task1.isSuccessful()) {
                                                                String emailTo = "graczone@synticsapp.com";
                                                                String password = "Deepak@yobro";
                                                                String emailBody = "Hello," + username + " ,\n" +
                                                                        "\n" +
                                                                        "Your account is successfully created.";
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
//                                                                    Toast.makeText(getApplicationContext(), "otp send successfully", Toast.LENGTH_SHORT).show();
//                            progressDialog.dismiss();


                                                                } catch (MessagingException e) {
//                            progressDialog.dismiss();
                                                                    Log.d("myTag", "error to send message");
                                                                    Toast.makeText(getApplicationContext(), "error to send otp", Toast.LENGTH_LONG).show();
                                                                    throw new RuntimeException(e);
                                                                }
                                                                Log.d("myTag", "successfully add data");
                                                                progressDialog.dismiss();
                                                                Intent intent = new Intent(signInWithGoogleActivity.this, Select_Game.class);
                                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                                                                startActivity(intent);
                                                                finish();
                                                            } else {
                                                                Log.d("myTag", "failed to add data");
                                                                progressDialog.dismiss();
                                                            }
//                                                            progressDialog.dismiss();
                                                        });
                                                Log.d("myTag", "token generated" + token);
                                            }

                                        });

//
                                        Log.d("myTag", "signInWithCredential:success: currentUser: " + email);
//                                        progressDialog.dismiss();
//                        Toast.makeText(signInWithGoogleActivity.this, "Firebase Authentication Succeeded ", Toast.LENGTH_LONG).show();
                                    } else {
// If sign-in fails to display a message to the user.
                                        Log.d("myTag", "signInWithCredential:failure", task.getException());
                                        Toast.makeText(signInWithGoogleActivity.this, "Firebase Authentication failed:" + task.getException(), Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();
                                    }
//                                    progressDialog.dismiss();
                                });
                    }
                });
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