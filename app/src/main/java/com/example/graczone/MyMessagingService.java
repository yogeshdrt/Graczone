package com.example.graczone;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.graczone.ui.Notification.NotificationModel;
import com.google.common.reflect.TypeToken;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyMessagingService extends FirebaseMessagingService {

    public static final String MyPREFERENCES = "MyPrefs";
    private static final String TAG = "MY_TAG";
    String title, body, image;
    ArrayList<NotificationModel> notificationModels;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification() != null) {
            title = remoteMessage.getNotification().getTitle();
            body = remoteMessage.getNotification().getBody();
//            image = remoteMessage.getNotification().getImageUrl().toString();
        }
        Log.d(TAG, "onMessageReceived()");

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            title = remoteMessage.getData().get("data_title");
            body = remoteMessage.getData().get("data_body");
//            image = remoteMessage.getData().get("data_image");
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("H:mm:ss");
        String notificationTime = dateFormat.format(new Date());
        String notificationDate = timeFormat.format(new Date());
        notificationModels = new ArrayList<>();
        NotificationModel notification = new NotificationModel(title, body, notificationTime, notificationDate);
        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        if (!sharedPreferences.contains("models")) {
            notificationModels = new ArrayList<>();
            notificationModels.add(notification);
        } else {
            String json = sharedPreferences.getString("models", null);
            Type type = new TypeToken<ArrayList<NotificationModel>>() {
            }.getType();
            notificationModels = gson.fromJson(json, type);
            notificationModels.add(notification);
        }
        String json1 = gson.toJson(notificationModels);
        editor.putString("models", json1);
        editor.apply();
        Toast.makeText(getApplicationContext(), "save notification", Toast.LENGTH_SHORT).show();
//        FirebaseDatabase.getInstance().getReference("Notifications").child(notificationDate +"+"+ notificationTime).setValue(notification).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if(task.isSuccessful()) {
//                    Toast.makeText(getApplicationContext(), "save notification", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getApplicationContext(), "notification data is not save", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });
        showNotification(title, body);
    }

    public void showNotification(String title,String message) {
        Intent intent = new Intent(this, home.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "MyNotifications")
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setAutoCancel(true)
                .setContentText(message)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.notify(999, builder.build());
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }
}
