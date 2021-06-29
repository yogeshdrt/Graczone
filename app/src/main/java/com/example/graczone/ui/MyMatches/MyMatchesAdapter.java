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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_my_matches, parent, false);
        return new MyMatchesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyMatchesViewHolder holder, int position) {
        int length = myMatchesModels.size();
        holder.killTextView.setText(myMatchesModels.get(length - (position + 1)).getKillTextView());
        holder.dateTextView.setText(myMatchesModels.get(length - (position + 1)).getDateTextView());
        holder.entryFeeTextView.setText(myMatchesModels.get(length - (position + 1)).getEntryFeeTextView());
        holder.timeTextView.setText(myMatchesModels.get(length - (position + 1)).getTimeTextView());
        holder.mapTextView.setText(myMatchesModels.get(length - (position + 1)).getMapTextView());
        holder.rank1TextView.setText(myMatchesModels.get(length - (position + 1)).getRank1TextView());
        holder.rank2TextView.setText(myMatchesModels.get(length - (position + 1)).getRank2TextView());
        holder.rank3TextView.setText(myMatchesModels.get(length - (position + 1)).getRank3TextView());
        holder.rank2lTextView.setText(myMatchesModels.get(length - (position + 1)).getRank2lTextView());
        holder.rank3lTextView.setText(myMatchesModels.get(length - (position + 1)).getRank3lTextView());
        switch (myMatchesModels.get(length - (position + 1)).getTeamUp()) {
            case "SQUAD":
                holder.imageView.setImageResource(R.drawable.ic_baseline_groups_24);
                break;
            case "TDM":
                holder.imageView.setImageResource(R.drawable.tdm_icon);
                break;
            case "SOLO":
                holder.imageView.setImageResource(R.drawable.ic_baseline_person_24);
                break;
            case "DUO":
                holder.imageView.setImageResource(R.drawable.ic_baseline_group_24);
                break;
        }


    }

    @Override
    public int getItemCount() {
        return myMatchesModels.size();
    }
}
