package com.example.graczone.ui.Notification;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graczone.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class NotificationAdapter extends FirebaseRecyclerAdapter<NotificationModel, NotificationViewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public NotificationAdapter(@NonNull FirebaseRecyclerOptions<NotificationModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NotificationViewHolder notificationViewHolder, int i, @NonNull NotificationModel notificationModel) {

        getRef(i).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String title = snapshot.child("title").getValue().toString();
                String body = snapshot.child("body").getValue().toString();
                String time = snapshot.child("time").getValue().toString();
                String date = snapshot.child("date").getValue().toString();
                notificationViewHolder.notificationTitleTextView.setText(title);
                notificationViewHolder.bodyTextView.setText(body);
                notificationViewHolder.timeTextView.setText(time);
                notificationViewHolder.bodyTextView.setText(date);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_notification_card, parent, false);
        return new NotificationViewHolder(view);
    }
}
