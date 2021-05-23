package com.example.graczone.ui.MyMatches;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graczone.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MyMatchesAdapter extends RecyclerView.Adapter<MyMatchesViewHolder> {

    ArrayList<MyMatchesModel> myMatchesModels;

    public MyMatchesAdapter(ArrayList<MyMatchesModel> myMatchesModels) {
        this.myMatchesModels = myMatchesModels;
    }

    @NonNull
    @NotNull
    @Override
    public MyMatchesViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_squad, parent, false);
        return new MyMatchesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyMatchesViewHolder holder, int position) {
        holder.killTextView.setText(myMatchesModels.get(position).getKillTextView());
        holder.dateTextView.setText(myMatchesModels.get(position).getDateTextView());
        holder.entryFeeTextView.setText(myMatchesModels.get(position).getEntryFeeTextView());
        holder.timeTextView.setText(myMatchesModels.get(position).getTimeTextView());
        holder.mapTextView.setText(myMatchesModels.get(position).getMapTextView());

    }

    @Override
    public int getItemCount() {
        return myMatchesModels.size();
    }
}
