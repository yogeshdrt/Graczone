package com.example.graczone.ui.Notification;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graczone.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


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

        Toast.makeText(getActivity(), "swipe right to delete notification", Toast.LENGTH_SHORT).show();

        final NotificationModel[] deleteNotificationModel = new NotificationModel[1];

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
                deleteNotificationModel[0] = notificationModels.get(position);
                notificationModels.remove(position);
                notificationAdapter.notifyDataSetChanged();

                AppCompatActivity activity = (AppCompatActivity) view.getContext();

                Snackbar.make(recyclerView, deleteNotificationModel[0].getTitle(), Snackbar.LENGTH_LONG)
                        .setAction("undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                notificationModels.add(position, deleteNotificationModel[0]);
                                Log.d("myTag", deleteNotificationModel[0].getBody());
                                notificationAdapter.notifyItemInserted(position);
                                SharedPreferences sharedPreferences = activity.getSharedPreferences(MyPREFERENCES, activity.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                Gson gson = new Gson();
                                String json1 = gson.toJson(notificationModels);
                                editor.putString("models", json1);
                                editor.apply();
                            }
                        }).show();

//                AppCompatActivity activity = (AppCompatActivity) view.getContext();

                SharedPreferences sharedPreferences = activity.getSharedPreferences(MyPREFERENCES, activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                String json1 = gson.toJson(notificationModels);
                editor.putString("models", json1);
                editor.apply();

            }

            @Override
            public void onChildDraw(@NonNull @NotNull Canvas c, @NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeRightLabel("Delete").setSwipeRightLabelColor(R.color.black)
                        .addSwipeRightActionIcon(R.drawable.ic_baseline_delete_24)
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(getContext(), R.color.red))
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(notificationAdapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}