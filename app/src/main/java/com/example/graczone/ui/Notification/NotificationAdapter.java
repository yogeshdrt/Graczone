package com.example.graczone.ui.Notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
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
        String time, date, body, title;
        title = notificationModels.get(position).getTitle();
        date = notificationModels.get(position).getDate();
        time = notificationModels.get(position).getTime();
        body = notificationModels.get(position).getBody();
        holder.notificationTitleTextView.setText(title);
        holder.bodyTextView.setText(body);
        holder.timeTextView.setText(time);
        holder.dateTextView.setText(date);

        holder.itemView.setOnClickListener(v -> {
            NotificationShowFragment showFragment = new NotificationShowFragment();
            Bundle bundle = new Bundle();
            bundle.putString("time", time);
            bundle.putString("date", date);
            bundle.putString("body", body);
            bundle.putString("title", title);
            showFragment.setArguments(bundle);
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
            ft.add(R.id.nav_host_fragment, showFragment);
            ft.addToBackStack(null);
            ft.commit();

        });

    }

    @Override
    public int getItemCount() {
        return notificationModels.size();
    }
}
