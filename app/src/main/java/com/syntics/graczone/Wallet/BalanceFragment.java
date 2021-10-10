package com.syntics.graczone.Wallet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.razorpay.Checkout;
import com.syntics.graczone.R;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import dev.shreyaspatil.easyupipayment.EasyUpiPayment;
import dev.shreyaspatil.easyupipayment.listener.PaymentStatusListener;
import dev.shreyaspatil.easyupipayment.model.PaymentApp;
import dev.shreyaspatil.easyupipayment.model.TransactionDetails;


public class BalanceFragment extends Fragment implements PaymentStatusListener {

    ImageButton addBalanceBtn;
    EditText amountEditText;
    TextView balanceTextView;
    FirebaseUser firebaseUser;
    Button withdraw, rs100, rs200, rs500;
    LinearLayout linearLayout;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_balance, container, false);

        Checkout.preload(requireContext());


        withdraw = view.findViewById(R.id.withdraw_btn);
        withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), withdraw.class);
                startActivity(intent);
            }
        });

        linearLayout = view.findViewById(R.id.wallet_linear_layout);
        linearLayout.setOnClickListener(v -> {
            InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        });

        //wallet amount auto-fill code ------------------------------------


        rs100 = view.findViewById(R.id.rs100_btn);
        rs200 = view.findViewById(R.id.rs200_btn);
        rs500 = view.findViewById(R.id.rs500_btn);

        rs100.setOnClickListener(v -> amountEditText.setText("100"));

        rs200.setOnClickListener(v -> amountEditText.setText("200"));

        rs500.setOnClickListener(v -> amountEditText.setText("500"));

        //-------------------------------------

        amountEditText = view.findViewById(R.id.enter_amount);
        addBalanceBtn = view.findViewById(R.id.addBalanceBtn);
        balanceTextView = view.findViewById(R.id.balance);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("ddMMyyyyHHmmss", Locale.getDefault());
        String transcId = df.format(c);

        addBalanceBtn.setOnClickListener(v -> {
            String amount = amountEditText.getText().toString();
            String upi = "8077982617@ybl";
            String name = "Syntics App Development private limited";
            String desc = "Thank you";
            Log.d("myTag", "button click");

            if (TextUtils.isEmpty(amount) || TextUtils.isEmpty(upi) || TextUtils.isEmpty(name) || TextUtils.isEmpty(desc)) {
                Log.d("myTag", "empty credential when add money");
                Toast.makeText(getContext(), "Please enter all the details..", Toast.LENGTH_SHORT).show();
            } else {
                makePayment(amount, upi, name, desc, transcId);
//                startPayment(amount);
            }

        });

        return view;

    }

