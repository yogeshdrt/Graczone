package com.example.graczone.ui.Notification;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graczone.R;

public class NotificationViewHolder extends RecyclerView.ViewHolder {
    TextView notificationTitleTextView, bodyTextView, dateTextView, timeTextView;

    public NotificationViewHolder(@NonNull View itemView) {
        super(itemView);
        notificationTitleTextView = itemView.findViewById(R.id.notificationTitleTextView);
        bodyTextView = itemView.findViewById(R.id.bodyTextView);
        dateTextView = itemView.findViewById(R.id.dateTextView);
        timeTextView = itemView.findViewById(R.id.timeTextView);
    }
}
