package com.example.graczone;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class squad_Fragment extends Fragment {

    Dialog dialog;
    Button btnn;
    private RecyclerView mFirestoreList;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_squad_, container, false);




      /*  dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.popup_window);

        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.background));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;*/


        mFirestoreList = view.findViewById(R.id.firestore_list);
        firebaseFirestore = FirebaseFirestore.getInstance();

        Query query = firebaseFirestore.collection("SQUAD");
        FirestoreRecyclerOptions<ProductsModel> options = new FirestoreRecyclerOptions.Builder<ProductsModel>()
                .setQuery(query, ProductsModel.class).build();

        adapter = new FirestoreRecyclerAdapter<ProductsModel, ProductsViewHolder>(options) {
            @NonNull
            @Override
            public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single, parent, false);
                return new ProductsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ProductsViewHolder holder, int position, @NonNull ProductsModel model) {
                holder.time.setText(model.getTime());
                holder.entry_fee.setText(model.getEntry_fee());
                holder.rs_per_kill.setText(model.getRs_per_kill());
                holder.teamup.setText(model.getTeamup());
                
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.nav_host_fragment,new joining_Fragment()).addToBackStack(null).commit();


                     Bundle bundle =new Bundle();
                      bundle.putString("entry_fee",model.getEntry_fee());
                      squad_Fragment squad=new squad_Fragment();
                      squad.setArguments(bundle);
                    }
                });

         /*       btnn=dialog.findViewById(R.id.join_btn);

                btnn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
*/





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

        public ProductsViewHolder(View itemView) {
            super(itemView);

            time = itemView.findViewById(R.id.time);
            entry_fee = itemView.findViewById(R.id.entry_fee);
            rs_per_kill = itemView.findViewById(R.id.rs_per_kill);
            teamup = itemView.findViewById(R.id.teamup);
        }
    }




}