package com.example.graczone;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.graczone.LOGIN.create_account;

import java.util.Properties;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailVerificationActivity extends AppCompatActivity {

    EditText emailEditText, otpEditText;
    Button verify_btn, otp_btn;
    int otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);

        emailEditText = findViewById(R.id.emailEditText);
        otpEditText = findViewById(R.id.otpEditText);
        otp_btn = findViewById(R.id.next_btn);
        verify_btn = findViewById(R.id.verify_btn);

        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                otp = random.nextInt(8999) + 1000;
                String emailSend = emailEditText.getText().toString();
                String emailTo = "yogeshdrt@gmail.com";
                String password = "Yogi@123";
                String emailbody = "Hello,\n" +
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
                    message.setText(emailbody);
                    Transport.send(message);
                    Toast.makeText(getApplicationContext(), "otp send successfully", Toast.LENGTH_SHORT).show();


                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }


            }

        });
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        otp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("myTag", "" + otp);

                if (otpEditText.getText().toString().equals(String.valueOf(otp))) {
                    Intent intent = new Intent(EmailVerificationActivity.this, create_account.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

}
