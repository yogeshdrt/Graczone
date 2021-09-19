package com.syntics.graczone.ui.Notification;

import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.syntics.graczone.R;
import com.syntics.graczone.home;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyMessagingService extends FirebaseMessagingService {

    private static final String TAG = "myTag";
    String title, body;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    ArrayList<NotificationModel> notificationModels;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (remoteMessage.getNotification() != null) {
            title = remoteMessage.getNotification().getTitle();
            body = remoteMessage.getNotification().getBody();
//            image = remoteMessage.getNotification().getImageUrl().toString();
        }
        Log.d(TAG, "onMessageReceived()");

//         Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            title = remoteMessage.getData().get("title");
            body = remoteMessage.getData().get("body");
//            image = remoteMessage.getData().get("data_image");
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa");
        String notificationDate = dateFormat.format(new Date());
        String notificationTime = timeFormat.format(new Date());
        notificationModels = new ArrayList<>();
        NotificationModel notification = new NotificationModel(title, body, notificationTime, notificationDate);
//        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        Gson gson = new Gson();
//        if (sharedPreferences.contains("models")) {
//            String json = sharedPreferences.getString("models", null);
//            Type type = new TypeToken<ArrayList<NotificationModel>>() {
//            }.getType();
//            notificationModels = gson.fromJson(json, type);
//        }
//        notificationModels.add(0, notification);
//        String json1 = gson.toJson(notificationModels);
//        editor.putString("models", json1);
//        editor.apply();
//        showNotification(title, body);
        Log.d(TAG, "onMessageReceived()2");
//        Toast.makeText(getApplicationContext(), "save notification", Toast.LENGTH_SHORT).show();
        FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("Notifications").push().setValue(notification).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if (task.isSuccessful()) {
//                    Toast.makeText(getApplicationContext(), "save notification", Toast.LENGTH_SHORT).show();
                    Log.d("myTag", "save notification");
                } else {
//                    Toast.makeText(getApplicationContext(), "notification data is not save", Toast.LENGTH_SHORT).show();
                    Log.d("myTag", "not save notification");
                }
            }
        });
        showNotification(title, body);
        Log.d(TAG, "after method call");

    }

    public void showNotification(String title,String message) {
        Log.d(TAG, "start in method");
        Intent intent = new Intent(this, home.class);
        intent.putExtra("body", "yes");
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "MyNotifications")
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setContentText(message);


        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.notify(999, builder.build());
        Log.d("myTag", "show notification called.");

    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        assert firebaseUser != null;
        String userId = firebaseUser.getUid();
        FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Token").setValue(s)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("myTag", "token add successfully");
                    } else {
                        Log.d("myTag", "failed to add token");
                    }

                });
        Log.d("myTag", "Token generated");
    }
}
