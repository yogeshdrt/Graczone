package com.example.graczone.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.graczone.R;
import com.example.graczone.duo_Fragment;
import com.example.graczone.solo_Fragment;
import com.example.graczone.squad_Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeFragment extends Fragment {
    BottomNavigationView bnv;

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.FrameContainer, new squad_Fragment()).commit();


        bnv = (BottomNavigationView) root.findViewById(R.id.bottomNavigation);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment frag = null;
                switch (item.getItemId()) {
                    case R.id.menu_squad:
                        frag = new squad_Fragment();
                        break;
                    case R.id.menu_dual:
                        frag = new duo_Fragment();
                        break;
                    case R.id.menu_solo:
                        frag = new solo_Fragment();
                }

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.FrameContainer, frag).commit();
                return true;
            }
        });
        return root;

    }
}