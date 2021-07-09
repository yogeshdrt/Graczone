package com.example.graczone.Wallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import com.example.graczone.R;


public class RecentTransactionFragment extends Fragment {
    Button balance;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recent_transaction, container, false);

        //fragment back press handling--------------------------------
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
            }

        };
        requireActivity().getOnBackPressedDispatcher().addCallback(callback);
        //----------------------------------

        return view;
    }

}