package com.example.graczone.ui.feedback;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.graczone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Feedback_Fragment extends Fragment {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    EditText feedbackEditText;
    Button feedbackButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feedback_, container, false);
        feedbackEditText = view.findViewById(R.id.feedbackEditText);
        feedbackButton = view.findViewById(R.id.feedbackButton);

        feedbackButton.setOnClickListener(v -> {

            String feedbackText = feedbackEditText.getText().toString();

            if (feedbackText.isEmpty()) {
                Toast.makeText(getContext(), "feedback can not be empty", Toast.LENGTH_SHORT).show();
            } else {
                saveFeedback(feedbackText);
            }
        });

        return view;
    }

    public void saveFeedback(String feedbackText) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        assert firebaseUser != null;
        String userId = firebaseUser.getUid();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("email", firebaseUser.getEmail());
        hashMap.put("feedback", feedbackText);
        Log.d("myTag", "feedback: " + feedbackText + " name " + firebaseUser.getDisplayName());

        FirebaseDatabase.getInstance().getReference("Feedback").child(userId).setValue(hashMap)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("myTag", "successfull");
                        Toast.makeText(getContext(), "successfully submit your valuable feedback", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "please Try again your feedback is not save", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}