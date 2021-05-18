package com.example.graczone.ui.Settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.graczone.R;

public class Settings_Fragment extends Fragment {

    TextView usernameEditText, emailEditText, phoneNoEditText;
    String username, email, phone;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        username = "nothing";
        email = "nothing2";
        phone = "nothing3";

        Bundle bundle = getArguments();
        if (bundle != null) {
            username = bundle.getString("arg1");
            email = bundle.getString("arg2");
            phone = bundle.getString("arg3");
        }

        usernameEditText = root.findViewById(R.id.usernameTextView);
        usernameEditText.setText(username);
        emailEditText = root.findViewById(R.id.email2TextView);
        emailEditText.setText(email);
        phoneNoEditText = root.findViewById(R.id.phoneNoTextView);
        phoneNoEditText.setText(phone);

        return root;
    }
}