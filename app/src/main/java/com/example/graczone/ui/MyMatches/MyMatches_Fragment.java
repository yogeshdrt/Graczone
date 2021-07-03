package com.example.graczone.ui.MyMatches;

import android.content.Context;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MyMatches_Fragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<MyMatchesModel> myMatchesModels;
    MyMatchesAdapter myMatchesAdapter;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
//    NetworkChangeListener networkChangeListener = new NetworkChangeListener();


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
        final String[] key = new String[1];
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        myMatchesAdapter = new MyMatchesAdapter(myMatchesModels);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = (myMatchesModels.size() - 1) - viewHolder.getAdapterPosition();
                deleteMyMatchesModel[0] = myMatchesModels.get(position);
                String subscribe = (deleteMyMatchesModel[0].getDateTextView() + "-" + deleteMyMatchesModel[0].getTeamUp() + "-" + deleteMyMatchesModel[0].getMatch());
                FirebaseMessaging.getInstance().unsubscribeFromTopic(subscribe).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("myTag", "unSubscribe");
                    } else {
                        Log.d("myTag", "not unSubscribe");
                    }
                });
                myMatchesModels.remove(position);
                databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("MyMatches");
                Log.d("myTag", "after reference in show myMatches");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            if (Objects.equals(child.child("dateTextView").getValue(), deleteMyMatchesModel[0].getDateTextView()) && Objects.equals(child.child("timeTextView").getValue(), deleteMyMatchesModel[0].getTimeTextView())) {
                                key[0] = child.getKey();
                                Log.d("myTag", "delete key");
                                child.getRef().removeValue();
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
                myMatchesAdapter.notifyItemRemoved(position);

                AppCompatActivity activity = (AppCompatActivity) root.getContext();

                Snackbar.make(recyclerView, deleteMyMatchesModel[0].getMapTextView(), Snackbar.LENGTH_LONG)
                        .setAction("undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                databaseReference.child(key[0]).setValue(deleteMyMatchesModel[0]);
                                myMatchesModels.add(position, deleteMyMatchesModel[0]);
                                Log.d("myTag", deleteMyMatchesModel[0].getMapTextView());
                                myMatchesAdapter.notifyItemInserted(position);
//                                SharedPreferences sharedPreferences = activity.getSharedPreferences("myMatchesPre", activity.MODE_PRIVATE);
//                                SharedPreferences.Editor editor = sharedPreferences.edit();
//                                Gson gson = new Gson();
//                                String json1 = gson.toJson(myMatchesModels);
//                                editor.putString("myMatchModels", json1);
//                                editor.apply();
                            }
                        }).show();

//                SharedPreferences sharedPreferences = activity.getSharedPreferences("myMatchesPre", activity.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                Gson gson = new Gson();
//                String json1 = gson.toJson(myMatchesModels);
//                editor.putString("myMatchModels", json1);
//                editor.apply();
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

//    @Override
//    public void onStart() {
//        View view = getActivity().getCurrentFocus();
//        AppCompatActivity activity = (AppCompatActivity) view.getContext();
//        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
//        activity.registerReceiver(networkChangeListener, intentFilter);
//        super.onStart();
//    }
//
//    @Override
//    public void onStop() {
//        View view = getActivity().getCurrentFocus();
//        AppCompatActivity activity = (AppCompatActivity) view.getContext();
//        activity.unregisterReceiver(networkChangeListener);
//        super.onStop();
//    }
}