package com.example.graczone.ui.Notification;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graczone.R;
import com.google.gson.Gson;

import java.util.ArrayList;


public class Notification_Fragment extends Fragment {

    CardView cardView;

    public static final String MyPREFERENCES = "MyPrefs";

    RecyclerView recyclerView;
    ArrayList<NotificationModel> notificationModels;
    NotificationAdapter notificationAdapter;

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

        notificationAdapter = new NotificationAdapter(notificationModels);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //Remove swiped item from list and notify the RecyclerView
                int position = viewHolder.getAdapterPosition();
                notificationModels.remove(position);
                notificationAdapter.notifyDataSetChanged();

                AppCompatActivity activity = (AppCompatActivity) view.getContext();

                SharedPreferences sharedPreferences = activity.getSharedPreferences(MyPREFERENCES, activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                String json1 = gson.toJson(notificationModels);
                editor.putString("models", json1);
                editor.apply();

            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(notificationAdapter);

        return view;
    }


}