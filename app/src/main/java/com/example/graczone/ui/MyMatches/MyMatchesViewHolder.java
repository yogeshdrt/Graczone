package com.example.graczone.ui.MyMatches;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graczone.R;

import org.jetbrains.annotations.NotNull;

public class MyMatchesViewHolder extends RecyclerView.ViewHolder {

    TextView mapTextView, timeTextView, dateTextView, entryFeeTextView, killTextView, rank1TextView, rank2TextView, rank3TextView;

    public MyMatchesViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        mapTextView = itemView.findViewById(R.id.map);
        timeTextView = itemView.findViewById(R.id.time);
        dateTextView = itemView.findViewById(R.id.date);
        entryFeeTextView = itemView.findViewById(R.id.entryFee);
        killTextView = itemView.findViewById(R.id.rs_per_kill);
        rank1TextView = itemView.findViewById(R.id.rank1);
        rank2TextView = itemView.findViewById(R.id.rank2);
        rank3TextView = itemView.findViewById(R.id.rank3);
    }
}
