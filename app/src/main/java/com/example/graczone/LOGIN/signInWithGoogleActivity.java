package com.example.graczone.LOGIN;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.graczone.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class signInWithGoogleActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 100;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_with_google);

        firebaseAuth = FirebaseAuth.getInstance();

        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        signInButton.setOnClickListener(v -> signIn());
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
                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();
                assert account != null;
                firebaseAuthWithGoogle(account, personName);
                Intent intent = new Intent(signInWithGoogleActivity.this, Select_Game.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
                finish();
            }

            // Signed in successfully, show authenticated UI.
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d("myTag", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct, String username) {
        Log.d("myTag", "firebaseAuthWithGoogle:" + acct.getId());
        //Calling get credential from the oogleAuthProviderG
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    //Override th onComplete() to see we are successful or not.
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
// Update UI with the sign-in user's information
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            assert user != null;
                            String userid = user.getUid();
                            String email = user.getEmail();

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", username);
                            hashMap.put("email", email);
                            FirebaseDatabase.getInstance().getReference("Users").child(userid).push().setValue(hashMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                            if (task.isSuccessful()) {
//                                                progressDialog.dismiss();
                                                Toast.makeText(getApplicationContext(), "registration successfully!", Toast.LENGTH_SHORT).show();

                                            } else {
                                                Toast.makeText(getApplicationContext(), "not able to save this data!\n please register again.", Toast.LENGTH_SHORT).show();
//                                                progressDialog.dismiss();
                                            }
                                        }
                                    });
                            Log.d("myTag", "signInWithCredential:success: currentUser: " + email);
                            Toast.makeText(signInWithGoogleActivity.this, "Firebase Authentication Succeeded ", Toast.LENGTH_LONG).show();
                        } else {
// If sign-in fails to display a message to the user.
                            Log.d("myTag", "signInWithCredential:failure", task.getException());
                            Toast.makeText(signInWithGoogleActivity.this, "Firebase Authentication failed:" + task.getException(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}