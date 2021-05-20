package com.example.graczone.ui.Notification;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graczone.R;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationViewHolder> {

    ArrayList<NotificationModel> notificationModels;

    public NotificationAdapter(ArrayList<NotificationModel> notificationModels) {
        this.notificationModels = notificationModels;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_notification_card, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        holder.notificationTitleTextView.setText(notificationModels.get(position).getTitle());
        holder.bodyTextView.setText(notificationModels.get(position).getBody());
        holder.timeTextView.setText(notificationModels.get(position).getTime());
        holder.dateTextView.setText(notificationModels.get(position).getDate());

    }

    @Override
    public int getItemCount() {
        return notificationModels.size();
    }
}
