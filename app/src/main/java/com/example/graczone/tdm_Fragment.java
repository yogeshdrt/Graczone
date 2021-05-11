package com.example.graczone;

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

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class tdm_Fragment extends Fragment {

    private RecyclerView mFirestoreList;
    private FirebaseFirestore firebaseFirestore;

    private FirestoreRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_t_d_m, container, false);


        mFirestoreList = view.findViewById(R.id.firestore_list);
        firebaseFirestore = FirebaseFirestore.getInstance();

        Query query = firebaseFirestore.collection("TDM");
        FirestoreRecyclerOptions<ProductsModel> options = new FirestoreRecyclerOptions.Builder<ProductsModel>()
                .setQuery(query, ProductsModel.class).build();

        adapter = new FirestoreRecyclerAdapter<ProductsModel, tdm_Fragment.ProductsViewHolder>(options) {
            @NonNull
            @Override
            public tdm_Fragment.ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_squad, parent, false);
                return new tdm_Fragment.ProductsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull tdm_Fragment.ProductsViewHolder holder, int position, @NonNull ProductsModel model) {
                holder.time.setText(model.getTime());
                holder.entry_fee.setText(model.getEntry_fee());
                holder.rs_per_kill.setText(model.getRs_per_kill());
                holder.teamup.setText(model.getTeamup());

                holder.rank1.setText(model.getRank1());
                holder.rank2.setText(model.getRank2());
                holder.rank3.setText(model.getRank3());
                holder.date.setText(model.getDate());
                holder.map.setText(model.getMap());


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                        Intent intent = new Intent(getActivity(), joining.class);
                        intent.putExtra("entry_fee", model.getEntry_fee());
                        intent.putExtra("rs_per_kill", model.getRs_per_kill());
                        intent.putExtra("rank1", model.getRank1());
                        intent.putExtra("rank2", model.getRank2());
                        intent.putExtra("rank3", model.getRank3());
                        intent.putExtra("teamup", model.getTeamup());
                        intent.putExtra("map", model.getMap());


                        startActivity(intent);
                    }
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

    private class ProductsViewHolder extends RecyclerView.ViewHolder {
        private TextView time;
        private TextView entry_fee;
        private TextView rs_per_kill;
        private TextView teamup;
        private TextView rank1;
        private TextView rank2;
        private TextView rank3;
        private TextView date;
        private TextView map;

        public ProductsViewHolder(View itemView) {
            super(itemView);

            time = itemView.findViewById(R.id.time);
            entry_fee = itemView.findViewById(R.id.entry_fee);
            rs_per_kill = itemView.findViewById(R.id.rs_per_kill);
            teamup = itemView.findViewById(R.id.teamup);
            rank1 = itemView.findViewById(R.id.rank1);
            rank2 = itemView.findViewById(R.id.rank2);
            rank3 = itemView.findViewById(R.id.rank3);
            date = itemView.findViewById(R.id.date);
            map = itemView.findViewById(R.id.map);


        }
    }




}