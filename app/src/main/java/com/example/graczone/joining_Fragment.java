package com.example.graczone;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class joining_Fragment extends Fragment {

    TextView entry,pubg_tourna,upcomiing;
    RecyclerView firestore_list;
    BottomNavigationView bnv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_joining_, container, false);

    }

 





    @Override
    public void onResume() {
        super.onResume();

        // On resume, make everything in activity_main invisible except this fragment.
        pubg_tourna = getActivity().findViewById(R.id.pubg_tourna);
        upcomiing = getActivity().findViewById(R.id.upcomiing);
        firestore_list = getActivity().findViewById(R.id.firestore_list);
        bnv = getActivity().findViewById(R.id.bottomNavigation);


        pubg_tourna.setVisibility(View.GONE);
        upcomiing.setVisibility(View.GONE);
        firestore_list.setVisibility(View.GONE);
        bnv.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();

        // Make everything visible again
        pubg_tourna = getActivity().findViewById(R.id.pubg_tourna);
        upcomiing = getActivity().findViewById(R.id.upcomiing);
        firestore_list = getActivity().findViewById(R.id.firestore_list);
        bnv = getActivity().findViewById(R.id.bottomNavigation);

        pubg_tourna.setVisibility(View.VISIBLE);
        upcomiing.setVisibility(View.VISIBLE);
        firestore_list.setVisibility(View.VISIBLE);
        bnv.setVisibility(View.VISIBLE);
    }
}