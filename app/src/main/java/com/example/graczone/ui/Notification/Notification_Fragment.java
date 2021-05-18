package com.example.graczone.ui.Notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graczone.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class Notification_Fragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<NotificationModel> notificationModelArrayList;
    DatabaseReference databaseReference;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        notificationModelArrayList = new ArrayList<>();
        NotificationModel notificationModel = new NotificationModel("Demo", "send notification for free", "12:2:1", "15/05/2021");
        notificationModelArrayList.add(notificationModel);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Notifications");
//        FirebaseDatabase.getInstance().getReference("Notifications").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot child1 : snapshot.getChildren()) {
//                    String title = child1.child("title").getValue().toString();
//                    String body = child1.child("body").getValue().toString();
//                    String time = child1.child("time").getValue().toString();
//                    String date = child1.child("date").getValue().toString();
//                    NotificationModel notificationModel1 = new NotificationModel(title, body, time, date);
//                    notificationModelArrayList.add(notificationModel1);
//
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        notificationModelArrayList.add(notificationModel);
        recyclerView = root.findViewById(R.id.notification_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(new NotificationAdapter(notificationModelArrayList));

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<NotificationModel>()
                .setQuery(databaseReference, NotificationModel.class)
                .build();
        NotificationAdapter notificationAdapter = new NotificationAdapter(options);
        recyclerView.setAdapter(notificationAdapter);
        notificationAdapter.startListening();
    }
}