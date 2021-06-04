package com.example.graczone.ui.MyMatches;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class MyMatches_Fragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<MyMatchesModel> myMatchesModels;
    MyMatchesAdapter myMatchesAdapter;


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

        final MyMatchesModel[] deleteMyMatchesModel = new MyMatchesModel[1];

        myMatchesAdapter = new MyMatchesAdapter(myMatchesModels);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                deleteMyMatchesModel[0] = myMatchesModels.get(position);
                myMatchesModels.remove(position);
                myMatchesAdapter.notifyItemRemoved(position);

                AppCompatActivity activity = (AppCompatActivity) root.getContext();

                Snackbar.make(recyclerView, deleteMyMatchesModel[0].getMapTextView(), Snackbar.LENGTH_LONG)
                        .setAction("undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myMatchesModels.add(position, deleteMyMatchesModel[0]);
                                Log.d("myTag", deleteMyMatchesModel[0].getMapTextView());
                                myMatchesAdapter.notifyItemInserted(position);
                                SharedPreferences sharedPreferences = activity.getSharedPreferences("myMatchesPre", activity.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                Gson gson = new Gson();
                                String json1 = gson.toJson(myMatchesModels);
                                editor.putString("myMatchModels", json1);
                                editor.apply();
                            }
                        }).show();

                SharedPreferences sharedPreferences = activity.getSharedPreferences("myMatchesPre", activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                String json1 = gson.toJson(myMatchesModels);
                editor.putString("myMatchModels", json1);
                editor.apply();
            }

            public void onChildDraw(@NonNull @NotNull Canvas c, @NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(getContext(), R.color.red))
                        .addSwipeRightLabel("Delete").setSwipeRightLabelColor(R.color.black)
                        .addSwipeRightActionIcon(R.drawable.ic_baseline_delete_24)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(myMatchesAdapter);

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