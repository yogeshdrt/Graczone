package com.example.graczone.Matches;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graczone.ProductsModel;
import com.example.graczone.R;
import com.example.graczone.joining_TDM;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class tdm_Fragment extends Fragment {

    private FirestoreRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_t_d_m, container, false);


        RecyclerView mFirestoreList = view.findViewById(R.id.firestore_list);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        Query query = firebaseFirestore.collection("TDM");
        FirestoreRecyclerOptions<ProductsModel> options = new FirestoreRecyclerOptions.Builder<ProductsModel>()
                .setQuery(query, ProductsModel.class).build();

        adapter = new FirestoreRecyclerAdapter<ProductsModel, tdm_Fragment.ProductsViewHolder>(options) {
            @NonNull
            @Override
            public tdm_Fragment.ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_squad, parent, false);
                return new ProductsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull tdm_Fragment.ProductsViewHolder holder, int position, @NonNull ProductsModel model) {
                holder.time.setText(model.getTime());
                holder.entry_fee.setText(model.getEntry_fee());
                holder.rs_per_kill.setText(model.getRs_per_kill());
                holder.teamup.setText(model.getTeamup());
                holder.rank1.setText(model.getRank1());
                holder.date.setText(model.getDate());
                holder.map.setText(model.getMap());
                holder.count.setText((model.getCount() + "/8"));
                holder.match.setText(model.getMatch());
                holder.linearProgressIndicator.setProgress(Integer.parseInt(model.getCount()));

                if (Integer.parseInt(model.getCount()) == 8) {

                    holder.itemView.setEnabled(false);
                    holder.count.setText("Full");

                }

                holder.itemView.setOnClickListener(v -> {
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    Intent intent = new Intent(getActivity(), joining_TDM.class);
                    intent.putExtra("entry_fee", model.getEntry_fee());
                    intent.putExtra("rs_per_kill", model.getRs_per_kill());
                    intent.putExtra("teamup", model.getTeamup());
                    intent.putExtra("map", model.getMap());
                    intent.putExtra("rank1", model.getRank1());
                    intent.putExtra("time", model.getTime());
                    intent.putExtra("date", model.getDate());
                    intent.putExtra("match", model.getMatch());
                    intent.putExtra("count", model.getCount());

                    startActivity(intent);
                });
            }
        };


        mFirestoreList.setHasFixedSize(true);
        mFirestoreList.setLayoutManager(new LinearLayoutManager(getContext()));
        mFirestoreList.setAdapter(adapter);


        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    private static class ProductsViewHolder extends RecyclerView.ViewHolder {
        private final TextView time;
        private final TextView entry_fee;
        private final TextView rs_per_kill;
        private final TextView teamup;
        private final TextView date;
        private final TextView map;
        private final TextView rank1;
        private final TextView match;
        private final TextView count;
        private final LinearProgressIndicator linearProgressIndicator;

        public ProductsViewHolder(View itemView) {
            super(itemView);

            time = itemView.findViewById(R.id.time);
            entry_fee = itemView.findViewById(R.id.entryFee);
            rs_per_kill = itemView.findViewById(R.id.rs_per_kill);
            teamup = itemView.findViewById(R.id.teamup);
            date = itemView.findViewById(R.id.date);
            map = itemView.findViewById(R.id.map);
            rank1 = itemView.findViewById(R.id.rank1);
            match = itemView.findViewById(R.id.match_number);
            count = itemView.findViewById(R.id.countTextView);
            linearProgressIndicator = itemView.findViewById(R.id.filling_progressbar);
            linearProgressIndicator.setMax(8);


        }
    }




}