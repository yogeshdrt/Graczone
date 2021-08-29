package com.syntics.graczone.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.syntics.graczone.Matches.duo_Fragment;
import com.syntics.graczone.Matches.solo_Fragment;
import com.syntics.graczone.Matches.squad_Fragment;
import com.syntics.graczone.Matches.tdm_Fragment;
import com.syntics.graczone.R;

public class HomeFragment extends Fragment {
    BottomNavigationView bnv;


    @SuppressLint("NonConstantResourceId")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_home, container, false);


        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.FrameContainer, new solo_Fragment()).commit();


        bnv = root.findViewById(R.id.bottomNavigation);
        bnv.setOnNavigationItemSelectedListener(item -> {
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
                    break;
                case R.id.menu_tdm:
                    frag = new tdm_Fragment();
            }

            assert frag != null;
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.FrameContainer, frag).commit();
            return true;
        });
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