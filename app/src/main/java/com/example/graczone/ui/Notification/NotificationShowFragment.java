package com.example.graczone.ui.Notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.graczone.R;

public class NotificationShowFragment extends Fragment {

    TextView title, body, date, time;


    public NotificationShowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification_show, container, false);

        title = view.findViewById(R.id.titleTextView);
        body = view.findViewById(R.id.body2TextView);
        time = view.findViewById(R.id.time2TextView);
        date = view.findViewById(R.id.date2TextView);

        Bundle bundle = getArguments();
        if (bundle != null) {

            title.setText(bundle.getString("title"));
            body.setText(bundle.getString("body"));
            time.setText(bundle.getString("time"));
            date.setText(bundle.getString("date"));
        }


        return view;
    }
}