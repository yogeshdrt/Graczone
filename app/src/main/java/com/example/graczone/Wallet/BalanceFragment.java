package com.example.graczone.Wallet;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.graczone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment;
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener;
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;


public class BalanceFragment extends Fragment implements PaymentStatusListener {

    ImageView addBalanceBtn;
    EditText amountEditText;
    TextView balanceTextView;
    FirebaseUser firebaseUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_balance, container, false);

        amountEditText = view.findViewById(R.id.enter_amount);
        addBalanceBtn = view.findViewById(R.id.addBalanceBtn);
        balanceTextView = view.findViewById(R.id.balance);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("ddMMyyyyHHmmss", Locale.getDefault());
        String transcId = df.format(c);

        addBalanceBtn.setOnClickListener(v -> {
            String amount = amountEditText.getText().toString();
            String upi = "8077982617@okbizaxis";
            String name = "Graczone";
            String desc = "Thank you";
            Log.d("myTag", "button click");

            if (TextUtils.isEmpty(amount) || TextUtils.isEmpty(upi) || TextUtils.isEmpty(name) || TextUtils.isEmpty(desc)) {
                Log.d("myTag", "empty credential when add money");
                Toast.makeText(getContext(), "Please enter all the details..", Toast.LENGTH_SHORT).show();
            } else {
                makePayment(amount, upi, name, desc, transcId);
            }

        });


        return view;

    }

    private void makePayment(String amount, String upi, String name, String desc, String transcationId) {
        // on below line we are calling an easy payment method and passing
        // all parameters to it such as upi id,name, description and others.
        Log.d("myTag", "inner of make Payment");
        try {

            EasyUpiPayment.Builder builder = new EasyUpiPayment.Builder();
            builder.with(getActivity());
            Log.d("myTag", "after with");
            builder.setPayeeVpa(upi);
            Log.d("myTag", "after upi");
            builder.setPayeeName(name);
            Log.d("myTag", "after name");
            builder.setTransactionId(transcationId);
            Log.d("myTag", "after trid");
            builder.setTransactionRefId(transcationId);
            Log.d("myTag", "after trareid");
            builder.setDescription(desc);
            Log.d("myTag", "after des");
            builder.setAmount(amount + ".00");
            Log.d("myTag", "after ammount");
            builder.setPayeeMerchantCode("BCR2DN6TR6LLBDJC");

            final EasyUpiPayment easyUpiPayment = builder.build();


//            final EasyUpiPayment easyUpiPayment = new EasyUpiPayment.Builder()
//                    .with(getActivity())
//                    // on below line we are adding upi id.
//                    .setPayeeVpa(upi)
//                    // on below line we are setting name to which we are making payment.
//                    .setPayeeName(name)
//                    // on below line we are passing transaction id.
//                    .setTransactionId(transcationId)
//                    // on below line we are passing transaction ref id.
//                    .setTransactionRefId(transcationId)
//                    // on below line we are adding description to payment.
//                    .setDescription(desc)
//                    // on below line we are passing amount which is being paid.
//                    .setAmount(amount)
//                    // on below line we are calling a build method to build this ui.
//                    .build();

            // on below line we are calling a start
            // payment method to start a payment.
            Log.d("myTag", "after build");
            easyUpiPayment.startPayment();
            // on below line we are calling a set payment
            // status listener method to call other payment methods.
            Log.d("myTag", "before statusListener");
            easyUpiPayment.setPaymentStatusListener(this);
            Log.d("myTag", "after Status Listener");
        } catch (Exception e) {
            Log.d("myTag", Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void onTransactionCompleted(TransactionDetails transactionDetails) {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("Status", transactionDetails.getStatus());
        hashMap.put("TransactionId", transactionDetails.getTransactionId());
        hashMap.put("ApprovalRefNo", transactionDetails.getApprovalRefNo());
        hashMap.put("TransactionRefNO", transactionDetails.getTransactionRefId());
        hashMap.put("RespondCode", transactionDetails.getResponseCode());
        if (Objects.equals(hashMap.get("Status"), "Success")) {
            int bln = Integer.parseInt(balanceTextView.getText().toString()) + Integer.parseInt(amountEditText.getText().toString());

            balanceTextView.setText(bln);
            FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("Balance").setValue(bln)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("myTag", "add balance in database");
                        } else {
                            Log.d("myTag", "failed to add balance in database");
                        }
                    });
            Toast.makeText(getActivity(), "Transaction successfully completed..", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Transaction failed..", Toast.LENGTH_SHORT).show();
        }
        // on below line we are getting details about transaction when completed.
//        String transcDetails = transactionDetails.getStatus().toString() + "\n" + "Transaction ID : " + transactionDetails.getTransactionId();
//        transactionDetailsTV.setVisibility(View.VISIBLE);
//        // on below line we are setting details to our text view.
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("Transactions").push().setValue(hashMap)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("myTag", "add transaction details in database");
                        Toast.makeText(getContext(), "successfully add Tran. data", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("myTag", "failed to add transaction details in database");
                        Toast.makeText(getContext(), "Transact. data not add", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public void onTransactionSuccess() {
        // this method is called when transaction is successful and we are displaying a toast message.
        Log.d("myTag", "runOnTransactionSuccess");

//        int bln = Integer.parseInt(balanceTextView.getText().toString()) + Integer.parseInt(amountEditText.getText().toString());
//
//        balanceTextView.setText(bln);
//        isSuccess = true;

        Toast.makeText(getActivity(), "Transaction successfully completed..", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTransactionSubmitted() {
        // this method is called when transaction is done
        // but it may be successful or failure.
        Log.e("myTag", "TRANSACTION SUBMIT");
        Toast.makeText(getContext(), "Pending | Submitted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTransactionFailed() {
        // this method is called when transaction is failure.
        Toast.makeText(getContext(), "Failed to complete transaction", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTransactionCancelled() {
        // this method is called when transaction is cancelled.
        Toast.makeText(getContext(), "Transaction cancelled..", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAppNotFound() {
        // this method is called when the users device is not having any app installed for making payment.
        Toast.makeText(getContext(), "No app found for making transaction..", Toast.LENGTH_SHORT).show();
    }
}