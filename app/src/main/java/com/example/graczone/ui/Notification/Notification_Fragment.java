package com.example.graczone.ui.Notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graczone.R;

import java.util.ArrayList;


public class Notification_Fragment extends Fragment {

    CardView cardView;

    RecyclerView recyclerView;
    ArrayList<NotificationModel> notificationModels;

    public Notification_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        recyclerView = view.findViewById(R.id.notification_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        notificationModels = new ArrayList<>();
        Bundle bundle = getArguments();
        if (bundle != null) {
            notificationModels = (ArrayList<NotificationModel>) bundle.getSerializable("models");
        }

        recyclerView.setAdapter(new NotificationAdapter(notificationModels));


        return view;
    }

}