//    private void startPayment(String amount) {
//
//        Checkout checkout = new Checkout();
//        checkout.setKeyID("rzp_test_9lPxISfIV3o3Dl");
//
//        checkout.setImage(R.drawable.logo);
//
//        /**
//         * Reference to current activity
//         */
//        final Activity activity = getActivity();
//
//        /**
//         * Pass your payment options to the Razorpay Checkout as a JSONObject
//         */
//        try {
//            JSONObject options = new JSONObject();
//
//            options.put("name", "Graczone");
//            options.put("description", "Reference No. #123456");
//            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
////            options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
//            options.put("theme.color", "#3399cc");
//            options.put("currency", "INR");
//            options.put("amount", amount + "00");//pass amount in currency subunits
//            options.put("prefill.email", "gaurav.kumar@example.com");
//            options.put("prefill.contact", "6395953457");
//            JSONObject retryObj = new JSONObject();
//            retryObj.put("enabled", true);
//            retryObj.put("max_count", 4);
//            options.put("retry", retryObj);
//
//            checkout.open(activity, options);
//
//        } catch (Exception e) {
//            Log.d("myTag", "Error in starting Razorpay Checkout", e);
//        }
//
//    }
//
//    @Override
//    public void onPaymentSuccess(String s) {
//        Log.d("myTag", "runOnTransactionSuccess");
//
//        int bln = Integer.parseInt(balanceTextView.getText().toString()) + Integer.parseInt(amountEditText.getText().toString());
//
//        balanceTextView.setText(bln);
//        FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("Balance").setValue(bln)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        Log.d("myTag", "add balance in database");
//                    } else {
//                        Log.d("myTag", "failed to add balance in database");
//                    }
//                });
//        Toast.makeText(getActivity(), "Transaction successfully completed..", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onPaymentError(int i, String s) {
//
//        Toast.makeText(getContext(), "Transaction cancelled..", Toast.LENGTH_SHORT).show();
//
//    }

    private void makePayment(String amount, String upi, String name, String desc, String transcationId) {
        // on below line we are calling an easy payment method and passing
        // all parameters to it such as upi id,name, description and others.
        Log.d("myTag", "inner of make Payment");
        try {

            PaymentApp paymentApp;
            paymentApp = PaymentApp.ALL;

//            switch (paymentAppChoice.getId()) {
//                case R.id.app_default:
//                    paymentApp = PaymentApp.ALL;
//                    break;
//                case R.id.app_amazonpay:
//                    paymentApp = PaymentApp.AMAZON_PAY;
//                    break;
//                case R.id.app_bhim_upi:
//                    paymentApp = PaymentApp.BHIM_UPI;
//                    break;
//                case R.id.app_google_pay:
//                    paymentApp = PaymentApp.GOOGLE_PAY;
//                    break;
//                case R.id.app_phonepe:
//                    paymentApp = PaymentApp.PHONE_PE;
//                    break;
//                case R.id.app_paytm:
//                    paymentApp = PaymentApp.PAYTM;
//                    break;
//                default:
//                    throw new IllegalStateException("Unexpected value: " + paymentAppChoice.getId());
//            }

            EasyUpiPayment.Builder builder = new EasyUpiPayment.Builder(requireActivity());
            builder.with(paymentApp);
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
            builder.setPayeeMerchantCode("BCR2DN6TZ6K4DF2K");

            final EasyUpiPayment easyUpiPayment = builder.build();


//            final EasyUpiPayment easyUpiPayment = new EasyUpiPayment.Builder()
//                    .with(getActivity())
//                    // on below line we are adding upi id.
//                    .setPayeeVpa(upi)
//                    // on below line we are setting name to which we are making oayment.
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

        switch (transactionDetails.getTransactionStatus()) {
            case SUCCESS:
                onTransactionSuccess();
                break;
            case FAILURE:
                onTransactionFailed();
                break;
            case SUBMITTED:
                onTransactionSubmitted();
                break;
        }

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("Status", transactionDetails.getTransactionStatus().toString());
        hashMap.put("TransactionId", transactionDetails.getTransactionId());
        hashMap.put("ApprovalRefNo", transactionDetails.getApprovalRefNo());
        hashMap.put("TransactionRefNO", transactionDetails.getTransactionRefId());
        hashMap.put("RespondCode", transactionDetails.getResponseCode());

//        if (Objects.equals(hashMap.get("Status"), "Success")) {
//            int bln = Integer.parseInt(balanceTextView.getText().toString()) + Integer.parseInt(amountEditText.getText().toString());
//
//            balanceTextView.setText(bln);
//            FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("Balance").setValue(bln)
//                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            Log.d("myTag", "add balance in database");
//                        } else {
//                            Log.d("myTag", "failed to add balance in database");
//                        }
//                    });
//            Toast.makeText(getActivity(), "Transaction successfully completed..", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(getActivity(), "Transaction failed..", Toast.LENGTH_SHORT).show();
//        }
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

    public void onTransactionSuccess() {
        // this method is called when transaction is successful and we are displaying a toast message.
        Log.d("myTag", "runOnTransactionSuccess");

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
    }

    public void onTransactionSubmitted() {
        // this method is called when transaction is done
        // but it may be successful or failure.
        Log.e("myTag", "TRANSACTION SUBMIT");
        Toast.makeText(getContext(), "Pending | Submitted", Toast.LENGTH_SHORT).show();
    }

    public void onTransactionFailed() {
        // this method is called when transaction is failure.
        Toast.makeText(getContext(), "Failed to complete transaction", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTransactionCancelled() {
        // this method is called when transaction is cancelled.
        Toast.makeText(getContext(), "Transaction cancelled..", Toast.LENGTH_SHORT).show();
    }

//    @Override
//    public void onAppNotFound() {
//        // this method is called when the users device is not having any app installed for making payment.
//        Toast.makeText(getContext(), "No app found for making transaction..", Toast.LENGTH_SHORT).show();
//    }

}