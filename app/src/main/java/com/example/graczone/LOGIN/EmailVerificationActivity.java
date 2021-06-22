package com.example.graczone.LOGIN;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.graczone.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;
import java.util.Properties;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static android.content.ContentValues.TAG;

public class EmailVerificationActivity extends AppCompatActivity {

    EditText emailEditText, otpEditText;
    Button verify_btn, otp_btn;
    int otp;
    ProgressDialog progressDialog;
    String emailSend;
    FirebaseAuth auth;
    NetworkChangeListner networkChangeListner = new NetworkChangeListner();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);

        emailEditText = findViewById(R.id.emailEditText);
        otpEditText = findViewById(R.id.otpEditText);
        otp_btn = findViewById(R.id.next_btn);
        verify_btn = findViewById(R.id.verify_btn);
        auth = FirebaseAuth.getInstance();

        verify_btn.setOnClickListener(v -> {
            emailSend = emailEditText.getText().toString();
            if (!Patterns.EMAIL_ADDRESS.matcher(emailSend).matches()) {
                emailEditText.setError("email address is not valid!");

            } else {
                auth.fetchSignInMethodsForEmail(emailSend).addOnCompleteListener(EmailVerificationActivity.this, task -> {
                    if (!Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getSignInMethods()).isEmpty()) {
                        Toast.makeText(getApplicationContext(), "this email already exist!", Toast.LENGTH_SHORT).show();
                    } else if (!task.isSuccessful()) {
                        try {
                            throw Objects.requireNonNull(task.getException());
                        } catch (Exception FirebaseAuthInvalidCredentialsException) {
                            Log.d(TAG, "onComplete:wrong_password");
                        }
                    } else {
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
                        Random random = new Random();
                        otp = random.nextInt(8999) + 1000;
                        String emailTo = "yogeshdrt@gmail.com";
                        String password = "Yogi@123";
                        String emailBody = "Hello,\n" +
                                "\n" +
                                "entered this OTP " + otp + " to verify your email address.";
                        Properties properties = new Properties();
                        properties.put("mail.smtp.auth", "true");
                        properties.put("mail.smtp.starttls.enable", "true");
                        properties.put("mail.smtp.host", "smtp.gmail.com");
                        properties.put("mail.smtp.port", "587");
                        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
                            @Override
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(emailTo, password);
                            }
                        });
                        try {
                            MimeMessage message = new MimeMessage(session);
                            message.setFrom(new InternetAddress(emailTo));
                            message.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(emailSend));
                            message.setSubject("Graczone");
                            message.setText(emailBody);
                            Transport.send(message);
                            Toast.makeText(getApplicationContext(), "otp send successfully", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();


                        } catch (MessagingException e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "error to send otp", Toast.LENGTH_LONG).show();
                            throw new RuntimeException(e);
                        }
                    }
                });
            }


        });
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        otp_btn.setOnClickListener(v -> {
            Log.d("myTag", "" + otp);
            String otp = otpEditText.getText().toString();

            if (otp.isEmpty()) {
                Toast.makeText(getApplicationContext(), "please enter otp", Toast.LENGTH_SHORT).show();
            } else if (otp.equals(String.valueOf(otp))) {
                Intent intent = new Intent(EmailVerificationActivity.this, create_account.class);
                intent.putExtra("email", emailSend);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "enter valid otp", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onStart() {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListner, intentFilter);
        Log.d("myTag", "call on start");
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListner);
        Log.d("myTag", "call on stop");
        super.onStop();
    }

}
