package com.example.graczone.ui.MyMatches;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graczone.R;

import java.util.ArrayList;

public class MyMatches_Fragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<MyMatchesModel> myMatchesModels;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_matches, container, false);
        recyclerView = root.findViewById(R.id.get_cardview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        myMatchesModels = new ArrayList<>();
        Bundle bundle = getArguments();
        if (bundle != null) {
            myMatchesModels = (ArrayList<MyMatchesModel>) bundle.getSerializable("myMatchModels");
        }

        recyclerView.setAdapter(new MyMatchesAdapter(myMatchesModels));

        return root;
    }
}