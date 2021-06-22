package com.example.graczone.Wallet;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.graczone.LOGIN.NetworkChangeListner;
import com.example.graczone.R;


public class RecentTransactionFragment extends Fragment {
    Button balance;

    NetworkChangeListner networkChangeListner = new NetworkChangeListner();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recent_transaction, container, false);


        return view;
    }

    @Override
    public void onStart() {
        View view = getActivity().getCurrentFocus();
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        activity.registerReceiver(networkChangeListner, intentFilter);
        super.onStart();
    }

    @Override
    public void onStop() {
        View view = getActivity().getCurrentFocus();
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        activity.unregisterReceiver(networkChangeListner);
        super.onStop();
    }
}