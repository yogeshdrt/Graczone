package com.example.graczone.ui.My_Profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.graczone.R;

public class My_Profile_Fragment extends Fragment {

    TextView usernameEditText, emailEditText, phoneNoEditText;
    String username, email, phone;
    ProgressDialog progressDialog;
    DrawerLayout drawer;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_my_profile, container, false);

//        drawer = root.findViewById(R.id.drawer_layout);
//        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        progressDialog = new ProgressDialog(root.getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

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

        progressDialog.dismiss();

        return root;
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